package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.bylazar.ftcontrol.panels.plugins.html.primitives.P;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.DeployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.ResetElevatorCommand;
import org.firstinspires.ftc.teamcode.commands.ResetExtendoCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LEDSubsystem;
import org.stealthrobotics.library.commands.SaveAutoHeadingCommand;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "SpecimenAuto")
public class SpecimenAuto extends StealthOpMode {
    private FollowerSubsystem follower;
    private ElevatorSubsystem elevator;
    private ExtendoSubsystem extendo;
    private IntakeSubsystem intake;
    private ClawSubsystem claw;
    private LEDSubsystem led;

    private final Pose startPose = new Pose(8.67, 64.72, 0);

    private final Pose scoringPose1 = new Pose(35, 80, 0);
    private final Pose scoringPose2 = new Pose(40, 75, 0);
    private final Pose scoringPose3 = new Pose(42, 65, -10);

    private final Pose pickupPose1 = new Pose(12, 40, Math.toRadians(180));
    private final Pose pickupPose1Forward = new Pose(10, 40, Math.toRadians(180));
    private final Pose pickupPose2 = new Pose(12, 40, Math.toRadians(175));
    private final Pose pickupPose2Forward = new Pose(8.5, 40, Math.toRadians(175));

    private final Pose firstSampleSweepPose = new Pose(28, 40, Math.toRadians(137));
    private final Pose firstSampleSweptPose = new Pose(28, 43, Math.toRadians(40));

    private PathChain score1, score2, score3, pickup1, pickup2, goToFirstSample, moveFirstSample;

    @Override
    public void initialize() {
        follower = new FollowerSubsystem(hardwareMap);
        follower.setStartPose(startPose);

        elevator = new ElevatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        led = new LEDSubsystem(hardwareMap);
        extendo = new ExtendoSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);

        schedule(
                new InstantCommand(() -> intake.wristHome()),
                new InstantCommand(() -> led.setMode(LEDSubsystem.LEDMode.AUTONOMOUS))
        );

        buildPaths();
    }

    public void buildPaths() {
        pickup1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(firstSampleSweptPose), new Point(pickupPose1)))
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.3))
                .setLinearHeadingInterpolation(firstSampleSweptPose.getHeading(), pickupPose1.getHeading())

                .addPath(new BezierCurve(new Point(pickupPose1), new Point(pickupPose1Forward)))
                .setLinearHeadingInterpolation(pickupPose1.getHeading(), pickupPose1Forward.getHeading())
                .setPathEndTimeoutConstraint(1000)
                .build();

        pickup2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scoringPose2), new Point(pickupPose2)))
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.3))
                .setLinearHeadingInterpolation(scoringPose2.getHeading(), pickupPose2.getHeading())

                .addPath(new BezierCurve(new Point(pickupPose2), new Point(pickupPose2Forward)))
                .setLinearHeadingInterpolation(pickupPose2.getHeading(), pickupPose2Forward.getHeading())
                .setPathEndTimeoutConstraint(1000)
                .build();

        //Scoring
        score1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose), new Point(scoringPose1)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scoringPose1.getHeading())
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.5))
                .build();

        score2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(pickupPose1Forward), new Point(scoringPose2)))
                .setLinearHeadingInterpolation(pickupPose1Forward.getHeading(), scoringPose2.getHeading())
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.5))
                .build();

        score3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(pickupPose2Forward), new Point(19.46, 81.38), new Point(19.23, 63.79), new Point(scoringPose3)))
                .setLinearHeadingInterpolation(pickupPose2Forward.getHeading(), scoringPose3.getHeading())
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.5))
                .build();

        goToFirstSample = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose1), new Point(firstSampleSweepPose)))
                .setLinearHeadingInterpolation(scoringPose1.getHeading(), firstSampleSweepPose.getHeading())
                .build();

        moveFirstSample = follower.pathBuilder()
                .addPath(new BezierLine(new Point(firstSampleSweepPose), new Point(firstSampleSweptPose)))
                .setLinearHeadingInterpolation(firstSampleSweepPose.getHeading(), firstSampleSweptPose.getHeading())
                .build();
}

    public Command scoreSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> elevator.setPosition(elevator.getPositionPercentage() - 0.065)),
                new WaitCommand(500),
                new InstantCommand(() -> claw.toggleState())
        );
    }

    public Command pickupSample() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> intake.wristDown()),
                new InstantCommand(() -> intake.intake()),
                new InstantCommand(() -> extendo.setPosition(1.0)),
                new WaitUntilCommand(() -> intake.getColor() != IntakeSubsystem.Color.BLACK),
                new InstantCommand(() -> intake.stop())
        );
    }

    public Command spitSample() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> intake.outtake()),
                new WaitUntilCommand(() -> intake.getColor() == IntakeSubsystem.Color.BLACK),
                new InstantCommand(() -> extendo.setPosition(0.45)),
                new InstantCommand(() -> intake.wristHome())
        );
    }

    public Command extend() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> extendo.setIsHomed(false)),
                new InstantCommand(intake::intake),
                new InstantCommand(() -> extendo.setPosition(ExtendoSubsystem.ExtendoPosition.DEPLOYED)),
                new WaitCommand(500),
                new InstantCommand(intake::wristHome)
        );
    }

    public Command grabSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> claw.setState(ClawSubsystem.ClawState.CLOSED)),
                new WaitCommand(350),
                new InstantCommand(() -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_CHAMBER))
        );
    }

    public Command second() {
        return new SequentialCommandGroup(
                follower.followPath(pickup1, true),
                new WaitCommand(100),
                grabSpecimen(),
                follower.followPath(score2, true),
                scoreSpecimen()
        );
    }

    public Command third() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        follower.followPath(pickup2, true),
                        new ResetElevatorCommand(elevator)
                ),
                new WaitCommand(100),
                grabSpecimen(),
                follower.followPath(score3, true),
                scoreSpecimen()
        );
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> claw.setState(ClawSubsystem.ClawState.CLOSED)),
                new ParallelCommandGroup(
                        follower.followPath(score1, false),
                        new InstantCommand(() ->  elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_CHAMBER))
                ),
                scoreSpecimen(),
                new ParallelCommandGroup(
                        follower.followPath(goToFirstSample, true),
                        new SequentialCommandGroup(
                                new WaitCommand(200),
                                new ParallelCommandGroup(
                                        new ResetElevatorCommand(elevator),
                                        extend()
                                )
                        )
                ),
                pickupSample(),
                follower.followPath(moveFirstSample, true),
                spitSample(),
                new ParallelCommandGroup(
                        new InstantCommand(() -> intake.stop()),
                        new ResetExtendoCommand(extendo),
                        second()
                ),
                third()
                ).andThen(new SaveAutoHeadingCommand(() -> follower.getHeading()));
    }
}

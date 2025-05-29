package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.bylazar.ftcontrol.panels.plugins.html.primitives.P;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.BezierPoint;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.ResetElevatorCommand;
import org.firstinspires.ftc.teamcode.commands.ResetExtendoCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LEDSubsystem;
import org.opencv.core.Mat;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "SpecimenAuto")
@Config
public class SpecimenAuto extends StealthOpMode {
    private FollowerSubsystem follower;
    private ElevatorSubsystem elevator;
    private ExtendoSubsystem extendo;
    private IntakeSubsystem intake;
    private ClawSubsystem claw;
    private LEDSubsystem led;

    private final Pose startPose = new Pose(8.67, 64.72);

    private final Pose scoringPose1 = new Pose(36, 68);
    private final Pose scoringPose2 = new Pose(36, 66);
    private final Pose scoringPose3 = new Pose(36, 64);
    private final Pose scoringPose4 = new Pose(36, 62);
    private final Pose scoringPose5 = new Pose(36, 60);

    private final Pose pickupPose = new Pose(12, 36.5, Math.toRadians(180));
    private final Pose pickupPoseForward = new Pose(8.9, 36.5, Math.toRadians(180));

    private final Pose firstSampleSweepPose = new Pose(28, 40, Math.toRadians(137));
    private final Pose firstSampleSweptPose = new Pose(28, 40.001, Math.toRadians(40));

    private PathChain scoreInitial, score, pickupInital, pickup, pickupForward, moveOver, moveFirstSample, moveSecondSample, moveThirdSample;

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
        scoreInitial = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scoringPose1)))
                .addParametricCallback(0, () -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_CHAMBER))
                .build();

        moveFirstSample = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose1), new Point(firstSampleSweepPose)))
                .setLinearHeadingInterpolation(scoringPose1.getHeading(), firstSampleSweepPose.getHeading())

                .addParametricCallback(0.7, () -> intake.intake())
                .addParametricCallback(0.5, () -> extendo.setPosition(0.75))
                .addParametricCallback(0.8, () -> intake.wristTravel())

                .addPath(new BezierLine(new Point(firstSampleSweepPose), new Point(firstSampleSweptPose)))
                .setLinearHeadingInterpolation(firstSampleSweepPose.getHeading(), firstSampleSweptPose.getHeading())

                .addParametricCallback(1, () -> intake.outtake())

                .build();
    }

    public Command scoreSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> elevator.setPosition(elevator.getPositionPercentage() - 0.065)),
                new WaitCommand(400),
                new InstantCommand(() -> claw.toggleState())
        );
    }

//    public Command pickupSpecimen() {
//        return new SequentialCommandGroup(
//                new InstantCommand(() -> claw.setState(ClawSubsystem.ClawState.CLOSED)),
//                new WaitCommand(300),
//                new InstantCommand(() -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_CHAMBER))
//        );
//    }
//
//    public Command pickupPath() {
//        return new SequentialCommandGroup(
//                new ParallelCommandGroup(
//                        follower.followPath(pickup, true),
//                        new SequentialCommandGroup(
//                                new WaitCommand(500),
//                                new ResetElevatorCommand(elevator)
//                        )
//                ),
//                follower.followPath(pickupForward, true)
//        );
//    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> claw.setState(ClawSubsystem.ClawState.CLOSED)),
                follower.followPath(scoreInitial, true),
                scoreSpecimen(),
                new ParallelCommandGroup(
                        follower.followPath(moveFirstSample, true),
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new ResetElevatorCommand(elevator)
                        )
                )
        );
    }
}

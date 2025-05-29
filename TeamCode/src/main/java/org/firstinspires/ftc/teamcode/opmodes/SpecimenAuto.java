package org.firstinspires.ftc.teamcode.opmodes;

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
public class SpecimenAuto extends StealthOpMode {
    private FollowerSubsystem follower;
    private ElevatorSubsystem elevator;
    private ClawSubsystem claw;
    private LEDSubsystem led;

    public static final Pose startPose = new Pose(8.25, 65);
    public static final Pose scoringPose = new Pose(36, 65);
    public static final Pose moveOverPose = new Pose(36, 75);
    public static final Pose pickupPose = new Pose(12, 36.5, Math.toRadians(180));
    public static final Pose pickupPoseControl = new Pose(34.24, 35.88);
    public static final Pose pickupPoseForward = new Pose(8.9, 36.5, Math.toRadians(180));

    public static final Pose firstSamplePose = new Pose(65, 24);
    public static final Pose firstSampleControlPoint1 = new Pose(25.32, 17.82);
    public static final Pose firstSampleControlPoint2 = new Pose(63.32, 42.21);
    public static final Pose firstSamplePosePushed = new Pose(18, 24);

    public static final Pose secondSamplePose = new Pose(65, 16);
    public static final Pose secondSampleControlPoint = new Pose(68, 29);
    public static final Pose secondSamplePosePushed = new Pose(18, 16);

    public static final Pose thirdSamplePose = new Pose(65, 10);
    public static final Pose thirdSampleControlPoint = new Pose(68.48, 14.07);
    public static final Pose thirdSamplePosePushed = new Pose(18, 10);

    private PathChain scoreInitial, score, pickupInital, pickup, pickupForward, moveOver, moveFirstSample, moveSecondSample, moveThirdSample;

    @Override
    public void initialize() {
        follower = new FollowerSubsystem(hardwareMap);
        follower.setStartPose(startPose);

        elevator = new ElevatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        led = new LEDSubsystem(hardwareMap);

        schedule(
                new InstantCommand(() -> led.setMode(LEDSubsystem.LEDMode.AUTONOMOUS))
        );

        buildPaths();
    }

    public void buildPaths() {
        scoreInitial = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scoringPose)))
                .addParametricCallback(0, () -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_CHAMBER))
                .build();

        moveOver = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose), new Point(moveOverPose)))
                .setConstantHeadingInterpolation(scoringPose.getHeading())
                .build();

        moveFirstSample = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(moveOverPose), new Point(firstSampleControlPoint1), new Point(firstSampleControlPoint2), new Point(firstSamplePose)))
                .setConstantHeadingInterpolation(0)

                .addPath(new BezierLine(new Point(firstSamplePose), new Point(firstSamplePosePushed)))
                .setConstantHeadingInterpolation(0)
                .build();

        moveSecondSample = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(firstSamplePosePushed), new Point(secondSampleControlPoint), new Point(secondSamplePose)))
                .setConstantHeadingInterpolation(0)

                .addPath(new BezierLine(new Point(secondSamplePose), new Point(secondSamplePosePushed)))
                .setConstantHeadingInterpolation(0)
                .build();

        moveThirdSample = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(secondSamplePosePushed), new Point(thirdSampleControlPoint), new Point(thirdSamplePose)))
                .setConstantHeadingInterpolation(0)

                .addPath(new BezierLine(new Point(thirdSamplePose), new Point(thirdSamplePosePushed)))
                .setConstantHeadingInterpolation(0)
                .build();

        pickupInital = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(thirdSamplePosePushed), new Point(pickupPoseControl), new Point(pickupPose)))
                .setLinearHeadingInterpolation(thirdSamplePosePushed.getHeading(), pickupPose.getHeading())
                .build();

        score = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupPose), new Point(scoringPose)))
                .setLinearHeadingInterpolation(pickupPose.getHeading(), scoringPose.getHeading())
                .build();

        pickup = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose), new Point(pickupPose)))
                .setLinearHeadingInterpolation(scoringPose.getHeading(), pickupPose.getHeading())
                .build();

        pickupForward = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupPose), new Point(pickupPoseForward)))
                .setConstantHeadingInterpolation(pickupPose.getHeading())
                .build();
    }

    public Command scoreSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> elevator.setPosition(elevator.getPositionPercentage() - 0.065)),
                new WaitCommand(200),
                follower.followPath(moveOver, false),
                new InstantCommand(() -> claw.toggleState())
        );
    }

    public Command pickupSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> claw.setState(ClawSubsystem.ClawState.CLOSED)),
                new WaitCommand(300),
                new InstantCommand(() -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_CHAMBER))
        );
    }

    public Command pickupPath() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        follower.followPath(pickup, true),
                        new SequentialCommandGroup(
                                new WaitCommand(500),
                                new ResetElevatorCommand(elevator)
                        )
                ),
                follower.followPath(pickupForward, true)
        );
    }

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
                ),
                follower.followPath(moveSecondSample, true),
                follower.followPath(moveThirdSample, true),
                follower.followPath(pickupInital, true),
                follower.followPath(pickupForward, true),
                pickupSpecimen(),
                follower.followPath(score, true),
                scoreSpecimen(),
                pickupPath(),
                pickupSpecimen(),
                follower.followPath(score, true),
                scoreSpecimen(),
                pickupPath(),
                pickupSpecimen(),
                follower.followPath(score, true),
                scoreSpecimen(),
                pickupPath(),
                pickupSpecimen(),
                follower.followPath(score, true),
                scoreSpecimen()
        );
    }
}

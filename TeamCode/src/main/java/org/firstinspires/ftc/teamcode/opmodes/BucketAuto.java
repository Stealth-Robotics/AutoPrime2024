package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.pedropathing.pathgen.Point;


import org.firstinspires.ftc.teamcode.commands.DeployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.ResetElevatorCommand;
import org.firstinspires.ftc.teamcode.commands.RetractIntakeCommand;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "BucketAuto")
public class BucketAuto extends StealthOpMode {
    FollowerSubsystem follower;
    ExtendoSubsystem extendo;
    ElevatorSubsystem elevator;
    IntakeSubsystem intake;
    PanSubsystem pan;

    private final Pose startPose = new Pose(9.38,112.57, Math.toRadians(180));
    private final Pose scorePose = new Pose(12.6,131.3, Math.toRadians(135));
    private final Pose scorePoseControl = new Pose(33.53, 118.671);

    private final Pose grabBlock1Pose = new Pose(25,120, Math.toRadians(180));
    private final Pose grabBlock2Pose = new Pose(24,129, Math.toRadians(180));
    private final Pose grabBlock3Pose = new Pose(25,133.2, Math.toRadians(-164));

    private final Pose climbPose = new Pose(70,100, Math.toRadians(90));

    private PathChain startToBucket, bucketToBlock1, block1ToBucket, bucketToBlock2, block2ToBucket, bucketToBlock3, block3ToBucket, bucketToClimb;

    @Override
    public void initialize() {
        follower = new FollowerSubsystem(hardwareMap);
        extendo = new ExtendoSubsystem(hardwareMap);
        elevator = new ElevatorSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        pan = new PanSubsystem(hardwareMap);

        follower.setStartPose(startPose);

        schedule(
                new InstantCommand(() -> intake.wristHome()),
                new InstantCommand(() -> pan.home())
        );

        startToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose), new Point(scorePoseControl), new Point(scorePose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.5))
                .build();
        bucketToBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(grabBlock1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), grabBlock1Pose.getHeading())
                .build();
        block1ToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock1Pose), new Point(scorePose)))
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.5))
                .setLinearHeadingInterpolation(grabBlock1Pose.getHeading(),scorePose.getHeading())
                .build();
        bucketToBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(grabBlock2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),grabBlock2Pose.getHeading())
                .build();
        block2ToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock2Pose), new Point(scorePose)))
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.5))
                .setLinearHeadingInterpolation(grabBlock2Pose.getHeading(),scorePose.getHeading())
                .build();
        bucketToBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(grabBlock3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),grabBlock3Pose.getHeading())
                .build();
        block3ToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock3Pose), new Point(scorePose)))
                .addParametricCallback(0.8, () -> follower.setMaxPower(0.5))
                .setLinearHeadingInterpolation(grabBlock3Pose.getHeading(),scorePose.getHeading())
                .build();
        bucketToClimb = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(climbPose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), climbPose.getHeading())
                .build();
    }

    private Command scorePiece() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> pan.score()),
                new WaitCommand(700),
                new InstantCommand(() -> pan.home())
        );
    }

    private Command wobble() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> pan.score()),
                new WaitCommand(75),
                new InstantCommand(() -> pan.home()),
                new WaitCommand(75),
                new InstantCommand(() -> pan.score()),
                new WaitCommand(75),
                new InstantCommand(() -> pan.home())
        );
    }

    private Command grabPiece() {
        return new ParallelCommandGroup(
                new ResetElevatorCommand(elevator),
                new SequentialCommandGroup(
                        new DeployIntakeCommand(extendo, intake),
                        new InstantCommand(() -> intake.wristDown()),
                        new InstantCommand(() -> extendo.setPosition(1.0)),
                        new WaitUntilCommand(() -> intake.getColor() != IntakeSubsystem.Color.BLACK),
                        new RetractIntakeCommand(extendo, intake, elevator, pan, true),
                        new InstantCommand(() -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_BUCKET)),
                        new WaitCommand(400)
                )
        );
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(() -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_BUCKET)),
                        wobble(),
                        new SequentialCommandGroup(
                                new WaitCommand(400),
                                follower.followPath(startToBucket,true)
                        )
                ),
                scorePiece(),
                follower.followPath(bucketToBlock1,true),
                grabPiece(),
                new WaitCommand(100),
                new ParallelCommandGroup(
                        follower.followPath(block1ToBucket,true),
                        wobble()
                ),
                scorePiece(),
                follower.followPath(bucketToBlock2,true),
                grabPiece(),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        follower.followPath(block2ToBucket,true),
                        wobble()
                ),
                scorePiece(),
                follower.followPath(bucketToBlock3, true),
                grabPiece(),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        follower.followPath(block3ToBucket, true),
                        wobble()
                ),
                scorePiece(),
                follower.followPath(bucketToClimb,true)
        );
    }
}

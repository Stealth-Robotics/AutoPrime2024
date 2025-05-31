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

    private final Pose startPose = new Pose(8.6775,112.57, Math.toRadians(180));
    private final Pose scorePose = new Pose(16,128, Math.toRadians(135));
    private final Pose scorePoseControl = new Pose(26.73, 117.732);

    private final Pose grabBlock1Pose = new Pose(19.7,127.114, Math.toRadians(160));
    private final Pose grabBlock2Pose = new Pose(19.7,127.114, Math.toRadians(-170));
    private final Pose grabBlock3Pose = new Pose(19.7,127.114, Math.toRadians(-155));

    private final Pose climbPose = new Pose(62.14,107.64, Math.toRadians(-90));
    private final Pose climbPoseControl = new Pose(53.472,130.397);

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
                .build();
        bucketToBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(grabBlock1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), grabBlock1Pose.getHeading())
                .build();
        block1ToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(grabBlock1Pose.getHeading(),scorePose.getHeading())
                .build();
        bucketToBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(grabBlock2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),grabBlock2Pose.getHeading())
                .build();
        block2ToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock2Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(grabBlock2Pose.getHeading(),scorePose.getHeading())
                .build();
        bucketToBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(grabBlock3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),grabBlock3Pose.getHeading())
                .build();
        block3ToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(grabBlock3Pose.getHeading(),scorePose.getHeading())
                .build();
        bucketToClimb = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(climbPoseControl), new Point(climbPose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), climbPose.getHeading())
                .addParametricCallback(0.5, () -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.LOW_RUNG))
                .addParametricCallback(0.98, () -> elevator.setPosition(elevator.getPositionPercentage() - 0.1))
                .build();
    }

    private Command scorePiece() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> pan.score()),
                new WaitCommand(100),
                new InstantCommand(() -> pan.home()),
                new WaitCommand(100),
                new InstantCommand(() -> pan.score()),
                new WaitCommand(700)
        );
    }

    private Command grabPiece() {
        return new ParallelCommandGroup(
                new ResetElevatorCommand(elevator),
                new SequentialCommandGroup(
                        new InstantCommand(() -> intake.wristDown()),
                        new InstantCommand(() -> extendo.setPosition(1.0)),
                        new WaitUntilCommand(() -> intake.getColor() != IntakeSubsystem.Color.BLACK),
                        new RetractIntakeCommand(extendo, intake, elevator, pan, true),
                        new InstantCommand(() -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_BUCKET))
                )
        );
    }

    private Command extend() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> extendo.setIsHomed(false)),
                new InstantCommand(intake::intake),
                new InstantCommand(() -> extendo.setPosition(ExtendoSubsystem.ExtendoPosition.DEPLOYED)),
                new WaitCommand(500),
                new InstantCommand(intake::wristTravel)
        );
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new ParallelCommandGroup(
                        new InstantCommand(() -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_BUCKET)),
                        new SequentialCommandGroup(
                                new WaitCommand(400),
                                follower.followPath(startToBucket,false)
                        ),
                        extend()
                ),
                scorePiece(),
                follower.followPath(bucketToBlock1,false),
                grabPiece(),
                new WaitCommand(100),
                new ParallelCommandGroup(
                        follower.followPath(block1ToBucket,false),
                        extend()
                ),
                scorePiece(),
                follower.followPath(bucketToBlock2,false),
                grabPiece(),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        follower.followPath(block2ToBucket,false),
                        extend()
                ),
                scorePiece(),
                follower.followPath(bucketToBlock3, false),
                grabPiece(),
                new WaitCommand(500),
                new ParallelCommandGroup(
                        follower.followPath(block3ToBucket, false),
                        extend()
                ),
                scorePiece(),
                follower.followPath(bucketToClimb,false)
                //Raw paths for testing
//                follower.followPath(startToBucket,true),
//                follower.followPath(bucketToBlock1,true),
//                follower.followPath(block1ToBucket,true),
//                follower.followPath(bucketToBlock2,true),
//                follower.followPath(block2ToBucket,true),
//                follower.followPath(bucketToBlock3, true),
//                follower.followPath(block3ToBucket, true),
//                follower.followPath(bucketToClimb,true)
        );
    }
}

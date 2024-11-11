package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "bucketCycleAuto")
public class bucketCycleAuto extends StealthOpMode {
    Follower follower;
    DriveSubsystem driveSubsystem;
    ReacherSubsystem reacherSubsystem;
    LifterSubsystem lifterSubsystem;
    FlipperSubsystem flipperSubsystem;
    IntakeSubsystem intakeSubsystem;
    LifterPanSubsystem panSubsystem;
    //static Pose startPose = new Pose(9.757,84.983,0);
    static Pose startPose = new Pose(9.18,107.78,Math.toRadians(90));
    static Pose scorePose = new Pose(15.6,128.57, Math.toRadians(135));/*
    static Pose grabBlock1Pose = new Pose(45.28,105.25, Math.toRadians(270));
    static Pose grabBlock2Pose = new Pose(45.28,113.61,Math.toRadians(270));*/
    static Pose grabBlock3Pose = new Pose(45.28,123.2, Math.toRadians(270));
    static Pose grabBlock1Pose = new Pose(27.9,121.13,Math.toRadians(180));
    static Pose grabBlock2Pose = new Pose(27.9,130.83,Math.toRadians(180));
    static PathChain startToBucket, bucketToBlock1, block1ToBucket, bucketToBlock2, block2ToBucket, bucketToBlock3, block3ToBucket;

    @Override
    public void initialize(){
        follower = new Follower(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        reacherSubsystem = new ReacherSubsystem(hardwareMap, telemetry);
        lifterSubsystem = new LifterSubsystem(hardwareMap,telemetry);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        startToBucket = follower.pathBuilder()
                //.addPath(new BezierCurve(new Point(startPose),new Point(41.4,110.14,Point.CARTESIAN), new Point(scorePose)))
                .addPath(new BezierCurve(new Point(startPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(startPose.getHeading(),scorePose.getHeading())
                .build();
        bucketToBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(grabBlock1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),grabBlock1Pose.getHeading())
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
    }
    private Command scorePiece(){
        return new SequentialCommandGroup(
                new InstantCommand(()->lifterSubsystem.moveArm(1)),
                new WaitCommand(1500),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(1000),
                new InstantCommand(()->lifterSubsystem.moveArm(0))
        );
    }
    private Command grabPiece(){
        return new SequentialCommandGroup(
                new deployIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,true, 0.9),
                new WaitCommand(1000),
                new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,lifterSubsystem,panSubsystem)
        );
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(startPose)),
                driveSubsystem.FollowPath(startToBucket,true),
                scorePiece(),
                driveSubsystem.FollowPath(bucketToBlock1,true),
                grabPiece(),
                driveSubsystem.FollowPath(block1ToBucket,true),
                scorePiece(),
                driveSubsystem.FollowPath(bucketToBlock2,true),
                grabPiece(),
                driveSubsystem.FollowPath(block2ToBucket,true),
                scorePiece(),
                driveSubsystem.FollowPath(bucketToBlock3,true),
                grabPiece(),
                driveSubsystem.FollowPath(block3ToBucket,true),
                scorePiece()
        );
    }
}

package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.paths.BluePaths;
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
import org.stealthrobotics.library.opmodes.StealthOpMode;

//This code is not used
//@Autonomous (name = "bucketAuto")
public class bucketAuto extends StealthOpMode{
    DriveSubsystem driveSubsystem;
    IntakeSubsystem intakeSubsystem;
    FlipperSubsystem flipperSubsystem;
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    Follower follower;
    BluePaths bluePaths;

    static Pose bucketStartingPose = new Pose(9.787, 84.983, 0);
    static Pose bucketLiftPose = new Pose(24.43, 125.93, Math.toRadians(135));
    static Pose bucketScorePose = new Pose(22.5,128, Math.toRadians(135));
    static Pose parkLeft = new Pose(61.8,99.78,Math.toRadians(270));
    static Pose parkRight = new Pose(83.06,98.65, Math.toRadians(270));

    static PathChain driveToBucket, parkLeftFromBucket, parkRightFromBucket, inchToBucket, inchFromBucket;
    static PathChain t1,t2,t3;

    @Override
    public void initialize(){
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        follower = new Follower(hardwareMap);
        //bluePaths = new BluePaths(follower);
        //bluePaths.buildPaths();


        driveToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketStartingPose), new Point(44.36, 95.48, Point.CARTESIAN), new Point(bucketLiftPose)))
                .setLinearHeadingInterpolation(bucketStartingPose.getHeading(), bucketLiftPose.getHeading())
                .build();
        parkLeftFromBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketLiftPose), new Point(20.41,98.08,Point.CARTESIAN), new Point(65.2,12.29,Point.CARTESIAN), new Point(parkLeft)))
                .setLinearHeadingInterpolation(bucketLiftPose.getHeading(), parkLeft.getHeading())
                .build();
        parkRightFromBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketLiftPose), new Point(86.61,129.54,Point.CARTESIAN), new Point(parkRight)))
                .setLinearHeadingInterpolation(bucketLiftPose.getHeading(), parkRight.getHeading())
                .build();
        inchToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketLiftPose), new Point(bucketScorePose)))
                .setConstantHeadingInterpolation(bucketScorePose.getHeading())
                .build();
        inchFromBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketScorePose), new Point(bucketLiftPose)))
                .setConstantHeadingInterpolation(bucketLiftPose.getHeading())
                .build();
        t1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(24,120,Point.CARTESIAN),new Point(24,24,Point.CARTESIAN)))
                .setConstantHeadingInterpolation(0)
                .build();
        t2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(24,24,Point.CARTESIAN),new Point(120,24,Point.CARTESIAN)))
                .setConstantHeadingInterpolation(0)
                .build();
        t2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(120,24,Point.CARTESIAN), new Point(144,144,Point.CARTESIAN), new Point(24,120,Point.CARTESIAN)))
                .setConstantHeadingInterpolation(0)
                .build();
        register(driveSubsystem, lifterSubsystem);
    }
    @Override
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(new Pose(24,120,0))),
                driveSubsystem.FollowPath(t1,true),
                new WaitCommand(5000),
                driveSubsystem.FollowPath(t2,true),
                new WaitCommand(5000),
                driveSubsystem.FollowPath(t3,true)
                /*new InstantCommand(()->driveSubsystem.setPose(bucketStartingPose)),
                driveSubsystem.FollowPath(driveToBucket, true),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.35)),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.in)),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.25)),
                new InstantCommand(()-> intakeSubsystem.setPower(1)),
                new WaitCommand(3000),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.55)),
                new InstantCommand(()-> intakeSubsystem.setPower(0)),
                new InstantCommand(()-> lifterSubsystem.moveArm(1)),
                new WaitCommand(2000),
                driveSubsystem.FollowPath(inchToBucket, true),
                new WaitCommand(1000),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(3000),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
                driveSubsystem.FollowPath(inchFromBucket, true)
                //new WaitCommand(3000),
                //new InstantCommand(()-> panSubsystem.setPos(panSubsystem.in)),
                //new InstantCommand(()-> lifterSubsystem.moveArm(0))
                //driveSubsystem.FollowPath(parkLeftFromBucket, true)*/
        );
    }
}
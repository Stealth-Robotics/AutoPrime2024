package org.firstinspires.ftc.teamcode.opModes;

import com.acmerobotics.roadrunner.drive.Drive;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

public class shuttleCycleAutoTuned extends StealthOpMode {
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    ClawSubsystem clawSubsystem;
    DriveSubsystem driveSubsystem;
    Follower follower;
    static Pose startPose = new Pose(8.25,66,0);
    static Pose score1Pose = new Pose(38.123,66,0);
    static Pose behindBlock1Pose1 = new Pose(59.264,32.058,Math.toRadians(180));
    static Pose behindBlock1Pose2 = new Pose(59.264,26,Math.toRadians(180));
    static Pose depositBlock1Pose = new Pose(9.704,26,Math.toRadians(180));
    static Pose score2Pose = new Pose(37.603,62.383,0);
    static Pose behindBlock2Pose1 = new Pose(59.09, 21.487, Math.toRadians(180));
    static Pose behindBlock2Pose2 = new Pose(59.09,16,Math.toRadians(180));
    static Pose depositBlock2Pose = new Pose(9.704,16,Math.toRadians(180));
    static Pose score3Pose = new Pose(37.43,57.704,0);
    static  Pose behindBlock3Pose = new Pose(58.744,8,Math.toRadians(180));
    static Pose depositBlock3Pose = new Pose(9.877,8,Math.toRadians(180));
    static PathChain driveToScore1,driveBehindBlock1,pushBlock1,driveToScore2,driveBehindBlock2,pushBlock2,driveToScore3,driveBehindBlock3, pushBlock3;
    @Override
    public void initialize(){
        follower = new Follower(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        lifterSubsystem = new LifterSubsystem(hardwareMap,telemetry);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        clawSubsystem = new ClawSubsystem(hardwareMap);
        driveToScore1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(score1Pose)))
                .setConstantHeadingInterpolation(startPose.getHeading())
                .build();
        driveBehindBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score1Pose), new Point(23.394,35.697,Point.CARTESIAN), new Point(behindBlock1Pose1)))
                .setLinearHeadingInterpolation(score1Pose.getHeading(),behindBlock1Pose1.getHeading())
                .addPath(new BezierLine(new Point(behindBlock1Pose1), new Point(behindBlock1Pose2)))
                .setConstantHeadingInterpolation(behindBlock1Pose1.getHeading())
                .build();
        pushBlock1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(behindBlock1Pose2), new Point(depositBlock1Pose)))
                .setConstantHeadingInterpolation(behindBlock1Pose2.getHeading())
                .build();
        driveToScore2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(depositBlock1Pose), new Point(score2Pose)))
                .setLinearHeadingInterpolation(depositBlock1Pose.getHeading(), score2Pose.getHeading())
                .build();
        driveBehindBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score2Pose), new Point(27.726,22.874,Point.CARTESIAN), new Point(behindBlock2Pose1)))
                .setLinearHeadingInterpolation(score2Pose.getHeading(),behindBlock2Pose1.getHeading())
                .addPath(new BezierLine(new Point(behindBlock2Pose1), new Point(behindBlock2Pose2)))
                .setConstantHeadingInterpolation(behindBlock2Pose1.getHeading())
                .build();
        pushBlock2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(behindBlock2Pose2), new Point(depositBlock2Pose)))
                .setConstantHeadingInterpolation(behindBlock2Pose2.getHeading())
                .build();
        driveToScore3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(depositBlock2Pose), new Point(score3Pose)))
                .setLinearHeadingInterpolation(depositBlock2Pose.getHeading(), score3Pose.getHeading())
                .build();
        driveBehindBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score3Pose), new Point(30.325,32.751,Point.CARTESIAN), new Point(behindBlock3Pose)))
                .setLinearHeadingInterpolation(score3Pose.getHeading(),behindBlock3Pose.getHeading())
                .build();
        pushBlock3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(behindBlock3Pose), new Point(depositBlock3Pose)))
                .setConstantHeadingInterpolation(behindBlock3Pose.getHeading())
                .build();

    }
}

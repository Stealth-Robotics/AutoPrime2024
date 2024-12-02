package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

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

@Autonomous(name = "shuttleCycleAuto")
public class shuttleCycleAutoTuned extends StealthOpMode {
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    ClawSubsystem clawSubsystem;
    DriveSubsystem driveSubsystem;
    Follower follower;
    static Pose startPose = new Pose(8.25,65,0);
    static Pose score1Pose = new Pose(38.123,65,0);
    static Pose behindBlock1Pose1 = new Pose(59.264,32.058,Math.toRadians(180));
    static Pose behindBlock1Pose2 = new Pose(59.264,26,Math.toRadians(180));
    static Pose depositBlock1Pose = new Pose(9.704,26,Math.toRadians(180));
    static Pose score2Pose = new Pose(37.603,62.383,0);
    static Pose behindBlock2Pose1 = new Pose(59.09, 21.487, Math.toRadians(180));
    static Pose behindBlock2Pose2 = new Pose(59.09,16,Math.toRadians(180));
    static Pose depositBlock2Pose = new Pose(9.704,16,Math.toRadians(180));
    static Pose score3Pose = new Pose(37.43,57.704,0);
    static Pose behindBlock3Pose = new Pose(58.744,8,Math.toRadians(180));
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
    private Command score(){
        return new SequentialCommandGroup(
                new InstantCommand(()->lifterSubsystem.moveArm(0.3)),
                new WaitCommand(300),
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawOpen)),
                new WaitCommand(300),
                new InstantCommand(()->lifterSubsystem.moveArm(0))
        );
    }
    private Command grab(){
        return new SequentialCommandGroup(
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawClosed)),
                new WaitCommand(300),
                new InstantCommand(()->lifterSubsystem.moveArm(0.42))
        );
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(startPose)),
                new InstantCommand(()->lifterSubsystem.setUsePID(true)),
                new InstantCommand(()->lifterSubsystem.moveArm(0.42)),
                driveSubsystem.FollowPath(driveToScore1, true),
                //score(),
                driveSubsystem.FollowPath(driveBehindBlock1, false),
                driveSubsystem.FollowPath(pushBlock1, true),
                grab(),
                driveSubsystem.FollowPath(driveToScore2, true),
                //score(),
                driveSubsystem.FollowPath(driveBehindBlock2, false),
                driveSubsystem.FollowPath(pushBlock2, true),
                grab(),
                driveSubsystem.FollowPath(driveToScore3, true),
                //score(),
                driveSubsystem.FollowPath(driveBehindBlock3, false),
                driveSubsystem.FollowPath(pushBlock3, true),
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawClosed))

        );
    }
}

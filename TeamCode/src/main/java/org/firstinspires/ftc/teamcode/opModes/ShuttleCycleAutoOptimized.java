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
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;
@Autonomous (name = "5clip")
public class ShuttleCycleAutoOptimized extends StealthOpMode {
    Follower follower;
    DriveSubsystem driveSubsystem;
    LifterSubsystem lifterSubsystem;
    ClawSubsystem clawSubsystem;
    static Pose startPose = new Pose(8.25,65,0);
    static Pose score1Pose = new Pose(35.25,65,0);
    static Pose behindBlock1Pose = new Pose(61,26,Math.toRadians(180));
    static Pose depositBlock1Pose = new Pose(20,26,Math.toRadians(180));
    static Pose behindBlock2Pose = new Pose(61,16,Math.toRadians(180));
    static Pose depositBlock2Pose = new Pose(20,16,Math.toRadians(180));
    static Pose behindBlock3Pose = new Pose(61,8,Math.toRadians(180));
    static Pose depositBlock3Pose = new Pose(12,8,Math.toRadians(180));
    static Pose score2Pose = new Pose(35.5,62,0);
    static Pose score3Pose = new Pose(35.5, 61,0);
    static Pose score4Pose = new Pose(35.5,60,0);
    static Pose score5Pose = new Pose(35.5,63,0);
    static Pose optimalPickupPose = new Pose(12,40,Math.toRadians(180));
    static PathChain driveToScore1, driveToStop1, shuttle1, driveToStop2, shuttle2;
    static PathChain driveToStop3, shuttle3, driveToScore2, driveHome2, driveToScore3;
    static PathChain driveHome3, driveToScore4, driveHome4, driveToScore5, shuttleBlocks;
    @Override
    public void initialize(){
        follower = new Follower(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        clawSubsystem = new ClawSubsystem(hardwareMap);
        driveToScore1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(score1Pose)))
                .setConstantHeadingInterpolation(startPose.getHeading())
                .build();
        driveToStop1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score1Pose), new Point(19.785,17.745,1), new Point(57.926,48.952,1), new Point(behindBlock1Pose)))
                .setLinearHeadingInterpolation(score1Pose.getHeading(),behindBlock1Pose.getHeading())
                .build();
        shuttle1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(behindBlock1Pose), new Point(depositBlock1Pose)))
                .setConstantHeadingInterpolation(behindBlock1Pose.getHeading())
                .build();
        driveToStop2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(depositBlock1Pose), new Point(64.045,26.516,1), new Point(behindBlock2Pose)))
                .setConstantHeadingInterpolation(depositBlock1Pose.getHeading())
                .build();
        shuttle2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(behindBlock2Pose), new Point(depositBlock2Pose)))
                .setConstantHeadingInterpolation(behindBlock2Pose.getHeading())
                .build();
        driveToStop3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(depositBlock2Pose), new Point(61.190,15.501,1), new Point(behindBlock3Pose)))
                .setConstantHeadingInterpolation(depositBlock2Pose.getHeading())
                .build();
        shuttle3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(behindBlock3Pose), new Point(depositBlock3Pose)))
                .setConstantHeadingInterpolation(behindBlock3Pose.getHeading())
                .build();
        shuttleBlocks = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score1Pose), new Point(19.785,17.745,1), new Point(57.926,48.952,1), new Point(behindBlock1Pose)))
                .setLinearHeadingInterpolation(score1Pose.getHeading(),behindBlock1Pose.getHeading())
                .addPath(new BezierLine(new Point(behindBlock1Pose), new Point(depositBlock1Pose)))
                .setConstantHeadingInterpolation(behindBlock1Pose.getHeading())
                .addPath(new BezierCurve(new Point(depositBlock1Pose), new Point(64.045,26.516,1), new Point(behindBlock2Pose)))
                .setConstantHeadingInterpolation(depositBlock1Pose.getHeading())
                .addPath(new BezierLine(new Point(behindBlock2Pose), new Point(depositBlock2Pose)))
                .setConstantHeadingInterpolation(behindBlock2Pose.getHeading())
                .addPath(new BezierCurve(new Point(depositBlock2Pose), new Point(61.190,15.501,1), new Point(behindBlock3Pose)))
                .setConstantHeadingInterpolation(depositBlock2Pose.getHeading())
                .addPath(new BezierLine(new Point(behindBlock3Pose), new Point(depositBlock3Pose)))
                .setConstantHeadingInterpolation(behindBlock3Pose.getHeading())
                .build();
        driveToScore2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(depositBlock3Pose), new Point(score2Pose)))
                .setLinearHeadingInterpolation(depositBlock3Pose.getHeading(), score2Pose.getHeading())
                .build();
        driveHome2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score2Pose), new Point(33.450,39.365,1), new Point(optimalPickupPose)))
                .setLinearHeadingInterpolation(score2Pose.getHeading(),optimalPickupPose.getHeading())
                .build();
        driveToScore3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(optimalPickupPose), new Point(14.890,63.025,1), new Point(score3Pose)))
                .setLinearHeadingInterpolation(optimalPickupPose.getHeading(),score3Pose.getHeading())
                .build();
        driveHome3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score3Pose), new Point(33.042,41.405,1), new Point(optimalPickupPose)))
                .setLinearHeadingInterpolation(score3Pose.getHeading(), optimalPickupPose.getHeading())
                .build();
        driveToScore4 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(optimalPickupPose), new Point(14.278,59.762,1), new Point(score4Pose)))
                .setLinearHeadingInterpolation(optimalPickupPose.getHeading(),score4Pose.getHeading())
                .build();
        driveHome4 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(score4Pose), new Point(35.286,42.221,1), new Point(optimalPickupPose)))
                .setLinearHeadingInterpolation(score4Pose.getHeading(), optimalPickupPose.getHeading())
                .build();
        driveToScore5 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(optimalPickupPose), new Point(17.745,63.637,1), new Point(score5Pose)))
                .setLinearHeadingInterpolation(optimalPickupPose.getHeading(),score5Pose.getHeading())
                .build();
    }
    private Command grab(){
        return new SequentialCommandGroup(
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawClosed)),
                new WaitCommand(300),
                new InstantCommand(()->lifterSubsystem.moveArm(0.39))
        );
    }
    private Command score(){
        return new SequentialCommandGroup(
                new InstantCommand(()->lifterSubsystem.moveArm(0.26)),
                new WaitCommand(300),
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawOpen)),
                new WaitCommand(300),
                new InstantCommand(()->lifterSubsystem.moveArm(0))
        );
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(startPose)),
                new InstantCommand(()->lifterSubsystem.setUsePID(true)),
                new InstantCommand(()->lifterSubsystem.moveArm(0.39)),
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawClosed)),
                driveSubsystem.FollowPath(driveToScore1,true),
                score(),
                driveSubsystem.FollowPath(shuttleBlocks, false),
                grab(),
                driveSubsystem.FollowPath(driveToScore2, true),
                score(),
                driveSubsystem.FollowPath(driveHome2, false),
                grab(),
                driveSubsystem.FollowPath(driveToScore3, true),
                score(),
                driveSubsystem.FollowPath(driveHome3, false),
                grab(),
                driveSubsystem.FollowPath(driveToScore4, true),
                score(),
                driveSubsystem.FollowPath(driveHome4, false),
                grab(),
                driveSubsystem.FollowPath(driveToScore5, true),
                score()

        );
    }
}

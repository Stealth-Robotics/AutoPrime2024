package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

//@Autonomous(name = "shuttleCycle")
public class shuttleCycleAuto extends StealthOpMode {
    Follower follower;
    DriveSubsystem driveSubsystem;
    static PathChain startToShuttleBlock1, shuttleBlock1, hangPreset, hangToShuttleBlock2, shuttleBlock2, hangBlock1, hangToShuttleBlock3, shuttleBlock3, hangBlock2, hangToBlock3, hangBlock3;
    static PathChain testPath;
    static Pose startPoint = new Pose(10.91,62.72,0);
    static Pose behindBlock1 = new Pose(63.42,24.95,0);
    static Pose behindBlock2 = new Pose(64.64,15.60,0);
    static Pose behindBlock3 = new Pose(65.32, 9.70, 0);
    static Pose grabPreset = new Pose(12.13,25.47,0);
    static Pose grabBlock1 = new Pose(12.30, 15.25, 0);
    static Pose grabBlock2 = new Pose(12.30, 9.70, 0);
    static Pose grabBlock3 = new Pose(10.22, 9.36, Math.toRadians(90));
    static Pose scorePreset = new Pose(34.83,74.69,Math.toRadians(180));
    static Pose scoreBlock1 = new Pose(34.83,66.54, Math.toRadians(180));
    static Pose scoreBlock2 = new Pose(34.66,59.44, Math.toRadians(180));
    static Pose scoreBlock3 = new Pose(34.48,69.49,Math.toRadians(180));
    @Override
    public void initialize(){
        follower = new Follower(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        startToShuttleBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPoint), new Point(10.74, 23.39, Point.CARTESIAN), new Point(65.85, 44.36, Point.CARTESIAN), new Point(behindBlock1)))
                .setConstantHeadingInterpolation(startPoint.getHeading())
                .build();
        shuttleBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(behindBlock1), new Point(grabPreset)))
                .setConstantHeadingInterpolation(behindBlock1.getHeading())
                .build();
        hangPreset = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabPreset),new Point(scorePreset)))
                .setLinearHeadingInterpolation(grabPreset.getHeading(),scorePreset.getHeading())
                .build();
        hangToShuttleBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePreset), new Point(22.18,24.43,Point.CARTESIAN), new Point(65.50,35.70, Point.CARTESIAN), new Point(behindBlock2)))
                .setLinearHeadingInterpolation(scorePreset.getHeading(),behindBlock2.getHeading())
                .build();
        shuttleBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(behindBlock2), new Point(grabBlock1)))
                .setConstantHeadingInterpolation(behindBlock2.getHeading())
                .build();
        hangBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock1), new Point(scoreBlock1)))
                .setLinearHeadingInterpolation(grabBlock1.getHeading(),scoreBlock1.getHeading())
                .build();
        hangToShuttleBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scoreBlock1), new Point(27.38,22.87,Point.CARTESIAN), new Point(66.02,18.54,Point.CARTESIAN), new Point(behindBlock3)))
                .setLinearHeadingInterpolation(scoreBlock1.getHeading(), behindBlock3.getHeading())
                .build();
        shuttleBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(behindBlock3), new Point(grabBlock2)))
                .setConstantHeadingInterpolation(behindBlock3.getHeading())
                .build();
        hangBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock2), new Point(scoreBlock2)))
                .setLinearHeadingInterpolation(grabBlock2.getHeading(),scoreBlock2.getHeading())
                .build();
        hangToBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scoreBlock2), new Point(grabBlock3)))
                .setLinearHeadingInterpolation(scoreBlock2.getHeading(), grabBlock3.getHeading())
                .build();
        hangBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock3), new Point(scoreBlock3)))
                .setLinearHeadingInterpolation(grabBlock3.getHeading(), scoreBlock3.getHeading())
                .build();
        testPath = follower.pathBuilder()
                        .addPath(new BezierCurve(new Point(0,0,Point.CARTESIAN), new Point(0,22.5,Point.CARTESIAN)))
                                .setConstantHeadingInterpolation(0)
        .build();
        register(driveSubsystem);
    }
    @Override
    public void whileWaitingToStart(){
        CommandScheduler.getInstance().run();
    }
    //startToShuttleBlock1, shuttleBlock1, hangPreset, hangToShuttleBlock2, shuttleBlock2,
    // hangBlock1, hangToShuttleBlock3, shuttleBlock3, hangBlock2, hangToBlock3, hangBlock3
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
            new InstantCommand(()->driveSubsystem.setPose(behindBlock1)),
            driveSubsystem.FollowPath(startToShuttleBlock1, true),
            new WaitCommand(1000),
            driveSubsystem.FollowPath(shuttleBlock1, true),
            new WaitCommand(1000),
            driveSubsystem.FollowPath(hangPreset, true),
            driveSubsystem.FollowPath(hangToShuttleBlock2, true),
            driveSubsystem.FollowPath(shuttleBlock2, true),
            driveSubsystem.FollowPath(hangBlock1, true),
            driveSubsystem.FollowPath(hangToShuttleBlock3, true),
            driveSubsystem.FollowPath(shuttleBlock3, true),
            driveSubsystem.FollowPath(hangBlock2, true),
            driveSubsystem.FollowPath(hangToBlock3, true),
            driveSubsystem.FollowPath(hangBlock3, true)
                /*new InstantCommand(()->driveSubsystem.setPose(new Pose(0,0,0))),
                driveSubsystem.FollowPath(testPath, true)*/
        );
    }
}

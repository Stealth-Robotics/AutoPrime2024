package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;
@Autonomous(name = "shuttleCycleAuto")
public class shuttleCycleAuto2 extends StealthOpMode {
    Follower follower;
    DriveSubsystem driveSubsystem;
    LifterSubsystem lifterSubsystem;
    ClawSubsystem clawSubsystem;
    static Pose startPose = new Pose(8.16,44.465,Math.toRadians(180));
    static Pose behindBlock1First = new Pose(52,32.635,Math.toRadians(180));
    static Pose behindBlock1 = new Pose(52,27,Math.toRadians(180));
    static Pose depositBlock1 = new Pose(12,27,Math.toRadians(180));
    static Pose scorePreset = new Pose(23,72,0);
    static Pose behindBlock2First = new Pose(20,37.53, Math.toRadians(180));
    static Pose behindBlock2Second = new Pose(47,20.805,Math.toRadians(180));
    static Pose behindBlock2 = new Pose(47,13.666,Math.toRadians(180));
    static Pose depositBlock2 = new Pose(8.16,13.666,Math.toRadians(180));
    static Pose scoreBlock1 = new Pose(20,70,0);
    static Pose behindBlock3First = new Pose(20,28.555, Math.toRadians(180));
    static Pose behindBlock3 = new Pose(62,9.38,Math.toRadians(180));
    static Pose depositBlock3 = new Pose(8.16,9.38,Math.toRadians(180));
    static Pose scoreBlock2 = new Pose(36.31,68,0);
    static Pose park = new Pose(5,22.43,0);
    static PathChain alignBehindBlock1, pushBlock1, score1, alignBehindBlock2, pushBlock2, score2, alignBehindBlock3, pushblock3, score3, driveToPark;
    @Override
    public void initialize(){
        follower = new Follower(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap,telemetry);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        clawSubsystem = new ClawSubsystem(hardwareMap);
        alignBehindBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose), new Point(behindBlock1First)))
                .setLinearHeadingInterpolation(startPose.getHeading(), behindBlock1First.getHeading())
                .addPath(new BezierCurve(new Point(behindBlock1First), new Point(behindBlock1)))
                .setLinearHeadingInterpolation(behindBlock1First.getHeading(),behindBlock1.getHeading())
                .build();
        pushBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(behindBlock1), new Point(depositBlock1)))
                .setConstantHeadingInterpolation(behindBlock1.getHeading())
                .build();
        score1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(depositBlock1), new Point(scorePreset)))
                .setLinearHeadingInterpolation(depositBlock1.getHeading(), scorePreset.getHeading())
                .build();
        alignBehindBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scoreBlock1), new Point(behindBlock2First)))
                .setLinearHeadingInterpolation(scoreBlock1.getHeading(), behindBlock2First.getHeading())
                .addPath(new BezierCurve(new Point(behindBlock2First), new Point(behindBlock2Second)))
                .setLinearHeadingInterpolation(behindBlock2First.getHeading(), behindBlock2Second.getHeading())
                .addPath(new BezierCurve(new Point(behindBlock2Second), new Point(behindBlock2)))
                .setLinearHeadingInterpolation(scoreBlock1.getHeading(),behindBlock2.getHeading())
                .build();
        pushBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(behindBlock2), new Point(depositBlock2)))
                .setConstantHeadingInterpolation(behindBlock2.getHeading())
                .build();
        score2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(depositBlock2), new Point(scoreBlock1)))
                .setLinearHeadingInterpolation(depositBlock2.getHeading(), scoreBlock1.getHeading())
                .build();
        alignBehindBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scoreBlock1), new Point(behindBlock3First)))
                .setLinearHeadingInterpolation(scoreBlock1.getHeading(), behindBlock3First.getHeading())
                .addPath(new BezierCurve(new Point(behindBlock3First), new Point(behindBlock3)))
                .setLinearHeadingInterpolation(scoreBlock1.getHeading(), behindBlock3.getHeading())
                .build();
        pushblock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(behindBlock3), new Point(depositBlock3)))
                .setConstantHeadingInterpolation(behindBlock3.getHeading())
                .build();
        score3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(depositBlock3), new Point(scoreBlock2)))
                .setLinearHeadingInterpolation(depositBlock3.getHeading(), scoreBlock2.getHeading())
                .build();
        driveToPark = follower.pathBuilder()
                //.addPath(new BezierCurve(new Point(scoreBlock2), new Point(park)))
                //.setLinearHeadingInterpolation(scoreBlock2.getHeading(), park.getHeading())
                .addPath(new BezierCurve(new Point(scoreBlock1), new Point(park)))
                .setLinearHeadingInterpolation(scoreBlock1.getHeading(), park.getHeading())
                .build();
    }
    private Command grabBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawClosed)),
                new WaitCommand(200),
                new InstantCommand(()->lifterSubsystem.moveArm(0.45))
        );
    }
    private Command scoreBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()-> lifterSubsystem.moveArm(0.4)),
                new WaitCommand(100),
                new InstantCommand(()-> clawSubsystem.setPos(clawSubsystem.clawOpen)),
                new WaitCommand(200),
                new InstantCommand(()->lifterSubsystem.moveArm(0))
        );
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(startPose)),
                new InstantCommand(()->lifterSubsystem.setUsePID(true)),
                driveSubsystem.FollowPath(alignBehindBlock1, true),
                new WaitCommand(1000),
                driveSubsystem.FollowPath(pushBlock1, true),
                grabBlock(),
                driveSubsystem.FollowPath(score1, true),
                scoreBlock(),
                driveSubsystem.FollowPath(alignBehindBlock2, true),
                new WaitCommand(1000),
                driveSubsystem.FollowPath(pushBlock2, true),
                grabBlock(),
                driveSubsystem.FollowPath(score2, true),
                scoreBlock(),
                /*driveSubsystem.FollowPath(alignBehindBlock3, true),
                new WaitCommand(1000),
                driveSubsystem.FollowPath(pushblock3, true),
                grabBlock(),
                driveSubsystem.FollowPath(score3, true),
                scoreBlock(),*/
                driveSubsystem.FollowPath(driveToPark, true)

        );
    }
}

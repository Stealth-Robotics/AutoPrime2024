package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.zeroLifterCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "bucketCycleAutoTuned")
public class bucketCycleAutoTuned extends StealthOpMode {
    DriveSubsystem driveSubsystem;
    Follower follower;
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    ReacherSubsystem reacherSubsystem;
    IntakeSubsystem intakeSubsystem;
    FlipperSubsystem flipperSubsystem;
    static Pose startPose = new Pose(9.01,112.46,Math.toRadians(135));
    static Pose scorePose = new Pose(15.77,127.54,Math.toRadians(135));
    static Pose intake1Pose = new Pose(30.67,121.13,Math.toRadians(180));
    static Pose intake2Pose = new Pose(30.67,130.14,Math.toRadians(180));
    static Pose intake3Pose = new Pose(45.4,128.05,Math.toRadians(270));
    static Pose parkPose = new Pose(63.25,93.93,Math.toRadians(90));
    static PathChain startToScore, driveToBlock1, block1ToScore, driveToBlock2, block2ToScore, driveToBlock3, block3ToScore, driveToPark;
    @Override
    public void initialize(){
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        follower = new Follower(hardwareMap);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        reacherSubsystem = new ReacherSubsystem(hardwareMap, telemetry);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        startToScore = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(startPose.getHeading(),scorePose.getHeading())
                .build();
        driveToBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(intake1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),intake1Pose.getHeading())
                .build();
        block1ToScore = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(intake1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(intake1Pose.getHeading(), scorePose.getHeading())
                .build();
        driveToBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(intake2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), intake2Pose.getHeading())
                .build();
        block2ToScore = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(intake2Pose),new Point(scorePose)))
                .setLinearHeadingInterpolation(intake2Pose.getHeading(), scorePose.getHeading())
                .build();
        driveToBlock3 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(intake3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), intake3Pose.getHeading())
                .build();
        block3ToScore = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(intake3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(intake3Pose.getHeading(), scorePose.getHeading())
                .build();
        driveToPark = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), new Point(65.15,119.04,Point.CARTESIAN), new Point(parkPose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading())
                .build();
    }
    public Command intakeBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()->lifterSubsystem.moveArm(0)),
                new deployIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, true, 0.3,1),
                new WaitCommand(1500),
                new zeroLifterCommand(lifterSubsystem),
                new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,lifterSubsystem,panSubsystem),
                new InstantCommand(()->lifterSubsystem.moveArm(1)));
    }
    public Command scoreBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(1000)
        );
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(startPose)),
                new InstantCommand(()->lifterSubsystem.setUsePID(true)),
                new InstantCommand(()->lifterSubsystem.moveArm(1)),
                new WaitCommand(1000),
                driveSubsystem.FollowPath(startToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToBlock1, true),
                intakeBlock(),
                driveSubsystem.FollowPath(block1ToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToBlock2, true),
                intakeBlock(),
                driveSubsystem.FollowPath(block2ToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToBlock3, true),
                intakeBlock(),
                driveSubsystem.FollowPath(block2ToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToPark, true)
        );
    }
}

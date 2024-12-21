package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.groundIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.zeroLifterCommand;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.commands.SaveAutoHeadingCommand;
import org.stealthrobotics.library.opmodes.StealthOpMode;

//@Autonomous(name = "bucketAuto")
public class bucketCycleAutoTuned extends StealthOpMode {
    DriveSubsystem driveSubsystem;
    Follower follower;
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    ReacherSubsystem reacherSubsystem;
    IntakeSubsystem intakeSubsystem;
    FlipperSubsystem flipperSubsystem;
    static Pose startPose = new Pose(10.916,118,Math.toRadians(135));
    static Pose bucketPose = new Pose(15.0,127.71,Math.toRadians(135));
    static Pose scorePose = new Pose(13.82,129,Math.toRadians(135));
    static Pose intake1Pose = new Pose(22,121.32,Math.toRadians(175));
    static Pose intake2Pose = new Pose(25.04,130.5,Math.toRadians(180));
    static Pose intake3Pose = new Pose(30,133,Math.toRadians(225));
    static Pose halfwayToPark = new Pose(47.13,116.1,Math.toRadians(230));
    static Pose parkPose = new Pose(63.25,93.93,Math.toRadians(270));
    static PathChain startToScore, inchToBucket, driveToBlock1, block1ToScore, driveToBlock2, block2ToScore, driveToBlock3, block3ToScore, driveToPark1, driveToPark2;
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
                .addPath(new BezierLine(new Point(startPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
        inchToBucket = follower.pathBuilder()
                .addPath(new BezierLine(new Point(bucketPose), new Point(scorePose)))
                .setConstantHeadingInterpolation(bucketPose.getHeading())
                .build();
        driveToBlock1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(intake1Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),intake1Pose.getHeading())
                .build();
        block1ToScore = follower.pathBuilder()
                .addPath(new BezierLine(new Point(intake1Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(intake1Pose.getHeading(), scorePose.getHeading())
                .build();
        driveToBlock2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(intake2Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), intake2Pose.getHeading())
                .build();
        block2ToScore = follower.pathBuilder()
                .addPath(new BezierLine(new Point(intake2Pose),new Point(scorePose)))
                .setLinearHeadingInterpolation(intake2Pose.getHeading(), scorePose.getHeading())
                .build();
        driveToBlock3 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(intake3Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), intake3Pose.getHeading())
                .build();
        block3ToScore = follower.pathBuilder()
                .addPath(new BezierLine(new Point(intake3Pose), new Point(scorePose)))
                .setLinearHeadingInterpolation(intake3Pose.getHeading(), scorePose.getHeading())
                .build();
        driveToPark1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(halfwayToPark)))
                .setLinearHeadingInterpolation(scorePose.getHeading(), halfwayToPark.getHeading())
                .build();
        driveToPark2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(halfwayToPark), new Point(parkPose)))
                .setLinearHeadingInterpolation(halfwayToPark.getHeading(), parkPose.getHeading())
                .build();
    }
    public Command intakeBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()->lifterSubsystem.moveArm(0)),
                new InstantCommand(()->reacherSubsystem.setMaxSpeed(0.7)),
                new groundIntakeCommand(intakeSubsystem, reacherSubsystem, flipperSubsystem, 1),
                new WaitCommand(1000),
                new zeroLifterCommand(lifterSubsystem),
                new InstantCommand(()->reacherSubsystem.setMaxSpeed(1)),
                new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,lifterSubsystem,panSubsystem),
                new InstantCommand(()->lifterSubsystem.moveArm(0.95)));
    }
    public Command scoreBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(300),
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.in)),
                new WaitCommand(50),
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(400),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.3))
        );
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(startPose)),
                new InstantCommand(()->lifterSubsystem.setUsePID(true)),
                new InstantCommand(()->reacherSubsystem.setMaxSpeed(0.5)),
                new InstantCommand(()->lifterSubsystem.moveArm(1)),
                new InstantCommand(()->flipperSubsystem.goToPos(0.55)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.3)),
                new WaitCommand(1000),
                driveSubsystem.FollowPath(startToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToBlock1, true),
                intakeBlock(),
                new WaitCommand(750),
                driveSubsystem.FollowPath(block1ToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToBlock2, true),
                intakeBlock(),
                new WaitCommand(750),
                driveSubsystem.FollowPath(block2ToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToBlock3, true),
                new InstantCommand(()->lifterSubsystem.moveArm(0)),
                new groundIntakeCommand(intakeSubsystem, reacherSubsystem, flipperSubsystem, 0.5),
                new WaitCommand(500),
                new zeroLifterCommand(lifterSubsystem),
                new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,lifterSubsystem,panSubsystem),
                new InstantCommand(()->lifterSubsystem.moveArm(0.95)),
                driveSubsystem.FollowPath(block2ToScore, true),
                scoreBlock(),
                driveSubsystem.FollowPath(driveToPark1, false),
                new InstantCommand(()->lifterSubsystem.moveArm(0.1)),
                driveSubsystem.FollowPath(driveToPark2, true)
        ).raceWith(Commands.run(() -> new SaveAutoHeadingCommand(()->follower.getTotalHeading())));
    }
}

package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
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

@Autonomous(name = "bucketAuto: RNG edition")
public class bucketCycleAutoRNG extends StealthOpMode {
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
    static Pose intake2Pose = new Pose(25.04,129,Math.toRadians(180));
    static Pose intake3Pose = new Pose(27,123,Math.toRadians(225));
    static Pose intake4Pose = new Pose(61.343,96.52,Math.toRadians(90));
    static Point intake4Handle = new Point(53.89,119.39,1);
    static Pose halfwayToPark = new Pose(47.13,116.1,Math.toRadians(230));
    static Pose parkPose = new Pose(63.25,93.93,Math.toRadians(270));
    static PathChain startToScore, inchToBucket, driveToBlock1, block1ToScore, driveToBlock2, block2ToScore, driveToBlock3, block3ToScore, driveToSub, subToScore, driveToPark1, driveToPark2;
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
        driveToSub = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scorePose), intake4Handle, new Point(intake4Pose)))
                .setLinearHeadingInterpolation(scorePose.getHeading(),intake4Pose.getHeading())
                .build();
        subToScore = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(intake4Pose), intake4Handle, new Point(scorePose)))
                .setLinearHeadingInterpolation(intake4Pose.getHeading(),scorePose.getHeading())
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
                new WaitCommand(750),
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
    private Command delayedScore(long delay){
        return new SequentialCommandGroup(
                new WaitCommand(delay),
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.out))
        );
    }
    private Command delayedDeploy(long delay1, double position){
        return new SequentialCommandGroup(
                new WaitCommand(delay1),
                new InstantCommand(()->lifterSubsystem.moveArm(0)),
                new InstantCommand(()->flipperSubsystem.goToPos(0.85)),
                new InstantCommand(()->intakeSubsystem.setPower(-1)),
                new WaitCommand(300),
                new InstantCommand(()->reacherSubsystem.setSetPoint(position))
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
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(startToScore, true),
                        delayedScore(750)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.3)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(driveToBlock1, true),
                        new InstantCommand(()->reacherSubsystem.setMaxSpeed(0.7)),
                        delayedDeploy(750,1)),
                intakeBlock(),
                new WaitCommand(750),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(block1ToScore, true),
                        delayedScore(750)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.3)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(driveToBlock2, true),
                        new InstantCommand(()->reacherSubsystem.setMaxSpeed(0.7)),
                        delayedDeploy(800,0.8)),
                intakeBlock(),
                new WaitCommand(750),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(block2ToScore, true),
                        delayedScore(1250)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.3)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(driveToBlock3, true),
                        new InstantCommand(()->reacherSubsystem.setMaxSpeed(0.7)),
                        delayedDeploy(800,0.7)),
                intakeBlock(),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(block3ToScore, true),
                        delayedScore(1750)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.3)),
                driveSubsystem.FollowPath(driveToSub, true),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(driveToSub, true),
                        new InstantCommand(()->reacherSubsystem.setMaxSpeed(1)),
                        delayedDeploy(1000,0.6)),
                intakeBlock(),
                driveSubsystem.FollowPath(subToScore, true),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(subToScore, true),
                        delayedScore(2000))
        );//.raceWith(Commands.run(() -> new SaveAutoHeadingCommand(()->follower.getTotalHeading())));
    }
}

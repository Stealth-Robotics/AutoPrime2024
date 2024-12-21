package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.RunCommand;
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
import org.firstinspires.ftc.teamcode.subsystems.IntakeSensorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.commands.SaveAutoHeadingCommand;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "bucketAuto")
public class bucketCycleAutoRNG extends StealthOpMode {
    DriveSubsystem driveSubsystem;
    Follower follower;
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    ReacherSubsystem reacherSubsystem;
    IntakeSubsystem intakeSubsystem;
    IntakeSensorSubsystem intakeSensorSubsystem;
    FlipperSubsystem flipperSubsystem;
    static Pose startPose = new Pose(10.916,118,Math.toRadians(135));
    static Pose bucketPose = new Pose(15.0,127.71,Math.toRadians(135));
    static Pose scorePose = new Pose(14.25,129.25,Math.toRadians(135));
    static Pose intake1Pose = new Pose(23.5,122.5,Math.toRadians(175));
    static Pose intake2Pose = new Pose(26,129,Math.toRadians(180));
    static Pose intake3Pose = new Pose(31,123,Math.toRadians(240));
    static Pose intake4Pose = new Pose(58,98,Math.toRadians(90));
    static Point intake4Handle = new Point(53.89,119.39,1);
    static Pose halfwayToPark = new Pose(59,100,Math.toRadians(180));
    static Pose parkPose = new Pose(63.25,93.93,Math.toRadians(270));
    static Pose wobble1Pose = new Pose(68,98,Math.toRadians(120));
    static Pose wobble1Pose2 = new Pose(65,94,Math.toRadians(90));
    static Pose wobble2Pose1 = new Pose(59,98,Math.toRadians(80));
    static Pose wobble2Pose2 = new Pose(59,98,Math.toRadians(100));
    static PathChain startToScore, inchFromBucket, driveToBlock1, block1ToScore, driveToBlock2, block2ToScore, driveToBlock3, block3ToScore, driveToSub, subToScore, driveToPark1, driveToPark2;
    static PathChain wobble1, wobble2;
    @Override
    public void initialize(){
        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
        follower = new Follower(hardwareMap);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        reacherSubsystem = new ReacherSubsystem(hardwareMap, telemetry);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        intakeSensorSubsystem = new IntakeSensorSubsystem(hardwareMap, telemetry);
        startToScore = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scorePose)))
                .setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading())
                .build();
        inchFromBucket = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scorePose), new Point(bucketPose)))
                .setConstantHeadingInterpolation(scorePose.getHeading())
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
        wobble1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(intake4Pose), new Point(wobble1Pose)))
                .setLinearHeadingInterpolation(intake4Pose.getHeading(),wobble1Pose.getHeading())
                .addPath(new BezierLine(new Point(wobble1Pose), new Point(intake4Pose)))
                .setLinearHeadingInterpolation(wobble1Pose.getHeading(), intake4Pose.getHeading())
                .addPath(new BezierLine(new Point(intake4Pose), new Point(wobble1Pose2)))
                .setLinearHeadingInterpolation(intake4Pose.getHeading(), wobble1Pose2.getHeading())
                .build();
        wobble2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(intake4Pose), new Point(wobble2Pose1)))
                .setLinearHeadingInterpolation(intake4Pose.getHeading(),wobble2Pose1.getHeading())
                .addPath(new BezierLine(new Point(wobble2Pose1), new Point(wobble2Pose2)))
                .setLinearHeadingInterpolation(wobble2Pose1.getHeading(),wobble2Pose2.getHeading())
                .addPath(new BezierLine(new Point(wobble2Pose2), new Point(wobble2Pose1)))
                .setLinearHeadingInterpolation(wobble2Pose2.getHeading(),wobble2Pose1.getHeading())
                .addPath(new BezierLine(new Point(wobble2Pose1), new Point(intake4Pose)))
                .setLinearHeadingInterpolation(wobble2Pose1.getHeading(),intake4Pose.getHeading())
                .build();
        subToScore = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(wobble1Pose2), intake4Handle, new Point(scorePose)))
                .setLinearHeadingInterpolation(wobble1Pose2.getHeading(),scorePose.getHeading())
                .build();
        driveToPark1 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(wobble1Pose2), new Point(halfwayToPark)))
                .setLinearHeadingInterpolation(wobble1Pose2.getHeading(), halfwayToPark.getHeading())
                .build();
        driveToPark2 = follower.pathBuilder()
                .addPath(new BezierLine(new Point(halfwayToPark), new Point(parkPose)))
                .setLinearHeadingInterpolation(halfwayToPark.getHeading(), parkPose.getHeading())
                .build();
    }
    private Command intakeBlock(){
        return new SequentialCommandGroup(
                new zeroLifterCommand(lifterSubsystem),
                new InstantCommand(()->reacherSubsystem.setMaxSpeed(1)),
                new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,lifterSubsystem,panSubsystem),
                new InstantCommand(()->lifterSubsystem.moveArm(0.95)),
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(50),
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.in)));
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
                new InstantCommand(()->flipperSubsystem.goToPos(0.83)),
                new InstantCommand(()->intakeSubsystem.setPower(-1)),
                new WaitCommand(150),
                new InstantCommand(()->reacherSubsystem.setSetPoint(position))
        );
    }
    private Command park(){
        return new SequentialCommandGroup(
                new InstantCommand(()->intakeSubsystem.setPower(1)),
                new InstantCommand(()->flipperSubsystem.goToPos(0.55)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0)),
                new WaitCommand(500),
                driveSubsystem.FollowPath(driveToPark1, false),
                new InstantCommand(()->lifterSubsystem.moveArm(0.1)),
                driveSubsystem.FollowPath(driveToPark2,true)
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
                        delayedDeploy(600,1)),
                new WaitCommand(600),
                intakeBlock(),
                new WaitCommand(700),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(block1ToScore, true),
                        delayedScore(750)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.3)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(driveToBlock2, true),
                        new InstantCommand(()->reacherSubsystem.setMaxSpeed(0.7)),
                        delayedDeploy(500,0.9)),
                new WaitCommand(400),
                intakeBlock(),
                new WaitCommand(700),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(block2ToScore, true),
                        delayedScore(900)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.6)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(driveToBlock3, true),
                        new InstantCommand(()->reacherSubsystem.setMaxSpeed(0.7)),
                        delayedDeploy(800,0.9)),
                new WaitCommand(400),
                intakeBlock(),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(block3ToScore, true),
                        delayedScore(1500)),
                new InstantCommand(()->reacherSubsystem.setSetPoint(0.5)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(driveToSub, false),
                        new InstantCommand(()->reacherSubsystem.setMaxSpeed(1)),
                        delayedDeploy(3000,1)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(wobble1, true),
                        new SequentialCommandGroup(
                                new WaitCommand(2500),
                                new RunCommand(()->{if(intakeSensorSubsystem.readSensorColor() == IntakeSensorSubsystem.ColorList.RED){
                                    new SequentialCommandGroup(
                                            new InstantCommand(()->intakeSubsystem.setPower(1)),
                                            new WaitCommand(1000),
                                            park());
                                }}),
                                intakeBlock()
                        )
                ),
                new InstantCommand(()->flipperSubsystem.goToPos(0.7)),
                new ParallelCommandGroup(
                        driveSubsystem.FollowPath(subToScore, true),
                        delayedScore(3000)),
                driveSubsystem.FollowPath(inchFromBucket, false)
        );//.raceWith(Commands.run(() -> new SaveAutoHeadingCommand(()->follower.getTotalHeading())));
    }
}

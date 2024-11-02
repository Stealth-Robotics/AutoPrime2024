package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.reverseIntakeCommand;
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

@Autonomous (name = "bucketAuto")
public class bucketAuto extends StealthOpMode{
    DriveSubsystem driveSubsystem;
    IntakeSubsystem intakeSubsystem;
    FlipperSubsystem flipperSubsystem;
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    Follower follower;
    BluePaths bluePaths;

    final Pose bucketStartingPose = new Pose(9.787, 84.983, 0);
    final Pose bucketScoringPose = new Pose(24.43, 125.93, Math.toRadians(135));
    final Pose parkLeft = new Pose(61.8,99.78,Math.toRadians(270));
    final Pose parkRight = new Pose(83.06,98.65, Math.toRadians(270));

    PathChain driveToBucket, parkLeftFromBucket, parkRightFromBucket;

    @Override
    public void initialize(){
        driveSubsystem = new DriveSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        follower = new Follower(hardwareMap);
        //bluePaths = new BluePaths(follower);
        //bluePaths.buildPaths();


        driveToBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketStartingPose), new Point(44.36, 95.48, Point.CARTESIAN), new Point(bucketScoringPose)))
                .setLinearHeadingInterpolation(bucketStartingPose.getHeading(), bucketScoringPose.getHeading())
                .build();
        parkLeftFromBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketScoringPose), new Point(20.41,98.08,Point.CARTESIAN), new Point(65.2,12.29,Point.CARTESIAN), new Point(parkLeft)))
                .setLinearHeadingInterpolation(bucketScoringPose.getHeading(), parkLeft.getHeading())
                .build();
        parkRightFromBucket = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(bucketScoringPose), new Point(86.61,129.54,Point.CARTESIAN), new Point(parkRight)))
                .setLinearHeadingInterpolation(bucketScoringPose.getHeading(), parkRight.getHeading())
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
                new InstantCommand(()->driveSubsystem.setPose(bucketStartingPose)),
                driveSubsystem.FollowPath(driveToBucket, true),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.35)),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.in)),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.25)),
                new InstantCommand(()-> intakeSubsystem.setPower(1)),
                new WaitCommand(1000),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.35)),
                new InstantCommand(()-> intakeSubsystem.setPower(0)),
                new InstantCommand(()-> lifterSubsystem.moveArm(1)),
                new WaitCommand(2000),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(3000),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.in)),
                new InstantCommand(()-> lifterSubsystem.moveArm(0))
                //driveSubsystem.FollowPath(parkLeftFromBucket, true)
        );
    }
}
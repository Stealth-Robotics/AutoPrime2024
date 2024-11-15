package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
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

@Autonomous(name = "shuttleCycleIntakeAuto")
public class shuttleCycleAutoIntake extends StealthOpMode {
    Follower follower;
    DriveSubsystem driveSubsystem;
    LifterSubsystem lifterSubsystem;
    ClawSubsystem clawSubsystem;
    IntakeSubsystem intakeSubsystem;
    FlipperSubsystem flipperSubsystem;
    ReacherSubsystem reacherSubsystem;
    LifterPanSubsystem panSubsystem;


    //If you want to alter the driving, change these
    //Increasing X moves you closer to the other alliance station
    //Increasing Y moves you closer to the left wall (bucket side)
    //Increasing angle rotates you counter-clockwise
    static Pose startPose = new Pose(8.16,44.465,Math.toRadians(180));
    static Pose grabBlock1 = new Pose(20,27,Math.toRadians(180));
    static Pose depositBlock1 = new Pose(11,27,Math.toRadians(180));
    static Pose scorePreset = new Pose(25,72,0); //X on this one might need to be altered (increasing moves closer to the bar)
    static Pose grabBlock2 = new Pose(20,13.666,Math.toRadians(180));
    static Pose depositBlock2 = new Pose(10,13.666,Math.toRadians(170));
    static Pose scoreBlock1 = new Pose(26,75,0); //X on this one might need to be altered (increasing moves closer to the bar)
    static Pose behindBlock3First = new Pose(20,28.555, Math.toRadians(90)); //not used
    static Pose behindBlock3 = new Pose(62,9.38,Math.toRadians(180)); //not used
    static Pose depositBlock3 = new Pose(8.16,9.38,Math.toRadians(180)); //not used
    static Pose scoreBlock2 = new Pose(36.31,68,0); //not used
    static Pose park = new Pose(5,22.43,0);
    static PathChain alignBehindBlock1, pushBlock1, score1, alignBehindBlock2, pushBlock2, score2, alignBehindBlock3, pushblock3, score3, driveToPark;
    @Override
    public void initialize(){
        follower = new Follower(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap,telemetry);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        clawSubsystem = new ClawSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        reacherSubsystem = new ReacherSubsystem(hardwareMap,telemetry);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        alignBehindBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose), new Point(grabBlock1)))
                .setLinearHeadingInterpolation(startPose.getHeading(), grabBlock1.getHeading())
                .build();
        pushBlock1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock1), new Point(depositBlock1)))
                .setConstantHeadingInterpolation(grabBlock1.getHeading())
                .build();
        score1 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(depositBlock1), new Point(scorePreset)))
                .setLinearHeadingInterpolation(depositBlock1.getHeading(), scorePreset.getHeading())
                .build();
        alignBehindBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(scoreBlock1), new Point(grabBlock2)))
                .setLinearHeadingInterpolation(scoreBlock1.getHeading(), grabBlock2.getHeading())
                .build();
        pushBlock2 = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(grabBlock2), new Point(depositBlock2)))
                .setConstantHeadingInterpolation(grabBlock2.getHeading())
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
    private Command intakeBlock(){
        return new SequentialCommandGroup(
                new deployIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,true,0.4,1),
                new WaitCommand(1000),
                new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem,lifterSubsystem,panSubsystem)
        );
    }
    private Command grabBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()->panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(500), //Wait to drop block so we don't get a penalty
                new InstantCommand(()->clawSubsystem.setPos(clawSubsystem.clawClosed)),
                new WaitCommand(500), //Wait to grab block before raising arm
                new InstantCommand(()->lifterSubsystem.moveArm(0.42))
        );
    }
    private Command scoreBlock(){
        return new SequentialCommandGroup(
                new InstantCommand(()-> lifterSubsystem.moveArm(0.3)),
                new WaitCommand(300), //Wait to finish moving arm before opening claw
                new InstantCommand(()-> clawSubsystem.setPos(clawSubsystem.clawOpen)),
                new WaitCommand(200), //Wait to open claw before lowering arm
                new InstantCommand(()->lifterSubsystem.moveArm(0))
        );
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()->driveSubsystem.setPose(startPose)),
                new InstantCommand(()->lifterSubsystem.setUsePID(true)),
                driveSubsystem.FollowPath(alignBehindBlock1, true),
                intakeBlock(),
                driveSubsystem.FollowPath(pushBlock1, true),
                grabBlock(),
                driveSubsystem.FollowPath(score1, true),
                scoreBlock(),
                driveSubsystem.FollowPath(alignBehindBlock2, true),
                intakeBlock(),
                driveSubsystem.FollowPath(pushBlock2, true),
                grabBlock(),
                driveSubsystem.FollowPath(score2, true),
                scoreBlock(),
                /*driveSubsystem.FollowPath(alignBehindBlock3, true),
                new WaitCommand(1000)/*,
                driveSubsystem.FollowPath(pushblock3, true),
                grabBlock(),
                driveSubsystem.FollowPath(score3, true),
                scoreBlock(),*/
                driveSubsystem.FollowPath(driveToPark, true)

        );
    }
}

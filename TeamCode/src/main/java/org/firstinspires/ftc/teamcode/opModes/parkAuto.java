package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "parkAuto")
public class parkAuto extends StealthOpMode {
    Follower follower;
    DriveSubsystem driveSubsystem;
    static Pose startPose = new Pose(10,60);
    static Pose endPose = new Pose(10,20); //If its not driving far enough decrease Y
    static PathChain park;
    @Override
    public void initialize(){
        follower = new Follower(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap,telemetry);
        park = follower.pathBuilder()
                .addPath(new BezierCurve(new Point(startPose),new Point(endPose)))
                .setConstantHeadingInterpolation(0)
                .build();
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
            new InstantCommand(()->driveSubsystem.setPose(startPose)),
            driveSubsystem.FollowPath(park, true)
        );
    }
}

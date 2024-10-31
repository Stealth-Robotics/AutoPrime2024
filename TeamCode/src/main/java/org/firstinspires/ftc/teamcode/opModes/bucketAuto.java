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

    @Override
    public void initialize(){
        driveSubsystem = new DriveSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        bluePaths = new BluePaths(follower);
        register(driveSubsystem, lifterSubsystem);
    }
    @Override
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                driveSubsystem.FollowPath(BluePaths.driveToBucket, true),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.35)),
                new InstantCommand(()-> panSubsystem.setPos(pan.in)),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.25)),
                new InstantCommand(()-> intakeSubsystem.setPower(1)),
                new WaitCommand(1000),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.35)),
                new InstantCommand(()-> lifter.moveArm(1)),
                new WaitCommand(2000),
                new InstantCommand(()-> panSubsystem.setPos(pan.out)),
                new WaitCommand(1000),
                new InstantCommand(()-> panSubsystem.setPos(pan.in)),
                new InstantCommand(()-> lifterSubsystem.moveArm(0)),
                driveSubsystem.FollowPath(bluePaths.parkLeftFromBucket, true)
        );
    }

}
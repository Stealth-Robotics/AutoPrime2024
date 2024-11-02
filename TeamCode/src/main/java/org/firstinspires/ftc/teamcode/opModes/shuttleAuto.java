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
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSensorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "shuttleAuto")
public class shuttleAuto extends StealthOpMode {
    DriveSubsystem driveSubsystem;
    IntakeSubsystem intakeSubsystem;
    ReacherSubsystem reacherSubsystem;
    FlipperSubsystem flipperSubsystem;
    LifterSubsystem lifterSubsystem;
    LifterPanSubsystem panSubsystem;
    BluePaths bluePaths;
    Follower follower;
    @Override
    public void initialize(){
    driveSubsystem = new DriveSubsystem(hardwareMap);
    intakeSubsystem = new IntakeSubsystem(hardwareMap);
    reacherSubsystem = new ReacherSubsystem(hardwareMap, telemetry);
    flipperSubsystem = new FlipperSubsystem(hardwareMap);
    lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
    panSubsystem = new LifterPanSubsystem(hardwareMap);
    bluePaths = new BluePaths(follower);
    bluePaths.buildPaths();
    register(driveSubsystem, reacherSubsystem);
    }
    @Override
    public void whileWaitingToStart() {
        CommandScheduler.getInstance().run();
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup(
                new InstantCommand(()-> driveSubsystem.setPose(bluePaths.shuttleStartingPose)),
                driveSubsystem.FollowPath(bluePaths.startToShuttle, true),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.35)),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.in)),
                new InstantCommand(()-> flipperSubsystem.goToPos(0.25)),
                new InstantCommand(()-> intakeSubsystem.setPower(1)),
                new WaitCommand(1000),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
                new WaitCommand(1000),
                new deployIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, true, IntakeSensorSubsystem.Alliance.BLUE),
                new WaitCommand(1000),
                new retractIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, lifterSubsystem, panSubsystem),
                new WaitCommand(1000),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
                driveSubsystem.FollowPath(bluePaths.shuttleFromStartPath, true),
                new deployIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, true, IntakeSensorSubsystem.Alliance.BLUE),
                new WaitCommand(1000),
                new retractIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, lifterSubsystem, panSubsystem),
                new WaitCommand(1000),
                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out))
        );
    }
}

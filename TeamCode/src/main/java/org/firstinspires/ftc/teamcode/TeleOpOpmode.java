package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.lifterDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.reacherDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.reverseIntakeCommand;
import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSensorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;
@TeleOp(name = "TeleOp")
public class TeleOpOpmode extends StealthOpMode {
    LifterSubsystem lifterSubsystem;
    ReacherSubsystem reacherSubsystem;
    IntakeSubsystem intakeSubsystem;
    IntakeSensorSubsystem intakeSensorSubsystem;
    FlipperSubsystem flipperSubsystem;
    DriveSubsystem driveSubsystem;

    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;

    FtcDashboard dashboard = FtcDashboard.getInstance();

    @Override
    public void whileWaitingToStart(){
        CommandScheduler.getInstance().run();
    }

    @Override
    public void initialize(){
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        reacherSubsystem = new ReacherSubsystem(hardwareMap);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        intakeSensorSubsystem = new IntakeSensorSubsystem(hardwareMap,telemetry);
        //driveSubsystem = new DriveSubsystem(hardwareMap, new SampleMecanumDrive(hardwareMap));

        register(lifterSubsystem);


        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

        lifterSubsystem.setDefaultCommand(new lifterDefaultCommand(lifterSubsystem,() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_DOWN),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_LEFT),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_UP),()->driverGamepad.getButton(GamepadKeys.Button.LEFT_BUMPER), ()->driverGamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER)));
        //reacherSubsystem.setDefaultCommand(new reacherDefaultCommand(reacherSubsystem, ()->driverGamepad.getButton(GamepadKeys.Button.A)));

        //driverGamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(new InstantCommand(()->intakeSubsystem.setPower(-1)));
        //driverGamepad.getGamepadButton(GamepadKeys.Button.Y).whenPressed(new InstantCommand(()->flipperSubsystem.goToPos(1)));
        //driveSubsystem.setDefaultCommand(driveSubsystem.driveTeleop(()->driverGamepad.getLeftX(),()->driverGamepad.getLeftY(),()->driverGamepad.getRightX()));
        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(new deployIntakeCommand(reacherSubsystem, flipperSubsystem,intakeSubsystem));
        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem));
        driverGamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(new reverseIntakeCommand(intakeSubsystem));


    }
}

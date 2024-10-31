package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.drive.MecanumDrive;
//import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.RunCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.driveDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.lifterDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.reacherDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.reverseIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.toggleClawCommand;
import org.firstinspires.ftc.teamcode.commands.togglePanTiltCommand;
import org.firstinspires.ftc.teamcode.commands.zeroLifterCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSensorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.opmodes.StealthOpMode;
@TeleOp(name = "TeleOp")
public class TeleOpOpmode extends StealthOpMode {
    LifterSubsystem lifterSubsystem;
    ReacherSubsystem reacherSubsystem;
    IntakeSubsystem intakeSubsystem;
    IntakeSensorSubsystem intakeSensorSubsystem;
    FlipperSubsystem flipperSubsystem;
    DriveSubsystem driveSubsystem;
    LifterPanSubsystem panSubsystem;
    ClawSubsystem clawSubsystem;
    Mecanum mecanum;

    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;

    FtcDashboard dashboard = FtcDashboard.getInstance();

    @Override
    public void whileWaitingToStart(){
        //CommandScheduler.getInstance().run();
    }

    @Override
    public void initialize(){
        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
        reacherSubsystem = new ReacherSubsystem(hardwareMap, telemetry);
        intakeSubsystem = new IntakeSubsystem(hardwareMap);
        flipperSubsystem = new FlipperSubsystem(hardwareMap);
        intakeSensorSubsystem = new IntakeSensorSubsystem(hardwareMap,telemetry);
        clawSubsystem = new ClawSubsystem(hardwareMap);
        driveSubsystem = new DriveSubsystem(hardwareMap);
        panSubsystem = new LifterPanSubsystem(hardwareMap);
        mecanum = new Mecanum(hardwareMap, telemetry);

        register(mecanum, reacherSubsystem);

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

        lifterSubsystem.setDefaultCommand(new lifterDefaultCommand(lifterSubsystem, clawSubsystem, () -> driverGamepad.getButton(GamepadKeys.Button.DPAD_DOWN),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_LEFT),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_UP), () -> driverGamepad.getButton(GamepadKeys.Button.LEFT_BUMPER), () -> driverGamepad.getButton(GamepadKeys.Button.RIGHT_BUMPER)));
        //driverGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new InstantCommand(()-> lifterSubsystem.moveArm(-0.5))).whenReleased(new zeroLifterCommand(lifterSubsystem));
        //driverGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(new InstantCommand(()->lifterSubsystem.moveArm(.8))).whenReleased(new InstantCommand(()-> lifterSubsystem.moveArm((lifterSubsystem.getPosition())/ lifterSubsystem.maxHeight)));
        //reacherSubsystem.setDefaultCommand(new reacherDefaultCommand(reacherSubsystem, ()->driverGamepad.getButton(GamepadKeys.Button.DPAD_DOWN),()->driverGamepad.getButton(GamepadKeys.Button.DPAD_UP)));

        //driveSubsystem.startTeleopDrive();
        //driveSubsystem.setDefaultCommand(new driveDefaultCommand(driveSubsystem, ()->driverGamepad.getLeftX(),()->driverGamepad.getLeftY(),()->driverGamepad.getRightX()));
        mecanum.setDefaultCommand(mecanum.driveTeleop(()->driverGamepad.getLeftX(),()-> driverGamepad.getLeftY(),()-> driverGamepad.getRightX()));
        new Trigger(() -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) > 0.1)
                .whenActive(new deployIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem));
        new Trigger(() -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.1)
                .whenActive(new retractIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, lifterSubsystem, panSubsystem));
        //driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new retractIntakeCommand(reacherSubsystem,flipperSubsystem,intakeSubsystem));
        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new reverseIntakeCommand(intakeSubsystem));
        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(new togglePanTiltCommand(panSubsystem));
        driverGamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(new toggleClawCommand(clawSubsystem));


    }
}

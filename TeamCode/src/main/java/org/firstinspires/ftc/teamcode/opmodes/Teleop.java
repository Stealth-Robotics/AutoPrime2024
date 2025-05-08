package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.InstantCommand;
        import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

        import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
        import org.firstinspires.ftc.teamcode.commands.eyesDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.lifterDefaultCommand;
        import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.reverseIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.toggleClawCommand;
import org.firstinspires.ftc.teamcode.commands.togglePanTiltCommand;
import org.firstinspires.ftc.teamcode.commands.zeroLifterCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.AutoToTeleStorage;
        import org.stealthrobotics.library.opmodes.StealthOpMode;

@TeleOp(name = "Teleop")
public class Teleop extends StealthOpMode {
    DriveSubsystem drive;
    Mecanum mecanum;

    LifterSubsystem elevator;
    LifterPanSubsystem pan;
    ReacherSubsystem extendo;
    IntakeSubsystem intake;

    ClawSubsystem claw;

    LimelightSubsystem ll;

    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;

    @Override
    public void initialize() {
//        elevator = new LifterSubsystem(hardwareMap, telemetry);
//        extendo = new ReacherSubsystem(hardwareMap, telemetry);
        intake = new IntakeSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        drive = new DriveSubsystem(hardwareMap, telemetry);
//        pan = new LifterPanSubsystem(hardwareMap);
        mecanum = new Mecanum(hardwareMap, telemetry);
//        ll = new LimelightSubsystem(hardwareMap, telemetry);

        mecanum.setHeading(AutoToTeleStorage.finalAutoHeading);

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

//        elevator.setDefaultCommand(new lifterDefaultCommand(lifterSubsystem, clawSubsystem, () -> driverGamepad.getButton(GamepadKeys.Button.DPAD_DOWN),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_LEFT),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_UP), () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER), () -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)));
        mecanum.setDefaultCommand(mecanum.driveTeleop(() -> driverGamepad.getLeftX(), () -> driverGamepad.getLeftY(), () -> driverGamepad.getRightX(), () -> driverGamepad.getButton(GamepadKeys.Button.LEFT_STICK_BUTTON)));

//        driverGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(new deployIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, true));
//        driverGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenReleased(new SequentialCommandGroup(new zeroLifterCommand(lifterSubsystem), new InstantCommand(()->lifterSubsystem.moveArm(0))));
//        driverGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(new retractIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, lifterSubsystem, panSubsystem));

        driverGamepad.getGamepadButton(GamepadKeys.Button.Y).whenPressed(new InstantCommand(() -> mecanum.resetHeading()));
//        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(new togglePanTiltCommand(panSubsystem));
        driverGamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(new toggleClawCommand(claw));
    }
}

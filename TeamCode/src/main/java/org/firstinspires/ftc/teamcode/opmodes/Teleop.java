package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem.ElevatorPosition;

import org.firstinspires.ftc.teamcode.commands.ElevatorDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.ExtendoDefaultCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.stealthrobotics.library.AutoToTeleStorage;
import org.stealthrobotics.library.opmodes.StealthOpMode;

import java.util.function.DoubleSupplier;

@TeleOp(name = "Teleop")
public class Teleop extends StealthOpMode {
    MecanumSubsystem mecanum;
    ElevatorSubsystem elevator;
    PanSubsystem pan;
    ExtendoSubsystem extendo;
    IntakeSubsystem intake;
    ClawSubsystem claw;

    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;

    @Override
    public void initialize() {
        elevator = new ElevatorSubsystem(hardwareMap);
        extendo = new ExtendoSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        pan = new PanSubsystem(hardwareMap);
        mecanum = new MecanumSubsystem(hardwareMap);

        mecanum.setHeading(AutoToTeleStorage.finalAutoHeading);

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

        mecanum.setDefaultCommand(
                mecanum.driveTeleop(
                    () -> driverGamepad.getLeftX(), () -> driverGamepad.getLeftY(), () -> driverGamepad.getRightX(), () -> driverGamepad.getButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                )
        );

        elevator.setDefaultCommand(
                new ElevatorDefaultCommand(
                        elevator, extendo, () -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER), () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                )
        );

        extendo.setDefaultCommand(
                new ExtendoDefaultCommand(
                        extendo, elevator, () -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER), () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
                )
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> mecanum.resetHeading()));
        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new InstantCommand(() -> claw.toggleState()));

//        driverGamepad.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(
//                () -> elevator.setMode(ElevatorSubsystem.ElevatorMode.HOLDING)
//        );
//
//        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(
//                new SequentialCommandGroup(
//                        new InstantCommand(() -> elevator.home()),
//                        pan.home(),
//                        intake.stop(),
//                        extendo.home()
//                )
//        );
//
//
//        driverGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
//                //Deploy intake & extendo
//                new SequentialCommandGroup(
//
//                )
//        );
//
//        driverGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
//                //Retract intake & extendo + transfer gamepiece into pan
//                new SequentialCommandGroup(
//
//                )
//        );
//
//        // ! Presets
        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                () -> elevator.setPosition(ElevatorPosition.LOW_BUCKET)
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                () -> elevator.setPosition(ElevatorPosition.LOW_BUCKET)
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
                () -> elevator.setPosition(ElevatorPosition.HIGH_CHAMBER)
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(
                () -> elevator.setPosition(ElevatorPosition.LOW_CHAMBER)
        );
    }
}

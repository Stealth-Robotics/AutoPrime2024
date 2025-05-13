package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.Trigger;
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
        DoubleSupplier triggerManualControl = () -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);

        elevator = new ElevatorSubsystem(hardwareMap, triggerManualControl);
        extendo = new ExtendoSubsystem(hardwareMap, triggerManualControl);
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

        //Manual elevator controls
        new Trigger(() -> Math.abs(triggerManualControl.getAsDouble()) > 0.05 && extendo.isHomed())
                .whenActive(elevator.manual(), true);

        //Manual extendo controls
        new Trigger(() -> Math.abs(triggerManualControl.getAsDouble()) > 0.05 && !extendo.isHomed())
                .whenActive(extendo.manual(), true);

        driverGamepad.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> mecanum.resetHeading()));
        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new InstantCommand(() -> claw.toggleState()));

        driverGamepad.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(
                () -> elevator.setMode(ElevatorSubsystem.ElevatorMode.HOLD)
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new SequentialCommandGroup(
                        new InstantCommand(() -> elevator.setPosition(ElevatorPosition.HOME)),
                        pan.home(),
                        intake.stop()
                )
        );


        driverGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                //Deploy intake & extendo
                new SequentialCommandGroup(

                )
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                //Retract intake & extendo + transfer gamepiece into pan
                new SequentialCommandGroup(

                )
        );

        if (claw.getState() == ClawSubsystem.ClawState.CLOSED) {
            //Presets for specimens
            driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                    () -> elevator.setPosition(ElevatorPosition.HIGH_CHAMBER)
            );

            driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                    () -> elevator.setPosition(ElevatorPosition.LOW_CHAMBER)
            );
        }
        else {
            //Presets for samples
            driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                    () -> elevator.setPosition(ElevatorPosition.HIGH_BUCKET)
            );

            driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                    () -> elevator.setPosition(ElevatorPosition.LOW_BUCKET)
            );
        }
    }
}

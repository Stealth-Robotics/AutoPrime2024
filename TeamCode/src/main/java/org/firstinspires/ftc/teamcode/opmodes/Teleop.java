package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.ElevatorDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.ExtendoDefaultCommand;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem.ElevatorPosition;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem.ElevatorMode;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem.ClawState;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem.ExtendoPosition;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem.ExtendoMode;
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

        //Manual elevator controls
        elevator.setDefaultCommand(
                new ElevatorDefaultCommand(elevator, extendo, () -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
        );

        //Manual extendo controls
        //TODO Fix
//        extendo.setDefaultCommand(
//                new ExtendoDefaultCommand(extendo, () -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) - driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER))
//        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> mecanum.resetHeading()));
        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new InstantCommand(() -> claw.toggleState()));

        driverGamepad.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(
                () -> elevator.setMode(ElevatorMode.HOLD)
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new SequentialCommandGroup(
                        new InstantCommand(() -> elevator.setPosition(ElevatorPosition.HOME)),
                        new InstantCommand(() -> extendo.setPosition(ExtendoPosition.HOME)),
                        new InstantCommand(() -> pan.home()),
                        new InstantCommand(() -> intake.stop())
                ),
                true
        );


        driverGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new SequentialCommandGroup(
                        new InstantCommand(() -> intake.intake()),
                        new InstantCommand(() -> extendo.setPosition(ExtendoPosition.DEPLOYED)),
                        new WaitCommand(200),
                        new InstantCommand(() -> intake.wristDown())
                )
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                new SequentialCommandGroup(
                        new InstantCommand(() -> intake.stop()),
                        new InstantCommand(() -> intake.wristUp()),
                        new InstantCommand(() -> extendo.setPosition(ExtendoPosition.HOME)),
                        new WaitCommand(1000),
                        new InstantCommand(() -> intake.outtake()),
                        new WaitCommand(1000),
                        new InstantCommand(() -> intake.stop()),
                        new InstantCommand(() -> intake.wristHome())
                )
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                () -> {
                    if (claw.getState().equals(ClawState.CLOSED)) {
                        elevator.setPosition(ElevatorPosition.HIGH_CHAMBER);
                    }
                    else {
                        elevator.setPosition(ElevatorPosition.HIGH_BUCKET);
                    }
                }
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                () -> {
                    if (claw.getState().equals(ClawState.CLOSED)) {
                        elevator.setPosition(ElevatorPosition.LOW_CHAMBER);
                    }
                    else {
                        elevator.setPosition(ElevatorPosition.LOW_BUCKET);
                    }
                }
        );

        //Scoring presets
        driverGamepad.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                new ConditionalCommand(
                        //Specimen Scoring
                        new SequentialCommandGroup(
                                //set elevator a bit lower than current and then toggle claw open
                                new InstantCommand(() -> claw.toggleState())
                        ),

                        //Sample Scoring
                        new SequentialCommandGroup(
                                new InstantCommand(() -> pan.score()),
                                new WaitCommand(1200),
                                new InstantCommand(() -> pan.home())
                        ),

                        () -> claw.getState().equals(ClawState.CLOSED)
                )
        );
    }
}

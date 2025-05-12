package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.stealthrobotics.library.AutoToTeleStorage;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@TeleOp(name = "Teleop")
public class Teleop extends StealthOpMode {
    MecanumSubsystem mecanumSubsystem;
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
        mecanumSubsystem = new MecanumSubsystem(hardwareMap);

        // ! Does this do anything?
        mecanumSubsystem.setHeading(AutoToTeleStorage.finalAutoHeading);

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

        mecanumSubsystem.setDefaultCommand(
                mecanumSubsystem.driveTeleop(
                    () -> driverGamepad.getLeftX(), () -> driverGamepad.getLeftY(), () -> driverGamepad.getRightX(), () -> driverGamepad.getButton(GamepadKeys.Button.LEFT_STICK_BUTTON)
                )
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> mecanumSubsystem.resetHeading()));

        //TODO tune value
        driverGamepad.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(
                elevator.setPosition(3000)
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new SequentialCommandGroup(
                        elevator.home(),
                        pan.home(),
                        intake.stop(),
                        extendo.home()
                )
        );

        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(
                new InstantCommand(() -> claw.toggleState())
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

        //High and Low Bucket Scoring Presets
//        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
//                () -> elevator.setElevatorPosition(0.1)
//        );
//
//        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
//                () -> elevator.setElevatorPosition(0.1)
//        );
//
        //High and Low Chamber Scoring Presets
//        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
//                () -> elevator.setElevatorPosition(0.1)
//        );
//
//        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(
//                () -> elevator.setElevatorPosition(0.1)
//        );
    }
}

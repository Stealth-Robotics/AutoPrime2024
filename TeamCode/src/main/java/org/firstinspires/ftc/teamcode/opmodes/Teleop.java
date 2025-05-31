package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.command.button.Trigger;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.AlignToSampleCommand;
import org.firstinspires.ftc.teamcode.commands.DeployIntakeCommand;
import org.firstinspires.ftc.teamcode.commands.ElevatorDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.ExtendoDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.IntakeDefaultCommand;
import org.firstinspires.ftc.teamcode.commands.ResetElevatorCommand;
import org.firstinspires.ftc.teamcode.commands.ResetExtendoCommand;
import org.firstinspires.ftc.teamcode.commands.RetractIntakeCommand;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem.ElevatorPosition;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem.ClawState;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LEDSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.stealthrobotics.library.Alliance;
import org.stealthrobotics.library.AutoToTeleStorage;
import org.stealthrobotics.library.opmodes.StealthOpMode;

public class Teleop extends StealthOpMode {
    MecanumSubsystem mecanum;
    ElevatorSubsystem elevator;
    PanSubsystem pan;
    ExtendoSubsystem extendo;
    IntakeSubsystem intake;
    ClawSubsystem claw;
    LEDSubsystem led;
    LimelightSubsystem ll;

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
        led = new LEDSubsystem(hardwareMap);
        ll = new LimelightSubsystem(hardwareMap);

        register(elevator, extendo, intake, claw, pan, mecanum, led, ll);

        schedule(
                new InstantCommand(() -> intake.wristHome()),
                new InstantCommand(() -> claw.setState(ClawState.OPEN))
        );

        mecanum.setHeading(AutoToTeleStorage.finalAutoHeading);

        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

        //Automated intake triggers
        Trigger retractIntakeTrigger = new Trigger(() -> (intake.getControlType().equals(IntakeSubsystem.IntakeControl.AUTOMATIC)) &&
                (Alliance.get() == Alliance.BLUE && (intake.getColor() == IntakeSubsystem.Color.BLUE || intake.getColor() == IntakeSubsystem.Color.YELLOW)) ||
                (Alliance.get() == Alliance.RED && (intake.getColor() == IntakeSubsystem.Color.RED || intake.getColor() == IntakeSubsystem.Color.YELLOW))
        );

        Trigger intakeSpitTrigger = new Trigger(() -> intake.getControlType().equals(IntakeSubsystem.IntakeControl.AUTOMATIC) &&
                ((Alliance.get() == Alliance.BLUE && intake.getColor() == IntakeSubsystem.Color.RED) ||
                (Alliance.get() == Alliance.RED && intake.getColor() == IntakeSubsystem.Color.BLUE))
        );

        retractIntakeTrigger.whenActive(new RetractIntakeCommand(extendo, intake, elevator, pan, false));
        intakeSpitTrigger.whenActive(
                new SequentialCommandGroup(
                        new InstantCommand(intake::outtake),
                        new WaitUntilCommand(() -> intake.getColor() == IntakeSubsystem.Color.BLACK),
                        new WaitCommand(200),
                        new InstantCommand(intake::intake)
                )
        );

        //Color coded limelight pipeline switching
        operatorGamepad.getGamepadButton(GamepadKeys.Button.Y).whenPressed(new InstantCommand(() -> ll.setPipeline(LimelightSubsystem.LLPipeline.YELLOW)));
        operatorGamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(new InstantCommand(() -> ll.setPipeline(LimelightSubsystem.LLPipeline.BLUE)));
        operatorGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new InstantCommand(() -> ll.setPipeline(LimelightSubsystem.LLPipeline.RED)));

        driverGamepad.getGamepadButton(GamepadKeys.Button.X).whenPressed(
                new AlignToSampleCommand(mecanum, ll)
        );

        mecanum.setDefaultCommand(
                mecanum.driveTeleop(
                    () -> driverGamepad.getLeftX(), () -> driverGamepad.getLeftY(), () -> driverGamepad.getRightX()
                )
        );

        //Manual elevator controls
        elevator.setDefaultCommand(
                new ElevatorDefaultCommand(elevator, extendo, () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER))
        );

        //Manual extendo controls
        extendo.setDefaultCommand(
                new ExtendoDefaultCommand(extendo, () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER))
        );

        //Manual intake controls (operator)
        intake.setDefaultCommand(
                new IntakeDefaultCommand(intake, extendo, led, () -> operatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.05, () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER))
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> intake.toggleControl()));

        driverGamepad.getGamepadButton(GamepadKeys.Button.START).whenPressed(new InstantCommand(() -> mecanum.resetHeading()));
        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenPressed(new InstantCommand(() -> claw.toggleState()));

        operatorGamepad.getGamepadButton(GamepadKeys.Button.A).whenPressed(
                new ResetElevatorCommand(elevator)
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.BACK).whenPressed(
                new ResetExtendoCommand(extendo)
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).whenPressed(
                new DeployIntakeCommand(extendo, intake)
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER).whenPressed(
                new RetractIntakeCommand(extendo, intake, elevator, pan, false)
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenPressed(
                () -> {
                    if (claw.getState().equals(ClawState.CLOSED)) {
                        elevator.setPosition(ElevatorPosition.HIGH_CHAMBER);
                    }
                    else {
                        elevator.setPosition(ElevatorPosition.HIGH_BUCKET);
                    }
                }
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenPressed(
                () -> {
                    if (claw.getState().equals(ClawState.CLOSED)) {
                        elevator.setPosition(ElevatorPosition.LOW_CHAMBER);
                    }
                    else {
                        elevator.setPosition(ElevatorPosition.LOW_BUCKET);
                    }
                }
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).whenPressed(
                () -> elevator.setPosition(ElevatorPosition.HIGH_RUNG)
        );

        operatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).whenPressed(
                () -> elevator.setPosition(ElevatorPosition.LOW_RUNG)
        );

        //Scoring presets
        driverGamepad.getGamepadButton(GamepadKeys.Button.Y).whenPressed(
                new ConditionalCommand(
                        //Specimen Scoring
                        new SequentialCommandGroup(
                                //set elevator a bit lower than current and then toggle claw open
                                new InstantCommand(() -> elevator.setPosition(elevator.getPositionPercentage() - ElevatorSubsystem.DUNK_AMOUNT)),
                                new WaitCommand(100),
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

        ll.init();
    }

    @SuppressWarnings("unused")
    @TeleOp(name = "Red Teleop", group = "Red")
    public static class RedTeleop extends Teleop {

    }

    @SuppressWarnings("unused")
    @TeleOp(name = "Blue Teleop", group = "Blue")
    public static class BlueTeleop extends Teleop {

    }
}

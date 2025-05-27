package org.firstinspires.ftc.teamcode.commands;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.MathFunctions;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LEDSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;
import org.stealthrobotics.library.Alliance;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

@Config
public class IntakeDefaultCommand extends CommandBase {
    private final IntakeSubsystem intake;
    private final ExtendoSubsystem extendo;
    private final LEDSubsystem led;
    private final ElevatorSubsystem elevator;
    private final PanSubsystem pan;

    private final BooleanSupplier outtakeTrigger;
    private final BooleanSupplier controlSwitchTrigger;
    private final DoubleSupplier triggers;

    private IntakeControl controlType = IntakeControl.MANUAL;

    public enum IntakeControl {
        MANUAL,
        AUTOMATIC
    }

    public IntakeDefaultCommand(IntakeSubsystem intake, ExtendoSubsystem extendo, LEDSubsystem led, ElevatorSubsystem elevator, PanSubsystem pan, BooleanSupplier outtakeTrigger, BooleanSupplier controlSwitchTrigger, DoubleSupplier triggers) {
        this.intake = intake;
        this.extendo = extendo;
        this.led = led;
        this.elevator = elevator;
        this.pan = pan;
        this.outtakeTrigger = outtakeTrigger;
        this.controlSwitchTrigger = controlSwitchTrigger;
        this.triggers = triggers;

        addRequirements(intake, led);
    }

    @Override
    public void execute() {
        //Toggling logic
        if (controlSwitchTrigger.getAsBoolean()) {
            if (controlType.equals(IntakeControl.MANUAL)) controlType = IntakeControl.AUTOMATIC;
            else controlType = IntakeControl.MANUAL;
        }

        //Wrist
        if (Math.abs(triggers.getAsDouble()) > 0.05 && !extendo.isHomed())
            intake.wristTravel();
        else if (!extendo.isHomed())
            intake.wristDown();

        IntakeSubsystem.Color color = intake.getColor();

        if (controlType == IntakeControl.MANUAL) {
            if (outtakeTrigger.getAsBoolean())
                intake.outtake();
            else if (!extendo.isHomed())
                intake.intake();
            else
                intake.stop();

            if (color == IntakeSubsystem.Color.YELLOW)
                led.setMode(LEDSubsystem.LEDMode.YELLOW_SAMPLE);
            else if (color == IntakeSubsystem.Color.BLUE)
                led.setMode(LEDSubsystem.LEDMode.BLUE_SAMPLE);
            else if (color == IntakeSubsystem.Color.RED)
                led.setMode(LEDSubsystem.LEDMode.RED_SAMPLE);
            else
                led.setMode(LEDSubsystem.LEDMode.NO_SAMPLE);
        }
        else {
            led.setMode(LEDSubsystem.LEDMode.AUTOMATIC_INTAKE_STATE);

            if (Alliance.get() == Alliance.BLUE) {
                if (color == IntakeSubsystem.Color.RED) intake.outtake(); //Spit wrong colored sample
                else if (color == IntakeSubsystem.Color.BLUE || color == IntakeSubsystem.Color.YELLOW) new RetractIntakeCommand(extendo, intake, elevator, pan);
            }
            else if (Alliance.get() == Alliance.RED) {
                if (color == IntakeSubsystem.Color.BLUE) intake.outtake(); //Spit wrong colored sample
                else if (color == IntakeSubsystem.Color.RED || color == IntakeSubsystem.Color.YELLOW) new RetractIntakeCommand(extendo, intake, elevator, pan);
            }
            else throw new IllegalArgumentException("what da heck");
        }

        telemetry.addData("Intake Control Type", controlType);
    }
}

package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LEDSubsystem;

import java.util.function.BooleanSupplier;

public class IntakeDefaultCommand extends CommandBase {
    private final IntakeSubsystem intake;
    private final ExtendoSubsystem extendo;
    private final LEDSubsystem led;
    private final BooleanSupplier outtakeTrigger;

    public IntakeDefaultCommand(IntakeSubsystem intake, ExtendoSubsystem extendo, LEDSubsystem led, BooleanSupplier outtakeTrigger) {
        this.intake = intake;
        this.extendo = extendo;
        this.led = led;
        this.outtakeTrigger = outtakeTrigger;

        addRequirements(intake, led);
    }

    @Override
    public void execute() {
        if (outtakeTrigger.getAsBoolean())
            intake.outtake();
        else if (extendo.isHomed())
            intake.stop();
        else
            intake.intake();

        switch (intake.readSensorColor()) {
            case YELLOW:
                led.setMode(LEDSubsystem.LEDMode.YELLOW_SAMPLE);
                break;
            case BLUE:
                led.setMode(LEDSubsystem.LEDMode.BLUE_SAMPLE);
                break;
            case RED:
                led.setMode(LEDSubsystem.LEDMode.RED_SAMPLE);
                break;
            default:
                led.setMode(LEDSubsystem.LEDMode.NO_SAMPLE);
                break;
        }
    }
}

package org.firstinspires.ftc.teamcode.commands;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.MathFunctions;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LEDSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

@Config
public class IntakeDefaultCommand extends CommandBase {
    private final IntakeSubsystem intake;
    private final ExtendoSubsystem extendo;
    private final LEDSubsystem led;
    private final BooleanSupplier outtakeTrigger;
    private final DoubleSupplier triggers;

    public IntakeDefaultCommand(IntakeSubsystem intake, ExtendoSubsystem extendo, LEDSubsystem led, BooleanSupplier outtakeTrigger, DoubleSupplier triggers) {
        this.intake = intake;
        this.extendo = extendo;
        this.led = led;
        this.outtakeTrigger = outtakeTrigger;
        this.triggers = triggers;

        addRequirements(intake, led);
    }

    @Override
    public void execute() {
        //Intake
        if (outtakeTrigger.getAsBoolean())
            intake.outtake();
        else if (!extendo.isHomed())
            intake.intake();
        else
            intake.stop();

        //Wrist
        if (Math.abs(triggers.getAsDouble()) > 0.05 && !extendo.isHomed())
            intake.wristTravel();
        else if (!extendo.isHomed())
            intake.wristDown();

        //Color sensor
        IntakeSubsystem.Color color = intake.getColor();

        if (color == IntakeSubsystem.Color.YELLOW)
            led.setMode(LEDSubsystem.LEDMode.YELLOW_SAMPLE);
        else if (color == IntakeSubsystem.Color.BLUE)
            led.setMode(LEDSubsystem.LEDMode.BLUE_SAMPLE);
        else if (color == IntakeSubsystem.Color.RED)
            led.setMode(LEDSubsystem.LEDMode.RED_SAMPLE);
        else
            led.setMode(LEDSubsystem.LEDMode.NO_SAMPLE);
    }
}

package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.MathFunctions;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

import java.util.function.DoubleSupplier;

public class ExtendoDefaultCommand extends CommandBase {
    private final ExtendoSubsystem extendo;
    private final IntakeSubsystem intake;
    private final DoubleSupplier triggers;

    public static double manualSpeed = 0.2;
    public static double maxManualExtension = 1.0;
    public static double minManualExtension = 0.4;

    public ExtendoDefaultCommand(ExtendoSubsystem extendo, IntakeSubsystem intake, DoubleSupplier triggers) {
        this.extendo = extendo;
        this.intake = intake;
        this.triggers = triggers;

        addRequirements(extendo, intake);
    }

    @Override
    public void execute() {
        if (Math.abs(triggers.getAsDouble()) > 0.05 && !extendo.isHomed()) {
            extendo.setPosition(MathFunctions.clamp(extendo.getPositionPercentage() + triggers.getAsDouble() * manualSpeed, minManualExtension, maxManualExtension));
            intake.wristTravel();
        }
        else if (!extendo.isHomed()) intake.wristDown();
    }
}

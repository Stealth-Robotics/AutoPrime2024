package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import java.util.function.DoubleSupplier;

public class ExtendoDefaultCommand extends CommandBase {
    private final ExtendoSubsystem extendo;
    private final DoubleSupplier triggers;

    public static double manualSpeed = 0.2;

    public ExtendoDefaultCommand(ExtendoSubsystem extendo, DoubleSupplier triggers) {
        this.extendo = extendo;
        this.triggers = triggers;

        addRequirements(extendo);
    }

    @Override
    public void execute() {
        if (Math.abs(triggers.getAsDouble()) > 0.05 && !extendo.isHomed()) {
            extendo.setPosition(extendo.getPositionPercentage() + triggers.getAsDouble() * manualSpeed);
        }
    }
}

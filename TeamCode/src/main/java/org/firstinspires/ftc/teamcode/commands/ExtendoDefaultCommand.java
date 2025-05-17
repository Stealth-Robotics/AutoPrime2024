package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem.ExtendoMode;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class ExtendoDefaultCommand extends CommandBase {
    private final ExtendoSubsystem extendo;
    private final DoubleSupplier triggers;

    public ExtendoDefaultCommand(ExtendoSubsystem extendo, DoubleSupplier triggers) {
        this.extendo = extendo;
        this.triggers = triggers;

        addRequirements(extendo);
    }

    @Override
    public void execute() {
        if (Math.abs(triggers.getAsDouble()) > 0.05 && !extendo.isHomed()) {
            extendo.setMode(ExtendoMode.MANUAL);
            extendo.setPower(triggers.getAsDouble());
        }
        else {
            extendo.setMode(ExtendoMode.PID);
        }

        if (Math.abs(extendo.getPosition()) <= 10.0)
            extendo.resetEncoder();
    }
}

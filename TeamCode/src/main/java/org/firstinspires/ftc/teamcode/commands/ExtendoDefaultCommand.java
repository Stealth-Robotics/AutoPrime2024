package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem.ExtendoMode;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class ExtendoDefaultCommand extends CommandBase {
    private final ElevatorSubsystem elevator;
    private final ExtendoSubsystem extendo;

    private final DoubleSupplier leftTrigger;
    private final DoubleSupplier rightTrigger;

    public ExtendoDefaultCommand(ExtendoSubsystem extendo, ElevatorSubsystem elevator, DoubleSupplier leftTrigger, DoubleSupplier rightTrigger) {
        this.elevator = elevator;
        this.extendo = extendo;

        this.leftTrigger = leftTrigger;
        this.rightTrigger = rightTrigger;

        addRequirements(elevator, extendo);
    }

    @Override
    public void execute() {
        if (Math.abs(elevator.getPosition()) < 0.1) {
            extendo.setMode(ExtendoMode.MANUAL);
            extendo.setPower(leftTrigger.getAsDouble() - rightTrigger.getAsDouble());
        }
    }
}

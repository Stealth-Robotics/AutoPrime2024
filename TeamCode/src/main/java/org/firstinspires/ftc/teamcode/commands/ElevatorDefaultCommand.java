package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem.ElevatorMode;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class ElevatorDefaultCommand extends CommandBase {
    private final ElevatorSubsystem elevator;
    private final ExtendoSubsystem extendo;

    private final DoubleSupplier leftTrigger;
    private final DoubleSupplier rightTrigger;

    public ElevatorDefaultCommand(ElevatorSubsystem elevator, ExtendoSubsystem extendo, DoubleSupplier leftTrigger, DoubleSupplier rightTrigger) {
        this.elevator = elevator;
        this.extendo = extendo;

        this.leftTrigger = leftTrigger;
        this.rightTrigger = rightTrigger;

        addRequirements(elevator, extendo);
    }

    @Override
    public void execute() {
        if (Math.abs(extendo.getPosition()) < 0.1) {
            elevator.setMode(ElevatorMode.MANUAL);
            elevator.setElevatorPower(leftTrigger.getAsDouble() - rightTrigger.getAsDouble());
        }
    }
}

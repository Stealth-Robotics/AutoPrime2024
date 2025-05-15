package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem.ClawState;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem.*;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class ElevatorDefaultCommand extends CommandBase {
    private final ElevatorSubsystem elevator;
    private final ExtendoSubsystem extendo;

    private final DoubleSupplier triggers;
    private boolean holdPosition = false;

    public ElevatorDefaultCommand(ElevatorSubsystem elevator, ExtendoSubsystem extendo, DoubleSupplier triggers) {
        this.elevator = elevator;
        this.extendo = extendo;

        this.triggers = triggers;

        addRequirements(elevator, extendo);
    }

    @Override
    public void execute() {
        if (Math.abs(triggers.getAsDouble()) > 0.05 && extendo.isHomed()) {
            elevator.setMode(ElevatorMode.MANUAL);
            elevator.setPower(triggers.getAsDouble());
            holdPosition = true;
        }
        else {
            if (holdPosition) {
                holdPosition = false;
                elevator.holdPosition();
            }
            elevator.setMode(ElevatorMode.PID);
        }
    }
}

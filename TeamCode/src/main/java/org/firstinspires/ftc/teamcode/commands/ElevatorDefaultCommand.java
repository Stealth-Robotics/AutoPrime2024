package org.firstinspires.ftc.teamcode.commands;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.button.Trigger;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem.ClawState;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem.*;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

@Config
public class ElevatorDefaultCommand extends CommandBase {
    private final ElevatorSubsystem elevator;
    private final ExtendoSubsystem extendo;

    private final DoubleSupplier triggers;

    public static double manualSpeed = 0.2;

    public ElevatorDefaultCommand(ElevatorSubsystem elevator, ExtendoSubsystem extendo, DoubleSupplier triggers) {
        this.elevator = elevator;
        this.extendo = extendo;
        this.triggers = triggers;

        addRequirements(elevator);
    }

    @Override
    public void execute() {
        if (Math.abs(triggers.getAsDouble()) > 0.1 && extendo.isHomed()) {
            elevator.setPosition(elevator.getPositionPercentage() + triggers.getAsDouble() * manualSpeed);
        }

        if (Math.abs(elevator.getPosition()) <= 10.0)
            elevator.resetEncoder();
    }
}

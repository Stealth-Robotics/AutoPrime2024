package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

public class ExtendoDefaultCommand extends CommandBase {
    private final ElevatorSubsystem elevator;
    private final ExtendoSubsystem extendo;

    public ExtendoDefaultCommand(ElevatorSubsystem elevator, ExtendoSubsystem extendo) {
        this.elevator = elevator;
        this.extendo = extendo;
    }
}

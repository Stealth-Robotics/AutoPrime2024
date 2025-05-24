package org.firstinspires.ftc.teamcode.commands;
import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

public class ResetElevatorCommand extends CommandBase {
    final ElevatorSubsystem elevator;

    public ResetElevatorCommand(ElevatorSubsystem elevator) {
        this.elevator = elevator;
        addRequirements(elevator);
    }

    @Override
    public void initialize() {
        elevator.downSlowForReset();
    }

    @Override
    public void end(boolean interrupted) {
        elevator.completeReset();
    }

    @Override
    public boolean isFinished() {
        return elevator.isStalled();
    }
}

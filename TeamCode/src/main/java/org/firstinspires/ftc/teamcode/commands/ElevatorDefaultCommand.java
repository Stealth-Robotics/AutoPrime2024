package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

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

    private final DoubleSupplier leftTrigger;
    private final DoubleSupplier rightTrigger;

    private boolean resetAfterManualControl;
    private final double zeroThreshold = 50;
    private final double zeroPowerConstant = -0.1;

    public ElevatorDefaultCommand(ElevatorSubsystem elevator, ExtendoSubsystem extendo, DoubleSupplier leftTrigger, DoubleSupplier rightTrigger) {
        this.elevator = elevator;
        this.extendo = extendo;

        this.leftTrigger = leftTrigger;
        this.rightTrigger = rightTrigger;

        addRequirements(elevator, extendo);
    }

    @Override
    public void execute() {
        if (leftTrigger.getAsDouble() > 0.05 || rightTrigger.getAsDouble() > 0.05) {
            resetAfterManualControl = true;

            elevator.setMode(ElevatorMode.MANUAL);
            elevator.setPower(leftTrigger.getAsDouble() - rightTrigger.getAsDouble());
        }
        else if (resetAfterManualControl) {
            if (elevator.getPosition() < zeroThreshold)
                elevator.resetEncoder();

            elevator.setPower(zeroPowerConstant);
            resetAfterManualControl = false;
        }

        if (elevator.getLimitSwitch()) {
            elevator.resetEncoder();
            elevator.setPosition(ElevatorPosition.HOME);
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            elevator.setMode(ElevatorMode.PID);
        }
    }
}

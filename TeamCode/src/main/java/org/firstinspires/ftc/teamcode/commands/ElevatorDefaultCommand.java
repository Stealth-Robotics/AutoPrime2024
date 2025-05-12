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
    private final ClawSubsystem claw;

    private final DoubleSupplier leftTrigger;
    private final DoubleSupplier rightTrigger;

    private final BooleanSupplier highPreset;
    private final BooleanSupplier lowPreset;

    private boolean resetAfterManualControl;
    private final double zeroThreshold = 50;
    private final double zeroPowerConstant = -0.1;

    public ElevatorDefaultCommand(ElevatorSubsystem elevator, ExtendoSubsystem extendo, ClawSubsystem claw, DoubleSupplier leftTrigger, DoubleSupplier rightTrigger, BooleanSupplier highPreset, BooleanSupplier lowPreset) {
        this.elevator = elevator;
        this.extendo = extendo;
        this.claw = claw;

        this.leftTrigger = leftTrigger;
        this.rightTrigger = rightTrigger;

        this.highPreset = highPreset;
        this.lowPreset = lowPreset;

        addRequirements(elevator, extendo, claw);
    }

    @Override
    public void execute() {
        if (elevator.getHold()) {
            elevator.holdPosition();
            return;
        }

        if (claw.getState() == ClawState.CLOSED) {
            //Presets for specimens
            if (highPreset.getAsBoolean()) {
                elevator.setUsePID(true);
                elevator.setPosition(ElevatorPosition.HIGH_CHAMBER);
            }
            else if (lowPreset.getAsBoolean()) {
                elevator.setUsePID(true);
                elevator.setPosition(ElevatorPosition.LOW_CHAMBER);
            }
        }
        else {
            //Presets for samples
            if (highPreset.getAsBoolean()) {
                elevator.setUsePID(true);
                elevator.setPosition(ElevatorPosition.HIGH_BUCKET);
            }
            else if (lowPreset.getAsBoolean()) {
                elevator.setUsePID(true);
                elevator.setPosition(ElevatorPosition.LOW_BUCKET);
            }
        }

        if (leftTrigger.getAsDouble() > 0.05 || rightTrigger.getAsDouble() > 0.05) {
            resetAfterManualControl = true;

            elevator.setUsePID(false);
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
}

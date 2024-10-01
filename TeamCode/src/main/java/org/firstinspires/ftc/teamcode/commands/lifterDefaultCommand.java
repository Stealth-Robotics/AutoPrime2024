package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;

public class lifterDefaultCommand extends CommandBase {
    private final BooleanSupplier input;
    private final LifterSubsystem lifter;

    public lifterDefaultCommand (LifterSubsystem lifter, BooleanSupplier input){
        this.lifter = lifter;
        this.input = input;
    }
    @Override
    public void execute(){
        if (input.getAsBoolean()){
            lifter.MoveArm(10);
        } else lifter.MoveArm(0);
    }
}

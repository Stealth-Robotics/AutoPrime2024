package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;

public class lifterDefaultCommand extends CommandBase {
    private final BooleanSupplier input1;
    private final BooleanSupplier input2;
    private final BooleanSupplier input3;
    private final BooleanSupplier input4;
    private final BooleanSupplier input5;
    private final LifterSubsystem lifter;

    public lifterDefaultCommand (LifterSubsystem lifter, BooleanSupplier input1, BooleanSupplier input2, BooleanSupplier input3, BooleanSupplier input4, BooleanSupplier input5){
        this.lifter = lifter;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        this.input5 = input5;
        addRequirements(lifter);
        lifter.resetEncoder();
    }
    @Override
    public void execute(){
        if (input1.getAsBoolean()){
            lifter.moveArm(0.15);
        } else if (input2.getAsBoolean()){
            lifter.moveArm(0.4);
        } else if (input3.getAsBoolean()){
            lifter.moveArm(0.95);
        } else if (input4.getAsBoolean()){
            lifter.moveArm(0.5);
        } else if (input5.getAsBoolean()){
            lifter.moveArm(0);
        }
    }
}

package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;

public class lifterDefaultCommand extends CommandBase {
    private final BooleanSupplier input1;
    private final BooleanSupplier input2;
    private final BooleanSupplier input3;
    private final BooleanSupplier input4;
    private final LifterSubsystem lifter;
    private final ClawSubsystem claw;

    public lifterDefaultCommand (LifterSubsystem lifter, ClawSubsystem claw, BooleanSupplier input1, BooleanSupplier input2, BooleanSupplier input3, BooleanSupplier input4){
        this.lifter = lifter;
        this.claw = claw;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.input4 = input4;
        addRequirements(lifter);
        lifter.resetEncoder();
    }
    @Override
    public void execute(){
        if(claw.getPos() == claw.clawClosed){ // Changes functionality based on claw state
            if (input3.getAsBoolean()){
                lifter.moveArm(0.95); // High rung
            } else if (input2.getAsBoolean()){
                lifter.moveArm(0.15); // Low rung
            } else if (input1.getAsBoolean()){
                if(lifter.getPosition() > 0.8){ // Above high rung
                    new placeSpecimenCommand(lifter, claw, 0.9); // High rung specimen position
                } else if (lifter.getPosition() > 0.1) { // Above low rung
                    new placeSpecimenCommand(lifter, claw, 0.1); // Low rung specimen position
                }
            }
        } else {
            if (input3.getAsBoolean()){
                lifter.moveArm(0.95); // High bucket
            } else if (input2.getAsBoolean()){
                lifter.moveArm(0.4); // Low bucket
            } else if (input1.getAsBoolean()){
                lifter.moveArm(0.15); // Claw intake height
            }
        }
        /*if (input1.getAsBoolean()){
            lifter.moveArm(0.15);
        } else if (input2.getAsBoolean()){
            lifter.moveArm(0.4);
        } else if (input3.getAsBoolean()){
            lifter.moveArm(0.95);
        } else if (input4.getAsBoolean()){
            lifter.moveArm(0.5);
        }*/
    }
}

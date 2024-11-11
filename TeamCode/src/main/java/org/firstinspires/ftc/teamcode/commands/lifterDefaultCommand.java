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
    private final DoubleSupplier input4;
    private final DoubleSupplier input5;
    private final LifterSubsystem lifter;
    private final ClawSubsystem claw;
    private final double manualControlIncrement = 300;
    private final double zeroThreshold = 50;

    public lifterDefaultCommand (LifterSubsystem lifter, ClawSubsystem claw, BooleanSupplier input1, BooleanSupplier input2, BooleanSupplier input3, DoubleSupplier input4, DoubleSupplier input5){
        this.lifter = lifter;
        this.claw = claw;
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
        if(claw.getPos() == claw.clawClosed){ // Changes functionality based on claw state
            if (input3.getAsBoolean()){
                lifter.setUsePID(true);
                lifter.moveArm(0.65); // High rung
            } else if (input2.getAsBoolean()){
                lifter.setUsePID(true);
                lifter.moveArm(0.35); // Low rung
            } else if (input1.getAsBoolean()){
                if(lifter.getPosition() > 0.6){ // Above high rung
                    lifter.setUsePID(true);
                    new placeSpecimenCommand(lifter, claw, 0.6); // High rung specimen position
                } else if (lifter.getPosition() > 0.3) { // Above low rung
                    lifter.setUsePID(true);
                    new placeSpecimenCommand(lifter, claw, 0.3); // Low rung specimen position
                }
            }
        } else {
            if (input3.getAsBoolean()){
                lifter.setUsePID(true);
                lifter.moveArm(1); // High bucket
            } else if (input2.getAsBoolean()){
                lifter.setUsePID(true);
                lifter.moveArm(0.4); // Low bucket
            } else if (input1.getAsBoolean()){
                lifter.setUsePID(true);
                lifter.moveArm(0); // Claw intake height
            }
        }
        /*if (input4.getAsBoolean()){
            //lifter.moveArm((lifter.getPosition() + manualControlIncrement) / lifter.maxHeight);
        }*/
        if(input4.getAsDouble()>0.05 || input5.getAsDouble() > 0.05) {
            lifter.setUsePID(false);
            lifter.setPower(input4.getAsDouble());
            lifter.setPower(-input5.getAsDouble());
        }
        /*lifter.moveArm((lifter.getPosition()+manualControlIncrement*input4.getAsDouble()) / lifter.maxHeight);
        if (input5.getAsBoolean()){
            lifter.moveArm((lifter.getPosition() - manualControlIncrement) / lifter.maxHeight);
        }*/
        if ((input5.getAsDouble()<0.05) && (input4.getAsDouble()<0.05)){
            if(lifter.getPosition() < zeroThreshold){
                //new zeroLifterCommand(lifter);
            } else {
                //lifter.setPower(0);
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

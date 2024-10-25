package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;

import java.util.function.BooleanSupplier;

public class zeroLifterCommand extends CommandBase {
    private LifterSubsystem lifter;

    public zeroLifterCommand(LifterSubsystem lifter){
        this.lifter = lifter;
    }
    @Override
    public void execute(){
        lifter.resetEncoder();
        lifter.moveArm(0);
    }
    public boolean isFinished(){return true;}
}

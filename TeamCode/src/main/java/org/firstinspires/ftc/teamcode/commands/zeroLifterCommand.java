package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;

public class zeroLifterCommand extends CommandBase {
    private ElevatorSubsystem lifter;

    public zeroLifterCommand(ElevatorSubsystem lifter){
        this.lifter = lifter;
    }
    @Override
    public void execute(){
        lifter.resetEncoder();
        lifter.moveArm(0);
    }
    public boolean isFinished(){return true;}
}

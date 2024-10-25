package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;

public class toggleClawCommand extends CommandBase {
    ClawSubsystem claw;
    public toggleClawCommand(ClawSubsystem claw){
        this.claw = claw;
    }

    @Override
    public void execute(){
        if(claw.getPos() == 0){claw.setPos(0.5);}
        else claw.setPos(0);
    }
    public boolean isFinished() {return true;}
}

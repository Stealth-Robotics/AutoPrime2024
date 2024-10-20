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
        claw.setPos(1-claw.getPos());
    }
}

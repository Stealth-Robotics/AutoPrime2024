package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;

public class togglePanTiltCommand extends CommandBase {
    PanSubsystem pan;
    public togglePanTiltCommand(PanSubsystem pan){
        this.pan = pan;
    }
    @Override
    public void execute(){
        if(pan.getPos() == pan.in){
            pan.setPos(pan.out);
        } else pan.setPos(pan.in);
    }

    public boolean isFinished() {return  true;}
}

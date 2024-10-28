package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;

public class togglePanTiltCommand extends CommandBase {
    LifterPanSubsystem pan;
    public togglePanTiltCommand(LifterPanSubsystem pan){
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

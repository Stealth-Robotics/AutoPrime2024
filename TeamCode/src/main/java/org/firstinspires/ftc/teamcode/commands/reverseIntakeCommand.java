package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class reverseIntakeCommand extends CommandBase {
    IntakeSubsystem intake;
    public reverseIntakeCommand(IntakeSubsystem intake){
        this.intake = intake;
    }
    @Override
    public void execute(){
        intake.intakeDirection = -intake.intakeDirection;
        intake.setPower(intake.intakeDirection);
    }
    public boolean isFinished()    {return (true);}
}

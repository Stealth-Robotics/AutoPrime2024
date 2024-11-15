package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

import java.util.function.BooleanSupplier;

//This command is not used. Reacher controlled by deploy and retract intake commands
public class reacherDefaultCommand extends CommandBase {
    private final ReacherSubsystem reacher;

    private final BooleanSupplier input;
    private final BooleanSupplier input2;

    public reacherDefaultCommand(ReacherSubsystem reacher, BooleanSupplier input, BooleanSupplier input2){
        this.reacher = reacher;
        this.input = input;
        this.input2 = input2;
        addRequirements(reacher);
        //reacher.resetEncoder();
    }

    @Override
    public void execute(){
        if (input.getAsBoolean()){
            reacher.setSetPoint(0.5);
        } else if (input2.getAsBoolean()) {
            reacher.setSetPoint(0);
        }
    }
}

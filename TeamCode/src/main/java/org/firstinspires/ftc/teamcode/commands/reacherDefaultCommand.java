package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

import java.util.function.BooleanSupplier;

public class reacherDefaultCommand extends CommandBase {
    private final ReacherSubsystem reacher;

    private final BooleanSupplier input;
    private final BooleanSupplier input2;

    public reacherDefaultCommand(ReacherSubsystem reacher, BooleanSupplier input, BooleanSupplier input2){
        this.reacher = reacher;
        this.input = input;
        this.input2=input2;
        addRequirements(reacher);
    }

    @Override
    public void execute(){
        if (input.getAsBoolean()){
            reacher.setPower(1);
        } else if (input2.getAsBoolean()){
            reacher.setPower(-1);
        } else {
            reacher.setPower(0);
        }
    }
}

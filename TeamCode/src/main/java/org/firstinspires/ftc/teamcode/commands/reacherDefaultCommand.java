package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

import java.util.function.BooleanSupplier;

public class reacherDefaultCommand extends CommandBase {
    private final ReacherSubsystem reacher;

    private final BooleanSupplier input;

    public reacherDefaultCommand(ReacherSubsystem reacher, BooleanSupplier input){
        this.reacher = reacher;
        this.input = input;
    }

    @Override
    public void execute(){
        if (input.getAsBoolean()){
            if (reacher.getPosition()<10){
                reacher.setPower(1);
            }
        } else {
            if (reacher.getPosition() > 0){
                reacher.setPower(-1);
            }
        }
    }
}

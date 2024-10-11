package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class handOffCommand extends CommandBase {
    LifterSubsystem lifter;
    ReacherSubsystem reacher;
    FlipperSubsystem flipper;
    IntakeSubsystem intake;
    DoubleSupplier inInput;
    DoubleSupplier outInput;
    private boolean in = true;

    public handOffCommand(LifterSubsystem lifter, ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, DoubleSupplier inInput, DoubleSupplier outInput){
        this.lifter = lifter;
        this.reacher = reacher;
        this.flipper = flipper;
        this.intake = intake;
        this.inInput = inInput;
        this.outInput = outInput;
        addRequirements(lifter, reacher, flipper, intake);
    }
    @Override
    public void execute(){
        if ((inInput.getAsDouble() > 0.1) && !in){ new retractIntakeCommand(reacher, flipper, intake); }
        else if ((outInput.getAsDouble() > 0.1) && in){ new deployIntakeCommand(reacher, flipper, intake);}
    }
}

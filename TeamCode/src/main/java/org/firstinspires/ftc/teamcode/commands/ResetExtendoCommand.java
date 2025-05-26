package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

public class ResetExtendoCommand extends CommandBase {
    private final ExtendoSubsystem extendo;

    public ResetExtendoCommand(ExtendoSubsystem extendo) {
        this.extendo = extendo;
        addRequirements(extendo);
    }

    @Override
    public void initialize() {
        extendo.downSlowForReset();
    }

    @Override
    public void end(boolean interrupted) {
        extendo.completeReset();
    }

    @Override
    public boolean isFinished() {
        return extendo.isStalled();
    }
}

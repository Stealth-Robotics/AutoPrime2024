package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class IntakeSpitCommand extends SequentialCommandGroup {
    public IntakeSpitCommand(IntakeSubsystem intake, ExtendoSubsystem extendo) {
        addCommands(
                new InstantCommand(intake::outtake),
                new WaitCommand(1000),
                new ConditionalCommand(new InstantCommand(intake::stop), new InstantCommand(intake::intake), extendo::isHomed)
        );
        addRequirements(intake);
    }
}

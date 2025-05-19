package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem.ExtendoPosition;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;

public class DeployIntakeCommand extends SequentialCommandGroup {
    public DeployIntakeCommand(ExtendoSubsystem extendo, IntakeSubsystem intake) {
        addCommands(
                new InstantCommand(intake::intake),
                new InstantCommand(() -> extendo.setPosition(ExtendoPosition.DEPLOYED)),
                new WaitCommand(500),
                new InstantCommand(intake::wristDown)
        );

        addRequirements(extendo, intake);
    }
}

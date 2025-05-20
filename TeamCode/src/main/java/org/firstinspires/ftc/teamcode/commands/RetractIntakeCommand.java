package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem.ExtendoPosition;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;

public class RetractIntakeCommand extends SequentialCommandGroup {
    public RetractIntakeCommand(ExtendoSubsystem extendo, IntakeSubsystem intake, ElevatorSubsystem elevator, PanSubsystem pan) {
        if (elevator.getPosition() <= 10) {
            addCommands(
                    new InstantCommand(() -> extendo.setPosition(ExtendoPosition.PAST_HOME)),
                    new InstantCommand(intake::wristUp),
                    new InstantCommand(intake::stop),
                    new InstantCommand(pan::home),
                    new WaitCommand(1000),
                    new InstantCommand(extendo::resetEncoder),
                    new InstantCommand(() -> extendo.setPosition(ExtendoPosition.TRANSFER)),
                    new WaitCommand(200),
                    new InstantCommand(intake::outtake),
                    new WaitCommand(500),
                    new InstantCommand(intake::wristHome),
                    new WaitCommand(500),
                    new InstantCommand(() -> extendo.setPosition(ExtendoPosition.HOME)),
                    new InstantCommand(intake::stop)
            );
        }
        else {
            addCommands(
                    new InstantCommand(() -> extendo.setPosition(ExtendoPosition.PAST_HOME)),
                    new InstantCommand(intake::wristHome),
                    new InstantCommand(intake::stop),
                    new InstantCommand(pan::home),
                    new WaitCommand(1000),
                    new InstantCommand(extendo::resetEncoder),
                    new InstantCommand(() -> extendo.setPosition(ExtendoPosition.HOME))
            );
        }

        addRequirements(extendo, intake, elevator, pan);
    }
}

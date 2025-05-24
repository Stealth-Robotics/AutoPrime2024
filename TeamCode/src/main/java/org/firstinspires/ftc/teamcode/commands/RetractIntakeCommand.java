package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;

import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem.ExtendoPosition;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;

public class RetractIntakeCommand extends SequentialCommandGroup {
    public RetractIntakeCommand(ExtendoSubsystem extendo, IntakeSubsystem intake, ElevatorSubsystem elevator, PanSubsystem pan) {
        addCommands(
                new InstantCommand(() -> extendo.setIsHomed(true)),
                new InstantCommand(intake::wristUp),
                new InstantCommand(() -> extendo.setPosition(ExtendoPosition.TRANSFER)),
                new InstantCommand(intake::stop),
                new InstantCommand(pan::home),
                new WaitUntilCommand(extendo::atPosition), //Wait until extendo is fully in position
                new InstantCommand(intake::outtake),
                new WaitUntilCommand(() -> intake.getColor().equals(IntakeSubsystem.Color.BLACK)), //Color sensor no longer detects sample
                new InstantCommand(intake::stop),
                new InstantCommand(intake::wristHome),
                new InstantCommand(() -> extendo.setPosition(ExtendoPosition.PAST_HOME)),
                new WaitCommand(500),
                new InstantCommand(extendo::resetEncoder),
                new InstantCommand(() -> extendo.setPosition(ExtendoPosition.HOME))
        );

        addRequirements(extendo, intake, elevator, pan);
    }
}

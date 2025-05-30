package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.ConditionalCommand;
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
    public RetractIntakeCommand(ExtendoSubsystem extendo, IntakeSubsystem intake, ElevatorSubsystem elevator, PanSubsystem pan, boolean isAuto) {
        addCommands(
                new ConditionalCommand(
                        new SequentialCommandGroup(
                                new InstantCommand(() -> extendo.setIsHomed(true)),
                                new InstantCommand(pan::home),
                                new InstantCommand(intake::wristHome),
                                new InstantCommand(intake::stop),
                                new InstantCommand(intake::wristUp),
                                new InstantCommand(() -> extendo.setPosition(ExtendoPosition.TRANSFER)),
                                new WaitUntilCommand(extendo::atPosition), //Wait until extendo is fully in position
                                new InstantCommand(() -> intake.setIntakeSpeed(0.25)),
                                new WaitUntilCommand(() -> intake.getColor().equals(IntakeSubsystem.Color.BLACK)), //Color sensor no longer detects sample
                                new WaitCommand(500), //Extra pause to insure consistency
                                new InstantCommand(intake::wristHome),
                                new InstantCommand(intake::stop),
                                new ResetExtendoCommand(extendo)
                        ),
                        new SequentialCommandGroup(
                                new InstantCommand(() -> extendo.setIsHomed(true)),
                                new InstantCommand(intake::wristHome),
                                new InstantCommand(intake::stop),
                                new ResetExtendoCommand(extendo)
                        ),
                        () -> isAuto || elevator.isHomed()
                )
        );

        addRequirements(extendo, intake, pan);
    }
}

package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

public class groundIntakeCommand extends SequentialCommandGroup {
    public groundIntakeCommand(IntakeSubsystem intake, ReacherSubsystem reacher, FlipperSubsystem flipper, double outPos){
        addCommands(
                new InstantCommand(()->flipper.goToPos(0.85)),
                new InstantCommand(()->intake.setPower(-1)),
                new InstantCommand(()->reacher.setSetPoint(outPos))
        );
    }
}

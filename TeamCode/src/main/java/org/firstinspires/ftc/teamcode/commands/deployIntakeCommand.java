package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSensorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

public class deployIntakeCommand extends SequentialCommandGroup {

    public deployIntakeCommand(ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, boolean in) {
        double intakePower;
        if(in){intakePower = 1;}
        else {intakePower = -1;}
            addCommands(
                    new InstantCommand(() -> flipper.goToPos(0.55)),
                    new InstantCommand(() -> reacher.setSetPoint(0.9)),
                    new WaitCommand(1000),
                    new InstantCommand(() -> flipper.goToPos(0.85)),
                    new InstantCommand(() -> intake.setPower(intakePower)),
                    new WaitCommand(0)
            );
    }
}

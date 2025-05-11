package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

public class deployIntakeCommand extends SequentialCommandGroup {

    public deployIntakeCommand(ExtendoSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, boolean in) {
        double intakePower;
        if(in){intakePower= -1;}
        else {intakePower = 1;}
        addCommands(
                new InstantCommand(() -> flipper.goToPos(0.55)),
                new InstantCommand(() -> reacher.setSetPoint(1)), //Reacher distance
                new WaitCommand(1000), //Wait time before flipping
                new InstantCommand(() -> flipper.goToPos(0.84)),
                new InstantCommand(() -> intake.setPower(intakePower))
        );
    }
    public deployIntakeCommand(ExtendoSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, boolean in, double setPoint1, double setpoint2){
        double intakePower;
        if(in){intakePower=-1;}
        else {intakePower = 1;}
        addCommands(
                new InstantCommand(() -> flipper.goToPos(0.55)),
                new InstantCommand(() -> reacher.setSetPoint(setPoint1)),
                new WaitCommand(1000), //Wait time before flipping
                new InstantCommand(() -> flipper.goToPos(0.84)),
                new InstantCommand(() -> intake.setPower(intakePower)),
                new InstantCommand(() -> reacher.setSetPoint(setpoint2))
        );
    }
}

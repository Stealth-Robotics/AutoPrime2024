package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

public class retractIntakeCommand extends SequentialCommandGroup {
    public retractIntakeCommand(ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake){
        addCommands(
                new InstantCommand(()-> flipper.goToPos(0.2)),
                //new InstantCommand(()-> reacher.setPower(-0.1)),
                new InstantCommand(()-> intake.setPower(0))
        );
    }
}

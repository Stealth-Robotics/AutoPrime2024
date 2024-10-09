package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

public class retractIntakeCommand extends CommandBase {
    ReacherSubsystem reacher;
    FlipperSubsystem flipper;
    IntakeSubsystem intake;

    public retractIntakeCommand(ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake){
        this.reacher = reacher;
        this.flipper = flipper;
        this.intake = intake;
    }
    @Override
    public void execute(){
        new SequentialCommandGroup(
                new InstantCommand(()-> flipper.goToPos(0)),
                new InstantCommand(()-> reacher.setPower(-0.1)),
                new InstantCommand(()-> intake.setPower(0))
        );
    }

    public boolean isFinished(){
        return(reacher.getPosition() < 10);
    }
}

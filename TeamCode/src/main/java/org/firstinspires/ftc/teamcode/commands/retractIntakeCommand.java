package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

public class retractIntakeCommand extends SequentialCommandGroup {
    public retractIntakeCommand(ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, LifterSubsystem lifter, LifterPanSubsystem pan) {
    //public retractIntakeCommand(ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, LifterPanSubsystem pan) {
        if (lifter.getPosition() < 10) {
            addCommands(
                new InstantCommand(()-> reacher.setSetPoint(-0.5)),
                new InstantCommand(()-> flipper.goToPos(0.15)),
                new InstantCommand(()-> intake.setPower(0)),
                new InstantCommand(()-> pan.setPos(pan.in)),
                new WaitCommand(1000),//Wait time before handoff is attempted
                new InstantCommand(()-> reacher.resetEncoder()),
                new InstantCommand(()-> reacher.setSetPoint(0)),
                new InstantCommand(()-> flipper.goToPos(0.25)),
                new InstantCommand(()-> intake.setPower(1)),
                new WaitCommand(1000),//Wait time before handoff stops and intake moves out of the way
                new InstantCommand(()-> intake.setPower(0)),
                new InstantCommand(()-> flipper.goToPos(0.35))
            );
        } else { //Doesn't attempt to handoff if the lifter is not all the way down
            addCommands(
                new InstantCommand(()-> reacher.setSetPoint(0)),
                new InstantCommand(()-> flipper.goToPos(0.2)),
                new InstantCommand(()-> intake.setPower(0))
            );
        }
    }
}

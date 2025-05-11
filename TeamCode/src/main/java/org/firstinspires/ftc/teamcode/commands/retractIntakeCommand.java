package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PanSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;

public class retractIntakeCommand extends SequentialCommandGroup {
    public retractIntakeCommand(ExtendoSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, ElevatorSubsystem lifter, PanSubsystem pan) {
    //public retractIntakeCommand(ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, LifterPanSubsystem pan) {
        if (lifter.getPosition() < 10) {
            addCommands(
                new InstantCommand(()-> reacher.setSetPoint(-0.5)),
                new InstantCommand(()-> flipper.goToPos(0.15)),
                new InstantCommand(()-> intake.setPower(0)),
                new InstantCommand(()-> pan.setPos(pan.in)),
                new WaitCommand(400),//Wait time before handoff is attempted
                new InstantCommand(()-> flipper.goToPos(0.25)),
                new InstantCommand(()-> intake.setPower(1)),
                new WaitCommand(500),//Wait time before handoff stops and intake moves out of the way
                new InstantCommand(()-> reacher.resetEncoder()),
                new InstantCommand(()-> reacher.setSetPoint(0)),
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

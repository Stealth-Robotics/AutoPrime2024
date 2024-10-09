package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class intakeDefaultCommand extends CommandBase {
    ReacherSubsystem reacher;
    FlipperSubsystem flipper;
    IntakeSubsystem intake;
    DoubleSupplier trigger1;
    DoubleSupplier trigger2;
    BooleanSupplier button;
    public int direction = 1;
    public intakeDefaultCommand (ReacherSubsystem reacher, FlipperSubsystem flipper, IntakeSubsystem intake, DoubleSupplier trigger1, DoubleSupplier trigger2, BooleanSupplier button){
        this.reacher = reacher;
        this.flipper = flipper;
        this.intake = intake;
        this.trigger1 = trigger1;
        this.trigger2 = trigger2;
        this.button = button;
    }
    @Override
    public void execute(){
        if (trigger2.getAsDouble() > 0.1) {
            new SequentialCommandGroup(
                    new InstantCommand(()-> flipper.goToPos(0)),
                    new InstantCommand(()-> reacher.setPower(-0.1)),
                    new InstantCommand(()-> intake.setPower(0))
            );
        } else if (trigger1.getAsDouble() > 0.1) {new SequentialCommandGroup(
                new InstantCommand(()-> reacher.setPower(0.1)),
                new InstantCommand(()-> flipper.goToPos(1)),
                new InstantCommand(()-> intake.setPower(0.5 * direction))
        );
        }
        if(button.getAsBoolean()){
            direction = 1-direction;
        }
    }
}

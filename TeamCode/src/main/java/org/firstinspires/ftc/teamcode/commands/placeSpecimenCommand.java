package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
//This command is not used
public class placeSpecimenCommand extends SequentialCommandGroup {
    public placeSpecimenCommand(LifterSubsystem lifter, ClawSubsystem claw, double lifterPosition){
        addCommands(
                new InstantCommand(()->lifter.moveArm((lifter.getPosition()-100)/lifterPosition)),
                new WaitCommand(1000),
                new InstantCommand(()->claw.setPos(claw.clawClosed))
        );
    }
}

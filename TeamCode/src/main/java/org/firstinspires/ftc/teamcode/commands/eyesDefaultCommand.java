package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.EyesSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class eyesDefaultCommand extends CommandBase {
    Mecanum mecanum;
    EyesSubsystem eyes;
    BooleanSupplier input1;
    BooleanSupplier input2;
    DoubleSupplier angle;
    public eyesDefaultCommand(Mecanum mecanum, EyesSubsystem eyes, BooleanSupplier input1, BooleanSupplier input2, DoubleSupplier angle){
        this.mecanum = mecanum;
        this.eyes = eyes;
        this.input1 = input1;
        this.input2 = input2;
        this.angle = angle;
        addRequirements(eyes);
    }
    @Override
    public void execute(){ //Enable the other code if you want eyes to run off A and B rather than stick position
        eyes.pointEyes((angle.getAsDouble()+1)/2);
        //if (input1.getAsBoolean()) { eyes.pointEyes(0); }
        //if (input2.getAsBoolean()) { eyes.pointEyes(1); }
    }
}

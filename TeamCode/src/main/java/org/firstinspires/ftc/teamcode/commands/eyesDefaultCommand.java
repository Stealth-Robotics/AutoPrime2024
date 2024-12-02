package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.EyebrowSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.EyesSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class eyesDefaultCommand extends CommandBase {
    Mecanum mecanum;
    EyesSubsystem eyes;
    EyebrowSubsystem eyebrows;
    BooleanSupplier input1;
    DoubleSupplier angle;
    DoubleSupplier cross;

    public eyesDefaultCommand(Mecanum mecanum, EyesSubsystem eyes, EyebrowSubsystem eyebrows, BooleanSupplier input1, DoubleSupplier angle, DoubleSupplier cross) {
        this.mecanum = mecanum;
        this.eyes = eyes;
        this.eyebrows = eyebrows;
        this.input1 = input1;
        this.angle = angle;
        addRequirements(eyes);
    }

    @Override
    public void execute() { //Enable the other code if you want eyes to run off A and B rather than stick position
        eyes.pointEyes((angle.getAsDouble() + 1) / 2);
        if (input1.getAsBoolean()) {
            eyebrows.setEyebrowPosition(0, 0);
        } else {
            eyebrows.setEyebrowPosition(1, 1);
            if ((angle.getAsDouble() == 0)) {
                eyes.rightEyeToPos(0.4);
                eyes.leftEyeToPos(0.8);
            }
        }
    }
}

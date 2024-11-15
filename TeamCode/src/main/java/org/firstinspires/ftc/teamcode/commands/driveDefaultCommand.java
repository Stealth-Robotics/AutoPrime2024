package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.Mecanum;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

//Don't touch this code please
public class driveDefaultCommand extends CommandBase {
    private final Mecanum mecanum;
    private final BooleanSupplier y;
    private final DoubleSupplier leftStickX;
    private final DoubleSupplier leftStickY;
    private final DoubleSupplier rightStickX;
    private final BooleanSupplier leftBumper;

    public driveDefaultCommand(Mecanum mecanum, DoubleSupplier leftStickX, DoubleSupplier leftStickY, DoubleSupplier rightStickX, BooleanSupplier y, BooleanSupplier leftBumper){
        this.mecanum = mecanum;
        this.leftStickX = leftStickX;
        this.leftStickY = leftStickY;
        this.rightStickX = rightStickX;
        this.leftBumper = leftBumper;
        this.y = y;
        addRequirements(mecanum);
    }

    @Override
    public void execute()
    {
        mecanum.drive(leftStickX.getAsDouble(), leftStickY.getAsDouble(), rightStickX.getAsDouble(), leftBumper.getAsBoolean());
        if (y.getAsBoolean()) {
            mecanum.resetHeading();
        }
    }
}

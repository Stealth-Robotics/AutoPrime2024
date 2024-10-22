package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;

import java.util.function.DoubleSupplier;

public class driveDefaultCommand extends CommandBase {
    private final DriveSubsystem drive;
    private final DoubleSupplier leftStickX;
    private final DoubleSupplier leftStickY;
    private final DoubleSupplier rightStickX;

    public driveDefaultCommand(DriveSubsystem drive, DoubleSupplier leftStickX, DoubleSupplier leftStickY, DoubleSupplier rightStickX){
        this.drive = drive;
        this.leftStickX = leftStickX;
        this.leftStickY = leftStickY;
        this.rightStickX = rightStickX;
        addRequirements(drive);
    }

    @Override
    public void execute()
    {
        drive.drive(leftStickX.getAsDouble(), leftStickY.getAsDouble(), rightStickX.getAsDouble());
    }
}

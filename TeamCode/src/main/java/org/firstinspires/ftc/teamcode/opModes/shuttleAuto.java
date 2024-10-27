package org.firstinspires.ftc.teamcode.opModes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "shuttleAuto")
public class shuttleAuto extends StealthOpMode {
    DriveSubsystem driveSubsystem;
    LifterPanSubsystem panSubsystem;
    @Override
    public void initialize(){
    driveSubsystem = new DriveSubsystem(hardwareMap);
    panSubsystem = new LifterPanSubsystem(hardwareMap);
    }
    @Override
    public Command getAutoCommand(){
        return new SequentialCommandGroup();
    }
}

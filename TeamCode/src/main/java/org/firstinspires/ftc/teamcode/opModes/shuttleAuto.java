package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "shuttleAuto")
public class shuttleAuto extends StealthOpMode {
    DriveSubsystem driveSubsystem;
    LifterPanSubsystem panSubsystem;
    public void initialize(){

    }
}

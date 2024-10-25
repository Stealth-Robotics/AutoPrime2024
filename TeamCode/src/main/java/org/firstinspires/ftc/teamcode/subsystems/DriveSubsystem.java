package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

//import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.stealthrobotics.library.StealthSubsystem;

public class DriveSubsystem extends StealthSubsystem {

    public Follower follower;

    public DriveSubsystem(HardwareMap hardwareMap){
        follower = new Follower(hardwareMap);
    }

    public void startTeleopDrive(){
        follower.startTeleopDrive();
    }

    public void drive(double leftStickX, double leftStickY, double rightStickX){
        follower.setTeleOpMovementVectors(-leftStickY,-leftStickX,-rightStickX);
        follower.update();
    }


}
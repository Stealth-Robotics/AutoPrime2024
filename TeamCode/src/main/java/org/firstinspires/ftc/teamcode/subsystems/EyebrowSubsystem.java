package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class EyebrowSubsystem extends SubsystemBase {
    Servo eyebrowLeft;
    Servo eyebrowRight;
    public EyebrowSubsystem(HardwareMap hardwareMap){
        eyebrowLeft = hardwareMap.get(Servo.class, "leftBrow");
        eyebrowRight = hardwareMap.get(Servo.class, "rightBrow");
    }
    public void setEyebrowLeft(double pos){
        eyebrowLeft.setPosition(pos);
    }
    public void setEyebrowRight(double pos){
        eyebrowRight.setPosition(pos);
    }
    public void setEyebrowPosition(double lPos, double rPos){
        setEyebrowLeft(lPos);
        setEyebrowRight(rPos);
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class EyesSubsystem extends SubsystemBase {
    private static Servo leftEye;
    private static Servo rightEye;
    final double leftEyeMin = 0.5;
    final double rightEyeMin = 0.2;
    final double leftEyeMax = 0.8;
    final double rightEyeMax = 0.5;
    public EyesSubsystem(HardwareMap hardwareMap){
        leftEye = hardwareMap.get(Servo.class, "leftEye");
        rightEye = hardwareMap.get(Servo.class, "rightEye");
    }
    public void rightEyeToPos(double pos){
        rightEye.setPosition(pos);
    }
    public void leftEyeToPos(double pos){
        leftEye.setPosition(pos);
    }
    public void pointEyes(double angle){
        rightEyeToPos(rightEyeMin + (rightEyeMax-rightEyeMin) * angle);
        leftEyeToPos(leftEyeMin + (leftEyeMax-leftEyeMin) * angle);
    }
}

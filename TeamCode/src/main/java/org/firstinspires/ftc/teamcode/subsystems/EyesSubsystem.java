package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class EyesSubsystem extends SubsystemBase {
    private static Servo leftEye;
    private static Servo rightEye;
    final double leftEyeMin = 0.0;
    final double rightEyeMin = 0.0;
    final double leftEyeMax = 1;
    final double rightEyeMax = 1;
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
    public void pointEyes(double angle, double cross){
        rightEyeToPos(rightEyeMin + (rightEyeMax-rightEyeMin) * angle);
        leftEyeToPos(leftEyeMin + (leftEyeMax-leftEyeMin) * angle);
    }
}

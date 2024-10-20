package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawSubsystem extends SubsystemBase {
    private Servo clawServo;
    public ClawSubsystem (HardwareMap hardwareMap){
        clawServo = hardwareMap.get(Servo.class, "clawServo");
    }
    public double getPos(){
        return clawServo.getPosition();
    }
    public void setPos(double position){
        clawServo.setPosition(position);
    }
}

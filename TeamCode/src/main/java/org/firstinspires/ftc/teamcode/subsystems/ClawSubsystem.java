package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawSubsystem extends SubsystemBase {
    private Servo clawServo;
    private double pos;
    public final double clawClosed = 0;
    public final double clawOpen = 0.5;
    public ClawSubsystem (HardwareMap hardwareMap){
        clawServo = hardwareMap.get(Servo.class, "clawServo");
    }
    public double getPos(){
        return pos;
    }
    public void setPos(double position){
        clawServo.setPosition(position);
        pos = position;
    }
}

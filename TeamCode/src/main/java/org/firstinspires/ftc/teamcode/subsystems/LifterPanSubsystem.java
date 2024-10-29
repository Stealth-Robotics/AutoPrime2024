package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class LifterPanSubsystem extends SubsystemBase {
    private Servo panServo;
    private double pos;
    public final double in = 0.2;
    public final double out = 0;
    public LifterPanSubsystem(HardwareMap hardwareMap){
        panServo = hardwareMap.get(Servo.class,"panServo");
    }
    public void setPos(double position){
        panServo.setPosition(position);
        pos = position;
    }
    public double getPos(){
        return pos;
    }
}

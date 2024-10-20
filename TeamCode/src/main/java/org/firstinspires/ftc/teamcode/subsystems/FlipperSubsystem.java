package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlipperSubsystem extends SubsystemBase {
    private Servo flipperServo;
    public FlipperSubsystem(HardwareMap hardwareMap){
        flipperServo = hardwareMap.get(Servo.class, "flipperServo");
    }
    public void goToPos(double pos){
        flipperServo.setPosition(pos);
    }
    public double getPos() { return flipperServo.getPosition();}
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class IntakeSubsystem extends SubsystemBase {
    public Servo intakeServo;

    public IntakeSubsystem(HardwareMap hardwareMap){
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");
    }

    public void setPower(double power){
        intakeServo.setPosition(power);
    }
}

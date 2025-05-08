package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.arcrobotics.ftclib.command.Command;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.stealthrobotics.library.StealthSubsystem;

public class IntakeSubsystem extends StealthSubsystem {
    private final Servo intakeServo;
    private final Servo wristServo;

    private final ColorSensor colorSensor;

    public enum ColorList {
        RED,
        BLUE,
        YELLOW,
        BLACK
    }

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        wristServo = hardwareMap.get(Servo.class, "wristServo");

        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");
    }

    public void setWristPosition(double pos) {
        wristServo.setPosition(pos);
    }

    public double getWristPosition() {
        return wristServo.getPosition();
    }

    public void setIntakeSpeed(double speed) {
        intakeServo.setPosition((speed + 1) / 2);
    }

    public ColorList readSensorColor() {
        if ((colorSensor.red() > 75) || (colorSensor.blue() > 75) || getYellowAmount() > 75) {
            if (colorSensor.red() > colorSensor.blue()) {
                if (colorSensor.red() > getYellowAmount()) {
                    return ColorList.RED;
                }
                else {
                    return ColorList.YELLOW;
                }
            }
            else {
                return ColorList.BLUE;
            }
        }
        else {
            return ColorList.BLACK;
        }
    }

    private int getYellowAmount() {
        return (colorSensor.red() + colorSensor.green()) / 2;
    }

    @Override
    public void periodic() {
        telemetry.addData("detectedColor: ", readSensorColor());
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.stealthrobotics.library.StealthSubsystem;

@Config
public class IntakeSubsystem extends StealthSubsystem {
    private final Servo intakeServo;
    private final Servo wristServo;

    public static double WRIST_UP_POSITION = 0.2;
    public static double WRIST_HOME_POSITION = 0.7;
    public static double WRIST_DOWN_POSITION = 0.815;

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

    public void wristUp() {
        setWristPosition(WRIST_UP_POSITION);
    }

    public void wristHome() {
        setWristPosition(WRIST_HOME_POSITION);
    }

    public void wristDown() {
        setWristPosition(WRIST_DOWN_POSITION);
    }

    public void intake() {
        setIntakeSpeed(-1);
    }

    public void outtake() {
        setIntakeSpeed(1);
    }

    public void stop() {
        setIntakeSpeed(0);
    }

    public void setWristPosition(double pos) {
        wristServo.setPosition(pos);
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
        telemetry.addData("DetectedColor: ", readSensorColor());
    }
}

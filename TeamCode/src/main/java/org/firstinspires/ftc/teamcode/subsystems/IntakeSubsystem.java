package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.stealthrobotics.library.StealthSubsystem;

import java.util.ArrayList;

@Config
public class IntakeSubsystem extends StealthSubsystem {
    private final CRServo intakeServo;
    private final Servo wristServo;

    public static double WRIST_UP_POSITION = 0.2;
    public static double WRIST_HOME_POSITION = 0.7;
    public static double WRIST_TRAVEL_POSITION = 0.84;
    public static double WRIST_DOWN_POSITION = 0.86;

    //Color sensor tuning variables
    public static double DISTANCE_ACTIVATION_CONSTANT = 3.0;

    public static int RED_VS_BLUE_CONSTANT = 14;
    public static int RED_VS_GREEN_CONSTANT = 40;

    private final RevColorSensorV3 colorSensor;

    public enum Color {
        RED,
        BLUE,
        YELLOW,
        BLACK
    }

    private IntakeControl control = IntakeControl.MANUAL;

    public enum IntakeControl {
        MANUAL,
        AUTOMATIC
    }

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeServo = hardwareMap.get(CRServo.class, "intakeServo");
        wristServo = hardwareMap.get(Servo.class, "wristServo");
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
    }

    public void toggleControl() {
        if (control.equals(IntakeControl.MANUAL))
            control = IntakeControl.AUTOMATIC;
        else
            control = IntakeControl.MANUAL;
    }

    public IntakeControl getControlType() {
        return control;
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

    public void wristTravel() {
        setWristPosition(WRIST_TRAVEL_POSITION);
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

    private void setWristPosition(double pos) {
        wristServo.setPosition(pos);
    }

    public void setIntakeSpeed(double speed) {
        intakeServo.setPower(speed);
    }

    public Color getColor() {
        if (colorSensor.getDistance(DistanceUnit.INCH) < DISTANCE_ACTIVATION_CONSTANT) {
            if (colorSensor.red() > colorSensor.blue() - RED_VS_BLUE_CONSTANT) {
                if (colorSensor.red() > colorSensor.green() - RED_VS_GREEN_CONSTANT) {
                    return Color.RED;
                }
                else {
                    return Color.YELLOW;
                }
            }
            else {
                return Color.BLUE;
            }
        }
        else {
            return Color.BLACK;
        }
    }

    @Override
    public void periodic() {
        telemetry.addData("Detected Color", getColor());
        telemetry.addData("r", colorSensor.red());
        telemetry.addData("g", colorSensor.green());
        telemetry.addData("b", colorSensor.blue());
        telemetry.addData("Intake Control", control);
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.stealthrobotics.library.StealthSubsystem;

import java.util.ArrayList;

@Config
public class IntakeSubsystem extends StealthSubsystem {
    private final Servo intakeServo;
    private final Servo wristServo;

    public static double WRIST_UP_POSITION = 0.2;
    public static double WRIST_HOME_POSITION = 0.7;
    public static double WRIST_DOWN_POSITION = 0.84;

    //Color sensor tuning variables
    public static int BLUE_ACTIVATION = 78;
    public static int RED_ACTIVATION = 58;
    public static int RED_VS_BLUE_CONSTANT = 10;
    public static int RED_VS_GREEN_CONSTANT = 40;

    private final RevColorSensorV3 colorSensor;

    private final ArrayList<Color> recentColors = new ArrayList<>();
    public static int RECENT_COLOR_HISTORY_COUNT = 10;

    public enum Color {
        RED(0),
        BLUE(1),
        YELLOW(2),
        BLACK(3);

        private int index;
        Color(int index) {
            this.index = index;
        }

        int getIndex() { return index; }
    }

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        wristServo = hardwareMap.get(Servo.class, "wristServo");
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
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

    private Color readSensorColor() {
        if (colorSensor.red() > RED_ACTIVATION || colorSensor.blue() > BLUE_ACTIVATION) {
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

    private void addNewColor(Color c) {
        if (recentColors.size() < RECENT_COLOR_HISTORY_COUNT) {
            recentColors.add(c);
        }
        else {
            recentColors.remove(0);
            recentColors.add(c);
        }
    }

    public Color getColor() {
        if (recentColors.isEmpty())
            return Color.BLACK;
        else {
            //Return the most frequent color
            int[] freq = new int[4];
            for (Color color : recentColors) {
                freq[color.getIndex()]++;
            }

            int max = Integer.MIN_VALUE;
            int bestIndex = -1;
            for (int i = 0; i < 4; i++) {
                if (freq[i] > max) {
                    max = freq[i];
                    bestIndex = i;
                }
            }

            switch (bestIndex) {
                case 0: return Color.RED;
                case 1: return Color.BLUE;
                case 2: return Color.YELLOW;
                default: return Color.BLACK;
            }
        }
    }

    @Override
    public void periodic() {
        addNewColor(readSensorColor());

        telemetry.addData("Detected Color", getColor());
    }
}

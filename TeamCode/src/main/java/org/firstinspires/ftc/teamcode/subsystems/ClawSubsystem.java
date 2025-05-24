package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.stealthrobotics.library.StealthSubsystem;

@Config
public class ClawSubsystem extends StealthSubsystem {
    private final Servo clawServo;
    private ClawState state = ClawState.OPEN;

    public static double CLOSED_POS = 0.32;
    public static double OPEN_POS = 0.0;

    public enum ClawState {
        CLOSED,
        OPEN
    }

    public ClawSubsystem(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
    }

    public ClawState getState() {
        return state;
    }

    public void setState(ClawState state) {
        if (state == ClawState.CLOSED)
           clawServo.setPosition(CLOSED_POS);
        else
            clawServo.setPosition(OPEN_POS);
    }

    public void toggleState() {
        if (state == ClawState.CLOSED) {
            state = ClawState.OPEN;
            clawServo.setPosition(OPEN_POS);
        }
        else {
            state = ClawState.CLOSED;
            clawServo.setPosition(CLOSED_POS);
        }
    }

    @Override
    public void periodic() {
        telemetry.addData("ClawState: ", state);
    }
}

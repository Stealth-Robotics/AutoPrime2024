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

    public enum ClawState {
        CLOSED(0.4),
        OPEN(0.0);

        private final double setpoint;
        ClawState(double setpoint) {
            this.setpoint = setpoint;
        }
    }

    public ClawSubsystem(HardwareMap hardwareMap) {
        clawServo = hardwareMap.get(Servo.class, "clawServo");
    }

    public ClawState getState() {
        return state;
    }

    public void toggleState() {
        if (state == ClawState.CLOSED) state = ClawState.OPEN;
        else state = ClawState.CLOSED;

        clawServo.setPosition(state.setpoint);
    }

    @Override
    public void periodic() {
        telemetry.addData("ClawState: ", state);
    }
}

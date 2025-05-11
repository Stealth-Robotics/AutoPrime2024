package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.stealthrobotics.library.StealthSubsystem;

public class PanSubsystem extends StealthSubsystem {
    private final Servo panServo;
    private PanState state;

    public enum PanState {
        INTAKE(0.7),
        SCORE(0.3);

        private final double setpoint;
        PanState(double setpoint) {
            this.setpoint = setpoint;
        }
    }

    public PanSubsystem(HardwareMap hardwareMap) {
        panServo = hardwareMap.get(Servo.class,"panServo");
    }

    public void setPanPosition(PanState state) {
        this.state = state;
        panServo.setPosition(state.setpoint);
    }

    public PanState getState() {
        return state;
    }
}

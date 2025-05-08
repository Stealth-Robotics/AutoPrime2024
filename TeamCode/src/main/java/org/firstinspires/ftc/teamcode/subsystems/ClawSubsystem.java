package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.BooleanSupplier;

public class ClawSubsystem extends StealthSubsystem {
    private final Servo clawServo;
    private ClawState state;

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

    public Command defaultCommand(BooleanSupplier toggle) {
        return this.run(() -> );
    }

    public ClawState getState() {
        return state;
    }

    public void toggleState() {
        if (state == ClawState.CLOSED) state = ClawState.OPEN;
        else state = ClawState.CLOSED;

        clawServo.setPosition(state.setpoint);
    }
}

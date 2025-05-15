package org.firstinspires.ftc.teamcode.subsystems;


import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.DoubleSupplier;

@Config
public class ElevatorSubsystem extends StealthSubsystem {
    private final MotorEx leftMotor;
    private final MotorEx rightMotor;

    //Only used for telemetry
    private ElevatorPosition currTarget = ElevatorPosition.HOME;

    private ElevatorMode mode = ElevatorMode.PID;

    private final DigitalChannel limitSwitch;

    private final MotorGroup elevatorMotors;
    private final PIDFController elevatorPID;

    private final double kP = 0.006;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;

    private final double TOLERANCE = 10.0;
    private final double MAX_HEIGHT = 3300;

    public enum ElevatorPosition {
        HIGH_BUCKET(1.0),
        LOW_BUCKET(0.5),
        LOW_CHAMBER(0.7),
        HIGH_CHAMBER(0.2),
        HOME(0.0);

        private final double position;
        ElevatorPosition(double position) {
            this.position = position;
        }
    }

    public enum ElevatorMode {
        PID,
        MANUAL,
        HOLD
    }

    public ElevatorSubsystem(HardwareMap hardwareMap) {
        leftMotor = new MotorEx(hardwareMap, "leftElevatorMotor");
        rightMotor = new MotorEx(hardwareMap, "rightElevatorMotor");

        limitSwitch = hardwareMap.get(DigitalChannel.class, "limitSwitch");

        rightMotor.setInverted(true);

        elevatorMotors = new MotorGroup(leftMotor, rightMotor);

        elevatorMotors.setRunMode(Motor.RunMode.RawPower);
        elevatorMotors.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        rightMotor.stopAndResetEncoder();

        elevatorPID = new PIDFController(kP, kI, kD, kF);
        elevatorPID.setTolerance(TOLERANCE);
    }

    public void setPosition(ElevatorPosition pos) {
        currTarget = pos;
        elevatorPID.setSetPoint(pos.position * MAX_HEIGHT);
    }

    public void setPower(double power) {
        elevatorMotors.set(power);
    }

    public int getPosition() {
        return -rightMotor.getCurrentPosition();
    }

    public void setMode(ElevatorMode newMode) {
        mode = newMode;
    }

    public void resetEncoder() {
        rightMotor.resetEncoder();
    }

    public void holdPosition() {
        elevatorPID.setSetPoint(elevatorMotors.getCurrentPosition());
    }

    public boolean getLimitSwitch() {
        return limitSwitch.getState();
    }

    @Override
    public void periodic() {
        if (mode == ElevatorMode.PID) {
            setPower(-elevatorPID.calculate(getPosition()));
        }
        else if (mode == ElevatorMode.HOLD) {
            holdPosition();
        }

        //Re-localize if limit switch triggered
        if (getLimitSwitch()) {
            resetEncoder();
            setPosition(ElevatorPosition.HOME);
        }

        telemetry.addData("Elevator Target: ", currTarget);
        telemetry.addData("Elevator Mode: ", mode.name());
    }
}

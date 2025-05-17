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

    private ElevatorMode mode = ElevatorMode.PID;

    private final MotorGroup elevatorMotors;
    private final PIDFController elevatorPID;

    public static double kP = 0.005;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0; //! This does what?

    public static double TOLERANCE = 10.0;
    public static double MAX_HEIGHT = 3150;

    @Config
    public static class ElevatorPosition {
        public static double HIGH_BUCKET = 1.0;
        public static double LOW_BUCKET = 0.55;
        public static double HIGH_CHAMBER = 0.4;
        public static double LOW_CHAMBER = 0.1;
        public static double HOME = 0.0;
    }

    public enum ElevatorMode {
        PID,
        MANUAL,
        HOLD
    }

    public ElevatorSubsystem(HardwareMap hardwareMap) {
        leftMotor = new MotorEx(hardwareMap, "leftElevatorMotor");
        rightMotor = new MotorEx(hardwareMap, "rightElevatorMotor");

        rightMotor.setInverted(true);

        elevatorMotors = new MotorGroup(leftMotor, rightMotor);

        elevatorMotors.setRunMode(Motor.RunMode.RawPower);
        elevatorMotors.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        rightMotor.stopAndResetEncoder();

        elevatorPID = new PIDFController(kP, kI, kD, kF);
        elevatorPID.setTolerance(TOLERANCE);
    }

    public void setPosition(double pos) {
        elevatorPID.setSetPoint(pos * MAX_HEIGHT);
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
        elevatorPID.setSetPoint(getPosition());
    }

    @Override
    public void periodic() {
        if (mode == ElevatorMode.PID) {
            setPower(-elevatorPID.calculate(getPosition()));
        }
        else if (mode == ElevatorMode.HOLD) {
            holdPosition();
        }

        telemetry.addData("Elevator Position", getPosition());
        telemetry.addData("Elevator Mode: ", mode.name());
    }
}

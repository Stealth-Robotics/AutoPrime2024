package org.firstinspires.ftc.teamcode.subsystems;


import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.DoubleSupplier;

@Config
public class ElevatorSubsystem extends StealthSubsystem {
    private final MotorEx leftMotor;
    private final MotorEx rightMotor;

    private final MotorGroup elevatorMotors;
    private final PIDFController elevatorPID;

    private ElevatorMode mode = ElevatorMode.PID;

    private final double kP = 0.006;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;

    private final double TOLERANCE = 10.0;
    private final double ELEVATOR_SPEED = 1.0;
    private final double MAX_HEIGHT = 3300;

    public enum ElevatorPosition {
        HIGH_BUCKET(0),
        LOW_BUCKET(0),
        LOW_CHAMBER(0),
        HIGH_CHAMBER(0),
        HOME(0);

        private final double position;
        ElevatorPosition(double position) {
            this.position = position;
        }
    }

    public enum ElevatorMode {
        MANUAL,
        PID,
        HOLDING
    }

    public ElevatorSubsystem(HardwareMap hardwareMap) {
        leftMotor = new MotorEx(hardwareMap, "leftElevatorMotor");
        rightMotor = new MotorEx(hardwareMap, "rightElevatorMotor");

        rightMotor.setInverted(true);

        elevatorMotors = new MotorGroup(leftMotor, rightMotor);

        elevatorMotors.setRunMode(Motor.RunMode.RawPower);
        elevatorMotors.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        elevatorMotors.stopAndResetEncoder();

        elevatorPID = new PIDFController(kP, kI, kD, kF);
        elevatorPID.setTolerance(TOLERANCE);
    }

    //Set the elevator position as a percentage of its max height
    public void setPosition(ElevatorPosition pos) {
        setMode(ElevatorMode.PID);
        elevatorPID.setSetPoint(pos.position * MAX_HEIGHT);
    }

    public void home() {
        elevatorMotors.resetEncoder();
        setPosition(ElevatorPosition.HOME);
    }

    public int getPosition() {
        return -rightMotor.getCurrentPosition();
    }

    public void setMode(ElevatorMode mode) {
        this.mode = mode;
    }

    public void setElevatorPower(double power) {
        rightMotor.set(power * ELEVATOR_SPEED);
    }

    private void holdPosition() {
        elevatorPID.setSetPoint(elevatorMotors.getCurrentPosition());
    }

    @Override
    public void periodic() {
        if (mode == ElevatorMode.PID) {
            double calc = elevatorPID.calculate(getPosition());
            setElevatorPower(-calc);
        }
        else if (mode == ElevatorMode.HOLDING) {
            holdPosition();
        }

        telemetry.addData("ElevatorMode: ", mode.name());
    }
}

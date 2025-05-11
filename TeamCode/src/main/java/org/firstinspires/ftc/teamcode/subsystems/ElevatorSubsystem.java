package org.firstinspires.ftc.teamcode.subsystems;


import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

import org.stealthrobotics.library.StealthSubsystem;

@Config
public class ElevatorSubsystem extends StealthSubsystem {
    private final MotorEx leftMotor;
    private final MotorEx rightMotor;

    private final MotorGroup elevatorMotors;

    private final DigitalChannel limitSwitch;

    private final PIDFController elevatorPID;

    private final double kP = 0.006;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;

    private final double tolerance = 10.0;

    private final double SPEED = 1.0;
    private final double MAX_HEIGHT = 3300;

    public ElevatorSubsystem(HardwareMap hardwareMap) {
        leftMotor = new MotorEx(hardwareMap, "leftElevatorMotor");
        rightMotor = new MotorEx(hardwareMap, "rightElevatorMotor");

        rightMotor.setInverted(true);

        elevatorMotors = new MotorGroup(leftMotor, rightMotor);

        elevatorMotors.setRunMode(Motor.RunMode.RawPower);
        elevatorMotors.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        elevatorMotors.stopAndResetEncoder();

        elevatorPID = new PIDFController(kP, kI, kD, kF);
        elevatorPID.setTolerance(tolerance);

        limitSwitch = hardwareMap.get(DigitalChannel.class, "limitSwitch");
    }

    //Set the elevator position as a percentage of its max height
    public void setElevatorPosition(double position) {
        if (position > 1.0) throw new IllegalArgumentException("Trying to exceed elevator's height limit");
        elevatorPID.setSetPoint(position * MAX_HEIGHT);
    }

    //TODO Add a hold mode for automatic climb sequence

    public int getPosition() {
        return -elevatorMotors.getCurrentPosition();
    }

    public boolean getLimitSwitch() {
        return limitSwitch.getState();
    }

    @Override
    public void periodic() {
        double calc = elevatorPID.calculate(getPosition());
        elevatorMotors.set(-calc * SPEED);
    }
}

package org.firstinspires.ftc.teamcode.subsystems;


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

@Config
public class ElevatorSubsystem extends StealthSubsystem {
    private final MotorGroup elevatorMotors;
    private final PIDFController elevatorPID;

    private final double kP = 0.006;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;

    private final double tolerance = 10.0;

    private final double ELEVATOR_SPEED = 1.0;
    private final double MAX_HEIGHT = 3300;

    public ElevatorSubsystem(HardwareMap hardwareMap) {
        MotorEx leftMotor = new MotorEx(hardwareMap, "leftElevatorMotor");
        MotorEx rightMotor = new MotorEx(hardwareMap, "rightElevatorMotor");

        rightMotor.setInverted(true);

        elevatorMotors = new MotorGroup(leftMotor, rightMotor);

        elevatorMotors.setRunMode(Motor.RunMode.RawPower);
        elevatorMotors.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        elevatorMotors.stopAndResetEncoder();

        elevatorPID = new PIDFController(kP, kI, kD, kF);
        elevatorPID.setTolerance(tolerance);
    }

    //TODO Add a hold mode for automatic climb sequence

    //Set the elevator position as a percentage of its max height
    public Command setPosition(double position) {
        if (position > 1.0) throw new IllegalArgumentException("Trying to exceed elevator's height limit");
        return this.runOnce(
                () -> elevatorPID.setSetPoint(position * MAX_HEIGHT)
        );
    }

    public Command home() {
        return new SequentialCommandGroup(
                new InstantCommand(elevatorMotors::resetEncoder),
                setPosition(0.0)
        );
    }

    public int getPosition() {
        return -elevatorMotors.getCurrentPosition();
    }

    @Override
    public void periodic() {
        double calc = elevatorPID.calculate(getPosition());
        elevatorMotors.set(-calc * ELEVATOR_SPEED);
    }
}

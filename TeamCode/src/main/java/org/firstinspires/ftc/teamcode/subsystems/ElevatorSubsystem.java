package org.firstinspires.ftc.teamcode.subsystems;


import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.pedropathing.pathgen.MathFunctions;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

import org.stealthrobotics.library.StealthSubsystem;
import org.stealthrobotics.library.math.filter.Debouncer;

import java.util.function.DoubleSupplier;

@Config
public class ElevatorSubsystem extends StealthSubsystem {
    private final MotorEx leftMotor;
    private final MotorEx rightMotor;

    private final MotorGroup elevatorMotors;
    private final PIDFController elevatorPID;

    public static double kP = 0.003;
    public static double kI = 0.002;
    public static double kD = 0.0;
    public static double kF = 0.0;

    public static double TOLERANCE = 1.0;
    public static double MAX_HEIGHT = 3200;

    public static double DUNK_AMOUNT = 0.1;

    public static double RESET_POWER = 0.6;
    public static double RESET_STALL_TIME_SEC = 0.050;

    public static double STALLED_TOLERANCE = 1.0;

    boolean isResetting = false;

    final Debouncer stalledDebouncer = new Debouncer(RESET_STALL_TIME_SEC, Debouncer.DebounceType.kRising);

    @Config
    public static class ElevatorPosition {
        public static double HIGH_BUCKET = 1.0;
        public static double LOW_BUCKET = 0.55;
        public static double HIGH_CHAMBER = 0.4;
        public static double LOW_CHAMBER = 0.1;
        public static double HOME = 0.0;
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

    public void downSlowForReset() {
        isResetting = true;
        setPower(-RESET_POWER);
        stalledDebouncer.calculate(false);
    }

    public boolean isStalled() {
        return stalledDebouncer.calculate(Math.abs(leftMotor.getVelocity()) < STALLED_TOLERANCE);
    }

    public void completeReset() {
        setPower(0.0);
        setPosition(ElevatorPosition.HOME);
        leftMotor.stopAndResetEncoder();
        rightMotor.stopAndResetEncoder();
        isResetting = false;
    }

    public void setPosition(double pos) {
        pos = MathFunctions.clamp(pos, 0.0, 1.0);
        elevatorPID.setSetPoint(pos * MAX_HEIGHT);
    }

    private void setPower(double pow) {
        elevatorMotors.set(pow);
    }

    public boolean isHomed() {
        return getPosition() <= 100; // ! Arbitrary value
    }

    public double getPositionPercentage() {
        return getPosition() / MAX_HEIGHT;
    }

    public int getPosition() {
        return rightMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        if (!isResetting)
            setPower(elevatorPID.calculate(getPosition()));

        telemetry.addData("Elevator Home", isHomed());
        telemetry.addData("Elevator Position", getPosition());
    }
}

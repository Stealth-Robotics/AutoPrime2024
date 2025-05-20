package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.DoubleSupplier;

@Config
public class ExtendoSubsystem extends StealthSubsystem {
    private final DcMotorEx extensionMotor;
    private final PIDController extensionPID;

    private ExtendoMode mode = ExtendoMode.PID;

    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public static double POSITION_TOLERANCE = 0.0;
    public static double HOMED_TOLERANCE = 10.0;
    public static double MAX_EXTENSION = 1300;

    @Config
    public static class ExtendoPosition {
        public static double DEPLOYED = 0.6;
        public static double TRANSFER = 0.15;
        public static double HOME = 0.0;
        public static double PAST_HOME = -0.1;
    }

    public enum ExtendoMode {
        MANUAL,
        PID
    }

    public ExtendoSubsystem(HardwareMap hardwareMap) {
        extensionMotor = hardwareMap.get(DcMotorEx.class, "extensionMotor");
        resetEncoder();

        extensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionPID = new PIDController(kP, kI, kD);
        extensionPID.setTolerance(POSITION_TOLERANCE);
    }

    public void setPower(double power) {
        extensionMotor.setPower(power);
    }

    public void setMode(ExtendoMode mode) {
        this.mode = mode;
    }

    public void setPosition(double pos) {
        extensionPID.setSetPoint(pos * MAX_EXTENSION);
    }

    public void holdPosition() {
        extensionPID.setSetPoint(getPosition());
    }

    public boolean isHomed() {
        return Math.abs(getPosition()) < HOMED_TOLERANCE;
    }

    public void resetEncoder() {
        extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getPosition() {
        return extensionMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        if (mode == ExtendoMode.PID) {
            extensionMotor.setPower(extensionPID.calculate(getPosition()));
        }

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        dashboardTelemetry.addData("Extendo position", getPosition());
        dashboardTelemetry.addData("Extendo target", extensionPID.getSetPoint());
        dashboardTelemetry.update();

        telemetry.addData("Extendo Mode: ", mode.name());
        telemetry.addData("Extendo Homed: ", isHomed());
        telemetry.addData("Extendo Position: ", getPosition());
        telemetry.addData("Extendo Current:", extensionMotor.getCurrent(CurrentUnit.AMPS));
    }
}

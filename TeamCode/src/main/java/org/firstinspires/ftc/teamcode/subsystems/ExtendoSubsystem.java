package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.DoubleSupplier;

@Config
public class ExtendoSubsystem extends StealthSubsystem {
    private final DcMotorEx extensionMotor;
    private final PIDController extensionPID;

    private ExtendoMode mode = ExtendoMode.PID;
    
    private final double kP = 0.002;
    private final double kI = 0.00000001;
    private final double kD = 0.00000001;

    private final double POSITION_TOLERANCE = 0.0;
    private final double HOMED_TOLERANCE = 0.0;
    private final double MAX_EXTENSION = 1300;

    public enum ExtendoPosition {
        DEPLOYED(0.5),
        HOME(0.0);

        private final double position;
        ExtendoPosition(double position) {
            this.position = position;
        }
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

    public void setPosition(ExtendoPosition pos) {
        extensionPID.setSetPoint(pos.position * MAX_EXTENSION);
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

        telemetry.addData("Extendo Mode: ", mode.name());
    }
}

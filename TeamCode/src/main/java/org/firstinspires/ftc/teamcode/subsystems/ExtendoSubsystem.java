package org.firstinspires.ftc.teamcode.subsystems;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import androidx.core.math.MathUtils;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.stealthrobotics.library.StealthSubsystem;

@Config
public class ExtendoSubsystem extends StealthSubsystem {
    private final DcMotorEx extensionMotor;
    private final PIDController extensionPID;

    private ExtendoMode mode = ExtendoMode.PID;
    
    private final double kP = 0.002;
    private final double kI = 0.00000001;
    private final double kD = 0.00000001;

    private final double EXTENDO_SPEED = 1.0;
    
    private final double tolerance = 0.0;
    private final double MAX_EXTENSION = 1300;

    public enum ExtendoPosition {
        DEPLOYED(0.0),
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
        extensionPID.setTolerance(tolerance);
    }

    public void setPower(double power) {
        extensionMotor.setPower(power * EXTENDO_SPEED);
    }

    public void setMode(ExtendoMode mode) {
        this.mode = mode;
    }
    
    private void setPosition(ExtendoPosition pos) {
        setMode(ExtendoMode.PID);
        extensionPID.setSetPoint(pos.position * MAX_EXTENSION);
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
            double calc = extensionPID.calculate(getPosition());
            extensionMotor.setPower(calc);
        }
    }
}

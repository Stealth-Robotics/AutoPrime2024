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
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.MathFunctions;
import org.stealthrobotics.library.StealthSubsystem;
import org.stealthrobotics.library.math.filter.Debouncer;

import java.util.function.DoubleSupplier;

@Config
public class ExtendoSubsystem extends StealthSubsystem {
    private final DcMotorEx extensionMotor;
    private final PIDController extensionPID;

    public static double kP = 0.005;
    public static double kI = 0.15;
    public static double kD = 0.0;

    public static double POSITION_TOLERANCE = 20.0;
    public static double MAX_EXTENSION = 1180;

    public static double RESET_POWER = 0.6;
    public static double RESET_STALL_TIME_SEC = 0.1;
    public static double STALLED_TOLERANCE = 0.01;

    public static boolean isHomed = true;
    private boolean isResetting = false;

    final Debouncer stalledDebouncer = new Debouncer(RESET_STALL_TIME_SEC, Debouncer.DebounceType.kRising);

    @Config
    public static class ExtendoPosition {
        public static double DEPLOYED = 0.4;
        public static double TRANSFER = 0.15;
        public static double HOME = 0.0;
        public static double PAST_HOME = -0.5;
    }

    public ExtendoSubsystem(HardwareMap hardwareMap) {
        extensionMotor = hardwareMap.get(DcMotorEx.class, "extensionMotor");
        resetEncoder();

        extensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionPID = new PIDController(kP, kI, kD);
        extensionPID.setTolerance(POSITION_TOLERANCE);
    }

    public void downSlowForReset() {
        isResetting = true;
        extensionMotor.setPower(-RESET_POWER);
        stalledDebouncer.calculate(false);
    }

    public boolean isStalled() {
        return stalledDebouncer.calculate(Math.abs(extensionMotor.getVelocity()) < STALLED_TOLERANCE);
    }

    public void completeReset() {
        extensionMotor.setPower(0.0);
        setPosition(ExtendoPosition.HOME);
        resetEncoder();
        isResetting = false;
    }

    public void setPosition(double pos) {
        pos = MathFunctions.clamp(pos, 0.0, 1.0);
        extensionPID.setSetPoint(pos * MAX_EXTENSION);
    }

    public boolean isHomed() {
        return isHomed;
    }

    public void setIsHomed(boolean newVal) {
        isHomed = newVal;
    }

    public boolean atPosition() {
        return extensionPID.atSetPoint();
    }

    public void resetEncoder() {
        extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public double getPositionPercentage() {
        return getPosition() / MAX_EXTENSION;
    }

    public double getPosition() {
        return extensionMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        if (!isResetting)
            extensionMotor.setPower(extensionPID.calculate(getPosition()));

        telemetry.addData("Extendo Position", getPosition());
        telemetry.addData("Extendo Current", extensionMotor.getCurrent(CurrentUnit.AMPS));
    }
}

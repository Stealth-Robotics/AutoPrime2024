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

import java.util.function.DoubleSupplier;

@Config
public class ExtendoSubsystem extends StealthSubsystem {
    private final DcMotorEx extensionMotor;
    private final PIDController extensionPID;

    public static double kP = 0.01;
    public static double kI = 0.0;
    public static double kD = 0.0;

    public static double POSITION_TOLERANCE = 0.0;
    public static double MAX_EXTENSION = 1300;

    public static boolean isHomed = true;

    @Config
    public static class ExtendoPosition {
        public static double DEPLOYED = 0.6;
        public static double TRANSFER = 0.15;
        public static double HOME = 0.0;
        public static double PAST_HOME = -0.1;
    }

    public ExtendoSubsystem(HardwareMap hardwareMap) {
        extensionMotor = hardwareMap.get(DcMotorEx.class, "extensionMotor");
        resetEncoder();

        extensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        extensionPID = new PIDController(kP, kI, kD);
        extensionPID.setTolerance(POSITION_TOLERANCE);
    }

    public void setPosition(double pos) {
        pos = MathFunctions.clamp(pos, 0.0, 1.0);
        extensionPID.setSetPoint(pos * MAX_EXTENSION);
    }

    public void holdPosition() {
        extensionPID.setSetPoint(getPosition());
    }

    public boolean isHomed() {
        return isHomed;
    }

    public void setIsHomed(boolean newVal) {
        isHomed = newVal;
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
        extensionMotor.setPower(extensionPID.calculate(getPosition()));

        telemetry.addData("Extendo Homed: ", isHomed());
        telemetry.addData("Extendo Position: ", getPosition());
        telemetry.addData("Extendo Current: ", extensionMotor.getCurrent(CurrentUnit.AMPS));
    }
}

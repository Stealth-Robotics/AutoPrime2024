package org.firstinspires.ftc.teamcode.subsystems;


import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class LifterSubsystem extends SubsystemBase {
    private MotorEx armL;
    private MotorEx armR;
    private final PIDFController armPID;
    public static double kP = 0.006;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kF = 0.0;
    public static double tolerance = 10.0;
    private final double maxSpeed = 1;
    private final double maxHeight = 2900;
    private final MotorGroup lifterMotors;
    private Telemetry telemetry;

    public LifterSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        armL = new MotorEx(hardwareMap, "lifterMotorL");
        armR = new MotorEx(hardwareMap, "lifterMotorR");
        armR.setInverted(true);
        lifterMotors = new MotorGroup(armL,armR);
        lifterMotors.setRunMode(Motor.RunMode.RawPower);
        lifterMotors.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        armR.stopAndResetEncoder();
        armPID = new PIDFController(kP, kI, kD, kF);
        armPID.setTolerance(tolerance);
        this.telemetry = telemetry;

    }

    public void moveArm(double position) {
        armPID.setSetPoint(position * maxHeight);
    }

    public void setPower(double power) { lifterMotors.set(power); }

    public int getPosition() { return -armR.getCurrentPosition(); }

    public void resetEncoder(){
        armR.resetEncoder();
    }

    @Override
    public void periodic(){
        double calc = armPID.calculate(getPosition());
        setPower(-calc*maxSpeed);

        FtcDashboard.getInstance().getTelemetry().addData("arm position:", getPosition());
        FtcDashboard.getInstance().getTelemetry().addData("sp:", armPID.getSetPoint());
        FtcDashboard.getInstance().getTelemetry().addData("calc:", calc);

        FtcDashboard.getInstance().getTelemetry().update();
        telemetry.addData("sp: ", armPID.getSetPoint());
        telemetry.addData("calc: ", calc);
    }
}

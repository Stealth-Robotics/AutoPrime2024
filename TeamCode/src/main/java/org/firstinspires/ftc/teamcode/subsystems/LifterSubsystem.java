package org.firstinspires.ftc.teamcode.subsystems;

import androidx.core.math.MathUtils;

import com.acmerobotics.dashboard.FtcDashboard;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.arcrobotics.ftclib.hardware.motors.MotorEx;
import com.arcrobotics.ftclib.hardware.motors.MotorGroup;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class LifterSubsystem extends SubsystemBase {
    private MotorEx armL;
    private MotorEx armR;
    private final PIDFController armPID;
    private final double kP = 0.001;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;
    private final double tolerance = 10.0;
    private final double maxSpeed = 0.5;
    private final MotorGroup lifterMotors;
    private Telemetry telemetry;

    public LifterSubsystem(HardwareMap hardwareMap){
        armL = new MotorEx(hardwareMap, "lifterMotorL");
        armR = new MotorEx(hardwareMap, "lifterMotorR");
        armL.setInverted(true);
        lifterMotors = new MotorGroup(armL,armR);
        lifterMotors.setRunMode(Motor.RunMode.RawPower);
        lifterMotors.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);
        armPID = new PIDFController(kP, kI, kD, kF);
        armPID.setTolerance(tolerance);
        this.telemetry = telemetry;

    }

    public void moveArm(int position) {
        armPID.setSetPoint(position);
    }

    public void setPower(double power) { lifterMotors.set(power); }

    public int getPosition() { return (armL.getCurrentPosition()); }

    @Override
    public void periodic(){
        double calc = armPID.calculate(getPosition());
        if(getPosition() > -1000){
            setPower(MathUtils.clamp(calc,-maxSpeed,maxSpeed));
        } else moveArm(0);

        FtcDashboard.getInstance().getTelemetry().addData("arm position:", getPosition());
        FtcDashboard.getInstance().getTelemetry().update();
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import androidx.core.math.MathUtils;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
public class ReacherSubsystem extends SubsystemBase {
    private DcMotorEx reachMotor;
    private PIDController reacherPID;
    private Telemetry telemetry;

    public static double kP = 0.002;
    public static double kI = 0.00000001;
    public static double kD = 0.00000001;
    public static double tolerance = 0;
    public static double maxReach = 1300;
    public double maxSpeed = 1;


    public ReacherSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        reachMotor = hardwareMap.get(DcMotorEx.class, "reachMotor");
        reachMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        reachMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        reachMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        reacherPID = new PIDController(kP,kI,kD);
        reacherPID.setTolerance(tolerance);
        this.telemetry = telemetry;
    }
    public void setSetPoint (double setPoint){reacherPID.setSetPoint(setPoint*maxReach);}
    public void setMaxSpeed (double speed){maxSpeed = speed;}
    public void resetEncoder(){
        reachMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        reachMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public double getSetPoint() {
        return reacherPID.getSetPoint();
    }
    public void setPower (double power){
        reachMotor.setPower(power);
    }
    public double getPosition (){
        return reachMotor.getCurrentPosition();
    }

    @Override
    public void periodic(){
        double calc = reacherPID.calculate(getPosition());
        setPower(MathUtils.clamp(calc,-maxSpeed,maxSpeed));
        telemetry.addData("reacherPos", getPosition());

        FtcDashboard.getInstance().getTelemetry().addData("setpoint:", getSetPoint());
        FtcDashboard.getInstance().getTelemetry().addData("position", getPosition());

        FtcDashboard.getInstance().getTelemetry().addData("calc", reacherPID.calculate(getPosition()));

        FtcDashboard.getInstance().getTelemetry().update();
    }

}

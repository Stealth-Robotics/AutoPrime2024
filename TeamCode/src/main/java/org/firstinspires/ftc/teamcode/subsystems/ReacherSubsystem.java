package org.firstinspires.ftc.teamcode.subsystems;

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

    public static double kP = 0;
    public static double kI = 0;
    public static double kD = 0;
    public static double tolerance = 0;


    public ReacherSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        reachMotor = hardwareMap.get(DcMotorEx.class, "reachMotor");
        reachMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        reacherPID = new PIDController(kP,kI,kD);
        reacherPID.setTolerance(tolerance);
        this.telemetry = telemetry;
    }
    public void setSetPoint (double setPoint){reacherPID.setSetPoint(setPoint);}
    public void setPower (double power){
        reachMotor.setPower(power);
    }
    public double getPosition (){
        return reachMotor.getCurrentPosition();
    }

    @Override
    public void periodic(){
        double calc = reacherPID.calculate(getPosition());
        setSetPoint(calc);
        telemetry.addData("reacherPos", getPosition());
    }

}

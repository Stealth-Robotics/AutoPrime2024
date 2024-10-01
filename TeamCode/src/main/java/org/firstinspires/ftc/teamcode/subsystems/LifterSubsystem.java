package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

public class LifterSubsystem extends SubsystemBase {
    private DcMotorEx armL;
    private DcMotorEx armR;
    private final PIDFController armPID;
    private final double kP = 0.0;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;
    private final double tolerance = 10.0;

    public LifterSubsystem(HardwareMap hardwareMap){
        armL = hardwareMap.get(DcMotorEx.class, "lifterMotorL");
        armR = hardwareMap.get(DcMotorEx.class, "lifterMotorR");
        armPID = new PIDFController(kP, kI, kD, kF);
        armPID.setTolerance(tolerance);



    }

    public void MoveArm(int position) {
        armPID.setSetPoint (position);
    }



}

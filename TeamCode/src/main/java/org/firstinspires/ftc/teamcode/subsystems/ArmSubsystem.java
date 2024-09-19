package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;


import com.arcrobotics.ftclib.controller.PIDFController;

public class ArmSubsystem extends SubsystemBase {
    private DcMotorEx arm;
    private final PIDFController armPID;
    private final double kP = 0.0;
    private final double kI = 0.0;
    private final double kD = 0.0;
    private final double kF = 0.0;
    private final double tolerance = 10.0;

    public ArmSubsystem (HardwareMap hardwareMap){
        arm = hardwareMap.get(DcMotorEx.class, "armMotor");
        armPID = new PIDFController(kP, kI, kD, kF);
        armPID.setTolerance(tolerance);
    }

    private void MoveArm(double position) {
    armPID.setSetPoint (position);
    }




}

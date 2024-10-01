package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ReacherSubsystem extends SubsystemBase {
    DcMotorEx reachMotor;

    public ReacherSubsystem(HardwareMap hardwareMap){
        reachMotor = hardwareMap.get(DcMotorEx.class, "reachMotor");
    }
    public void setPower (double power){
        reachMotor.setPower(power);
    }
    public double getPosition (){
        return reachMotor.getCurrentPosition();
    }
}

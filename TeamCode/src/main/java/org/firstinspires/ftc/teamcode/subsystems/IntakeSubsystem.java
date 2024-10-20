package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class IntakeSubsystem extends SubsystemBase {
    private Servo intakeServo;
    //private RevColorSensorV3 intakeSensor;
    private Telemetry telemetry;
    public enum ColorList {RED,BLUE,BLACK}
    public int intakeDirection = 1;

    public IntakeSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        intakeServo = hardwareMap.get(Servo.class, "intakeServo");
        this.telemetry = telemetry;
    }

    public void setPower(double power){
        intakeServo.setPosition((power+1)/2);
    }
    /*public IntakeSensorSubsystem.ColorList readSensorColor(){
        if(intakeSensor.red()> intakeSensor.blue()){
            return(IntakeSensorSubsystem.ColorList.RED);
        } else {
            return(IntakeSensorSubsystem.ColorList.BLUE);
        }
    }
    public double readSensorDistance(){
        return intakeSensor.getDistance(DistanceUnit.INCH);
    }
    @Override
    public void periodic(){
        telemetry.addData("Color",readSensorColor());
        telemetry.addData("Distance",readSensorDistance());
    }*/
}

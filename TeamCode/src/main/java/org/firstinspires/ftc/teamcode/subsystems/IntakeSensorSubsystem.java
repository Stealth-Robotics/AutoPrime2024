package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class IntakeSensorSubsystem extends SubsystemBase {
    private I2cDeviceSynchSimple deviceClient;
    private RevColorSensorV3 intakeSensor;
    public enum ColorList {RED,BLUE,BLACK}
    private Telemetry telemetry;

    public IntakeSensorSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        intakeSensor = new RevColorSensorV3(deviceClient,true);
        this.telemetry = telemetry;
    }
    public ColorList readIntakeSensorColor(){
        if(intakeSensor.red()> intakeSensor.blue()){
            return(ColorList.RED);
        } else {
            return(ColorList.BLUE);
        }
    }
    public double readDistance(){
        return intakeSensor.getDistance(DistanceUnit.INCH);
    }
    @Override
    public void periodic(){
        telemetry.addData("Color",readIntakeSensorColor());
        telemetry.addData("Distance",readDistance());
    }
}

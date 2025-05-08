package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class IntakeSensorSubsystem extends SubsystemBase {


    private Telemetry telemetry;

    public IntakeSensorSubsystem(HardwareMap hardwareMap, Telemetry telemetry){

        intakeSensor = hardwareMap.get(ColorSensor.class, "intakeSensor");

        this.telemetry = telemetry;
    }

    public int yellow(){
        return((intakeSensor.red()+intakeSensor.green())/2);
    }
    public ColorList readSensorColor(){
        if ((intakeSensor.red()>75) || (intakeSensor.blue() > 75) || yellow() > 75) {
            if (intakeSensor.red() > intakeSensor.blue()) {
                if(intakeSensor.red() > yellow()){
                    return (ColorList.RED);
                } else return (ColorList.YELLOW);
            } else {
                return (ColorList.BLUE);
            }
        } else return ColorList.BLACK;
    }

    @Override
    public void periodic(){
        telemetry.addData("red",intakeSensor.red());

        telemetry.addData("blue",intakeSensor.blue());
        telemetry.addData("green", intakeSensor.green());
        telemetry.addData("color", readSensorColor());
    }
}

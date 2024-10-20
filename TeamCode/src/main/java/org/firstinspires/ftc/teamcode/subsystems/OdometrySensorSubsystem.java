package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.SparkFunOTOS;

public class OdometrySensorSubsystem extends SubsystemBase {
    private static SparkFunOTOS odoSensor;

    public OdometrySensorSubsystem(HardwareMap hardwareMap){
        odoSensor = hardwareMap.get(SparkFunOTOS.class,"odometrySensor");
    }
    private void ConfigureOTOS(){
        odoSensor.setLinearUnit(DistanceUnit.INCH);
        //odoSensor.setAngularUnit();
    }
}

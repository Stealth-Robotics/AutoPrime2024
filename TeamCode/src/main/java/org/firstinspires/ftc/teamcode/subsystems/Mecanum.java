package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.stealthrobotics.library.Commands;
import org.stealthrobotics.library.StealthSubsystem;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

public class Mecanum extends StealthSubsystem {
    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;
    private Telemetry telemetry;
    SparkFunOTOS otos;
    double headingOffset = 0;

    public Mecanum(HardwareMap hardwareMap, Telemetry telemetry) {
        frontLeft = hardwareMap.get(DcMotorEx.class, "leftFront");
        frontRight = hardwareMap.get(DcMotorEx.class, "rightFront");
        backLeft = hardwareMap.get(DcMotorEx.class, "leftRear");
        backRight = hardwareMap.get(DcMotorEx.class, "rightRear");
        this.telemetry = telemetry;

        //TODO: Check
        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        //imu = hardwareMap.get(IMU.class, "imu");
        otos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        //TODO: Must update with correct values
        IMU.Parameters imuParameters = new IMU.Parameters(
                new RevHubOrientationOnRobot(
                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                        RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
        );
        otos.calibrateImu();

        //imu.initialize(imuParameters);
    }
    public void setHeading(double headingOffset){
        otos.setOffset(new SparkFunOTOS.Pose2D(0,0,headingOffset));
    }

    public double getHeading() {
        //return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) - headingOffset;
        //return Math.PI;
        return otos.getPosition().h;
    }

    public void resetHeading() {
        //headingOffset = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        otos.resetTracking();
    }

    public void drive(double x, double y, double rot, boolean slow) {
        double speedMult;
        if(slow){
            speedMult = 0.3;
        } else {
            speedMult = 1;
        }
        y = -speedMult*y;
        x = speedMult*x;
        rot = speedMult*rot;

        double rotX = x * Math.cos(-getHeading()) - y * Math.sin(-getHeading());
        double rotY = x * Math.sin(-getHeading()) + y * Math.cos(-getHeading());

        rotX = rotX * 1.1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rot), 1);
        double frontLeftPower = (rotY + rotX + rot) / denominator;
        double backLeftPower = (rotY - rotX + rot) / denominator;
        double frontRightPower = (rotY - rotX - rot) / denominator;
        double backRightPower = (rotY + rotX - rot) / denominator;

        frontLeft.setPower(frontLeftPower);
        backLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        backRight.setPower(backRightPower);
    }

    public Command driveTeleop(DoubleSupplier x, DoubleSupplier y, DoubleSupplier rot, BooleanSupplier halfSpeedBumper) {
        return this.run(() -> drive(x.getAsDouble(), -y.getAsDouble(), rot.getAsDouble(), halfSpeedBumper.getAsBoolean()));
    }
    @Override
    public void periodic(){
        telemetry.addData("heading", getHeading());
    }
}

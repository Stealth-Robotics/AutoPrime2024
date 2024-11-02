package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.hardware.HardwareMap;

//import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.stealthrobotics.library.StealthSubsystem;

public class DriveSubsystem extends StealthSubsystem {

    public Follower follower;
    Telemetry telemetry;

    public DriveSubsystem(HardwareMap hardwareMap, Telemetry telemetry){
        follower = new Follower(hardwareMap);
        this.telemetry = telemetry;
    }

    public void startTeleopDrive(){
        follower.startTeleopDrive();
    }

    public void drive(double leftStickX, double leftStickY, double rightStickX){
        follower.setTeleOpMovementVectors(-leftStickY,-leftStickX,-rightStickX);
        follower.update();
    }
    public Command FollowPath(Path path, boolean holdPoint){
        return this.runOnce(()-> follower.followPath(path,holdPoint))
            .andThen(new WaitUntilCommand(()-> !follower.isBusy()));
    }
    public Command FollowPath(PathChain path, boolean holdPoint){
        return this.runOnce(()-> follower.followPath(path,holdPoint))
                .andThen(new WaitUntilCommand(()-> !follower.isBusy()));
    }
    public void setPose(Pose pose){
        follower.setPose(pose);
    }


    @Override
    public void periodic() {
        follower.update();
        telemetry.addData("isbusy", follower.isBusy());
    }
}
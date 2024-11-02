package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.qualcomm.robotcore.hardware.HardwareMap;

//import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.stealthrobotics.library.StealthSubsystem;

public class DriveSubsystem extends StealthSubsystem {

    public Follower follower;

    public DriveSubsystem(HardwareMap hardwareMap){
        follower = new Follower(hardwareMap);
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
    }
}
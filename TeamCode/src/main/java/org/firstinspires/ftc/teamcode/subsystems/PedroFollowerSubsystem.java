package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.stealthrobotics.library.StealthSubsystem;

public class PedroFollowerSubsystem extends StealthSubsystem {
    public Follower follower;

    public PedroFollowerSubsystem(Follower follower){
        this.follower = follower;
    }

    public Command followPath(Path path, boolean holdPoint){
        return this.runOnce(()-> follower.followPath(path,holdPoint))
                .andThen(new WaitUntilCommand(()-> !follower.isBusy()));
    }

    public Command followPath(PathChain path, boolean holdPoint){
        return this.runOnce(()-> follower.followPath(path,holdPoint))
                .andThen(new WaitUntilCommand(()-> !follower.isBusy()));
    }
}

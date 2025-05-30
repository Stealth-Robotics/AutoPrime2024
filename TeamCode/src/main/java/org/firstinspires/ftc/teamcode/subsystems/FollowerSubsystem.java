package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitUntilCommand;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.stealthrobotics.library.StealthSubsystem;

public class FollowerSubsystem extends StealthSubsystem {
    private final Follower follower;

    public FollowerSubsystem(HardwareMap hardwareMap) {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
    }

    public Command followPath(Path path, boolean holdPoint) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> follower.followPath(path, holdPoint)),
                new WaitUntilCommand(() -> !follower.isBusy())
        );
    }

    public Command followPath(PathChain path, boolean holdPoint) {
        return new SequentialCommandGroup(
                new InstantCommand(() -> follower.followPath(path, holdPoint)),
                new WaitUntilCommand(() -> !follower.isBusy())
        );
    }

    public void setMaxPower(double power) {
        follower.setMaxPower(power);
    }

    public void setStartPose(Pose startPose) {
        follower.setStartingPose(startPose);
    }

    public PathBuilder pathBuilder() {
        return follower.pathBuilder();
    }

    @Override
    public void periodic() {
        follower.update();
    }
}

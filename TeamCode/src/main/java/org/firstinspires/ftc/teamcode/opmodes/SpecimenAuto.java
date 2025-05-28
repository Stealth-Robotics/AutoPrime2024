package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FollowerSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "SpecimenAuto")
public class SpecimenAuto extends StealthOpMode {
    private FollowerSubsystem follower;
    private ElevatorSubsystem elevator;
    private ClawSubsystem claw;
    private ExtendoSubsystem extendo;

    private final Pose startPose = new Pose(8.25, 65);
    private final Pose scoringPose = new Pose(37.5, 65);
    private final Pose scoochPose = new Pose(37.5, 70);
    private final Pose pickupPose = new Pose(13.25, 36, Math.toRadians(180.01));

    private PathChain scoreSpecimenInitial, scoreSpecimen, pickupSpecimen;

    @Override
    public void initialize() {
        follower = new FollowerSubsystem(hardwareMap);
        follower.setStartPose(startPose);

        elevator = new ElevatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        extendo = new ExtendoSubsystem(hardwareMap);

        buildPaths();
    }

    public void buildPaths() {
        scoreSpecimenInitial = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scoringPose)))
//                .setConstantHeadingInterpolation(startPose.getHeading())
//                .setPathEndTimeoutConstraint(0) What does this do?
                .build();
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> claw.setState(ClawSubsystem.ClawState.CLOSED)),
                follower.followPath(scoreSpecimenInitial, true)
        );
    }
}

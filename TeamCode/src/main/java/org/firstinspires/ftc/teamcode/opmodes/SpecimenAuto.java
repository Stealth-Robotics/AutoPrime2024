package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.PedroFollowerSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "SpecimenAuto")
public class SpecimenAuto extends StealthOpMode {
    private Follower follower;
    private ElevatorSubsystem elevator;
    private ClawSubsystem claw;
    private ExtendoSubsystem extendo;

    private PedroFollowerSubsystem pedro;

    private final Pose startPose = new Pose(8.25, 65);
    private final Pose scoringPose = new Pose(37.5, 65);
    private final Pose scoochPose = new Pose(37.5, 70);
    private final Pose pickupPose = new Pose(13.25, 36, Math.toRadians(180.01));

    private PathChain scoreSpecimenInital, scoreSpecimen, pickupSpecimen;

    @Override
    public void initialize() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        elevator = new ElevatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        extendo = new ExtendoSubsystem(hardwareMap);

        pedro = new PedroFollowerSubsystem(follower);

        buildPaths();
    }

    public void buildPaths() {
        scoreSpecimenInital = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scoringPose)))
                .setPathEndTimeoutConstraint(0) // ??
                .build();
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                pedro.followPath(scoreSpecimenInital, true)
        );
    }
}

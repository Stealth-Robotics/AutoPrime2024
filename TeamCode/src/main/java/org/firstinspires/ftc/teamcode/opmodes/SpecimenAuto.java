package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.BezierPoint;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.commands.ResetElevatorCommand;
import org.firstinspires.ftc.teamcode.commands.ResetExtendoCommand;
import org.firstinspires.ftc.teamcode.subsystems.ClawSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ElevatorSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ExtendoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.FollowerSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LEDSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@Autonomous(name = "SpecimenAuto")
public class SpecimenAuto extends StealthOpMode {
    private FollowerSubsystem follower;
    private ElevatorSubsystem elevator;
    private IntakeSubsystem intake;
    private ClawSubsystem claw;
    private ExtendoSubsystem extendo;
    private LEDSubsystem led;

    private final Pose startPose = new Pose(8.25, 65);
    private final Pose scoringPose = new Pose(36, 65);
    private final Pose moveOverPose = new Pose(36, 75);
    private final Pose pickupPose = new Pose(13.25, 36, Math.toRadians(180));

    //Moving preset samples poses
    private final Pose firstSamplePose = new Pose(28.8, 35.2, Math.toRadians(-204));
    private final Pose firstSamplePoseRotated = new Pose(28.8, 35.2, Math.toRadians(47));

//    private final Pose secondSamplePose = new Pose(30, 21, Math.toRadians(-24));
//    private final Pose secondSamplePoseRotated = new Pose();
//
//    private final Pose thirdSamplePose = new Pose();
//    private final Pose thirdSamplePoseRotated = new Pose();

    private PathChain scoreInitial, score, pickupSpecimen, moveOver, moveFirstSample, moveSecondSample, moveThirdSample;

    @Override
    public void initialize() {
        follower = new FollowerSubsystem(hardwareMap);
        follower.setStartPose(startPose);

        elevator = new ElevatorSubsystem(hardwareMap);
        claw = new ClawSubsystem(hardwareMap);
        extendo = new ExtendoSubsystem(hardwareMap);
        intake = new IntakeSubsystem(hardwareMap);
        led = new LEDSubsystem(hardwareMap);

        schedule(
                new InstantCommand(() -> intake.wristHome()),
                new InstantCommand(() -> led.setMode(LEDSubsystem.LEDMode.AUTONOMOUS))
        );

        buildPaths();
    }

    public void buildPaths() {
        scoreInitial = follower.pathBuilder()
                .addPath(new BezierLine(new Point(startPose), new Point(scoringPose)))
                .addParametricCallback(0, () -> elevator.setPosition(ElevatorSubsystem.ElevatorPosition.HIGH_CHAMBER))
                .build();

        moveOver = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoringPose), new Point(moveOverPose)))
                .setConstantHeadingInterpolation(scoringPose.getHeading())
                .build();

        //Go to position, deploy extendo, rotate sample into zone, move back rotation, move on to next sample
        //At end retract extendo, go to pickup position
        moveFirstSample = follower.pathBuilder()
                .addPath(new BezierLine(new Point(moveOverPose), new Point(firstSamplePose)))
                .setLinearHeadingInterpolation(moveOverPose.getHeading(), firstSamplePose.getHeading())

                .addParametricCallback(0.6, () -> intake.wristTravel())
                .addParametricCallback(0.3, () -> extendo.setPosition(0.52))

                .addPath(new BezierPoint(new Point(firstSamplePoseRotated)))
                .setLinearHeadingInterpolation(firstSamplePose.getHeading(), firstSamplePoseRotated.getHeading())
                .build();
    }

//    public void retractSweeper() {
//        new ParallelCommandGroup(
//                new InstantCommand(() -> intake.wristHome()),
//                new ResetExtendoCommand(extendo)
//        );
//    }

    public Command scoreSpecimen() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> elevator.setPosition(elevator.getPositionPercentage() - 0.065)),
                follower.followPath(moveOver, false),
                new InstantCommand(() -> claw.toggleState())
        );
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> claw.setState(ClawSubsystem.ClawState.CLOSED)),
                follower.followPath(scoreInitial, true),
                scoreSpecimen(),
                new ParallelCommandGroup(
                        follower.followPath(moveFirstSample, true),
                        new ResetElevatorCommand(elevator)
                )
        );
    }
}

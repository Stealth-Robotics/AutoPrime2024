package org.firstinspires.ftc.teamcode.opmodes;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
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

@Autonomous(name = "BucketAuto")
public class BucketAuto extends StealthOpMode {
    private FollowerSubsystem follower;
    private ElevatorSubsystem elevator;
    private IntakeSubsystem intake;
    private ClawSubsystem claw;
    private ExtendoSubsystem extendo;
    private LEDSubsystem led;

    @Override
    public void initialize() {
        follower = new FollowerSubsystem(hardwareMap);
//        follower.setStartPose(startPose);

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
    }

    @Override
    public Command getAutoCommand() {
        return new SequentialCommandGroup(
        );
    }
}

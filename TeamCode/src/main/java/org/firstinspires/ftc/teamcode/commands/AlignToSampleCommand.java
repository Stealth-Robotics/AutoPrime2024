package org.firstinspires.ftc.teamcode.commands;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.controller.PIDController;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.subsystems.LimelightSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.MecanumSubsystem;
import org.stealthrobotics.library.AnglePIDController;

@Config
public class AlignToSampleCommand extends CommandBase {
    private final MecanumSubsystem drive;
    private final LimelightSubsystem ll;

    private final AnglePIDController pid;
    public static double kP = 2;
    public static double kI = 0.0;
    public static double kD = 0.001;

    public static boolean reverseCalc = false;

    public static double ANGLE_TOLERANCE = 0.1;

    public AlignToSampleCommand(MecanumSubsystem drive, LimelightSubsystem ll) {
        this.drive = drive;
        this.ll = ll;

        pid = new AnglePIDController(kP, kI, kD);
        pid.setTolerance(ANGLE_TOLERANCE);

        addRequirements(drive, ll);
    }

    @Override
    public void initialize() {
        pid.setSetPoint(AngleUnit.DEGREES.toRadians(ll.getAngleToSample()) + drive.getHeading());
    }

    @Override
    public void execute() {
        double calc = pid.calculate(drive.getHeading());
        if (reverseCalc) calc *= -1;
        drive.drive(0, 0, calc);
        telemetry.addData("error", (pid.getSetPoint() - drive.getHeading()));
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }

    @Override
    public boolean isFinished() {
        return pid.atSetPoint();
    }
}

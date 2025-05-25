package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.stealthrobotics.library.AnglePIDController;

import java.util.Arrays;

@Config
public class LimelightSubsystem extends SubsystemBase {
    private final Limelight3A limelight;
    private final MecanumSubsystem drive;
    private LLPipeline currPipeline = LLPipeline.YELLOW;

    FtcDashboard dashboard = FtcDashboard.getInstance();//TODO Removeeeeee
    Telemetry dashboardTelemetry = dashboard.getTelemetry(); //TODO Removeeeeee

    public enum LLPipeline {
        YELLOW(0),
        RED(1),
        BLUE(2);
        private final int index;
        LLPipeline(int index) {
            this.index = index;
        }
    }

    public LimelightSubsystem(HardwareMap hardwareMap, MecanumSubsystem drive) {
        this.drive = drive;
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
    }

    public void init() {
        limelight.setPollRateHz(100);
        limelight.start();
    }

    public void setPipeline(LLPipeline p) {
        currPipeline = p;
        limelight.pipelineSwitch(p.index);
    }

    //Degrees
    public double getAngleToSample() {
        return limelight.getLatestResult().getTx();
    }

    @Override
    public void periodic() {
        telemetry.addData("Limelight Pipeline", currPipeline.name());

        limelight.updatePythonInputs(new double[] {0, 0, 0, 0, 0, 0, 0, 0});
        LLResult result = limelight.getLatestResult();

        telemetry.addData("Angle-To-Sample", result.getTx());

        //TODO Removeeeeee
        dashboardTelemetry.addData("target", result.getTx() + Math.toDegrees(drive.getHeading()));
        dashboardTelemetry.addData("position", Math.toDegrees(drive.getHeading()));
        dashboardTelemetry.update();
    }
}

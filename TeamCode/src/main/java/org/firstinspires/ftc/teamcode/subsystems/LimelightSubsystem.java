package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

import java.util.Arrays;

public class LimelightSubsystem extends SubsystemBase {
    private final Limelight3A limelight;
    private LLPipeline currPipeline = LLPipeline.YELLOW;

    public enum LLPipeline {
        YELLOW(0),
        RED(1),
        BLUE(2);
        private final int index;
        LLPipeline(int index) {
            this.index = index;
        }
    }

    public LimelightSubsystem(HardwareMap hardwareMap) {
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

    public double getAngleToSample() {
        return limelight.getLatestResult().getTx();
    }

    @Override
    public void periodic() {
        telemetry.addData("Limelight Pipeline", currPipeline.name());

        limelight.updatePythonInputs(new double[] {0, 0, 0, 0, 0, 0, 0, 0});
        LLResult result = limelight.getLatestResult();

        telemetry.addData("Angle-To-Sample", result.getTx());
    }
}

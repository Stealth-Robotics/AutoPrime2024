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
        limelight.setPollRateHz(100);

        limelight.pipelineSwitch(0);
        limelight.start();
    }

    public void setPipeline(LLPipeline p) {
        currPipeline = p;
        limelight.pipelineSwitch(p.index);
    }

    @Override
    public void periodic() {
        telemetry.addData("Limelight Pipeline", currPipeline.name());
        telemetry.addData("Limelight Python", Arrays.toString(limelight.getLatestResult().getPythonOutput()));

        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            double tx = result.getTx(); // How far left or right the target is (degrees)
            double ty = result.getTy(); // How far up or down the target is (degrees)
            double ta = result.getTa(); // How big the target looks (0%-100% of the image)

            telemetry.addData("Target X", tx);
            telemetry.addData("Target Y", ty);
            telemetry.addData("Target Area", ta);

        } else {
            telemetry.addData("Limelight", "No Targets");
        }
    }
}

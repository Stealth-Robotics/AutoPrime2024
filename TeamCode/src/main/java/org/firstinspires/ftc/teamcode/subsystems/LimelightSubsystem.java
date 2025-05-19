package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;

public class LimelightSubsystem extends SubsystemBase {
    private final Limelight3A limelight;
    private LLResult res;
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
        setPipeline(LLPipeline.YELLOW);
        limelight.start();
    }

    public void setPipeline(LLPipeline p) {
        currPipeline = p;
        limelight.pipelineSwitch(p.index);
    }

    @Override
    public void periodic() {
        telemetry.addData("Limelight Pipeline: ", currPipeline.name());

        res = limelight.getLatestResult();
        if (res != null && res.isValid()) {
            telemetry.addData("LimeLight TX: ", res.getTx());
        }
    }
}

package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.stealthrobotics.library.opmodes.StealthOpMode.telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class LimelightSubsystem extends SubsystemBase {
    private final Limelight3A limelight;
    private LLResult image;

    public LimelightSubsystem(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void periodic() {
        image = limelight.getLatestResult();
        if (image != null) {
            if (image.isValid()) {
                Pose3D botPose = image.getBotpose();
                telemetry.addData("tx", image.getTx());
                telemetry.addData("ty", image.getTy());
                telemetry.addData("Botpose", botPose.toString());
            }
        }
    }
}

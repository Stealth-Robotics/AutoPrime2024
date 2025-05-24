package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.stealthrobotics.library.StealthSubsystem;

@Config
public class LEDSubsystem extends StealthSubsystem {
    private final RevBlinkinLedDriver blinkin;

    @Config
    public static class LEDMode {
        public static BlinkinPattern BLUE_SAMPLE = BlinkinPattern.BLUE;
        public static BlinkinPattern RED_SAMPLE = BlinkinPattern.RED;
        public static BlinkinPattern YELLOW_SAMPLE = BlinkinPattern.YELLOW;
        public static BlinkinPattern NO_SAMPLE = BlinkinPattern.BLACK;

        public static BlinkinPattern CLIMB_SEQUENCE = BlinkinPattern.RAINBOW_RAINBOW_PALETTE;
        public static BlinkinPattern AUTONOMOUS = BlinkinPattern.STROBE_GOLD;
    }

    public LEDSubsystem(HardwareMap hardwareMap) {
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        setMode(LEDMode.AUTONOMOUS);
    }

    public void setMode(BlinkinPattern pattern) {
        blinkin.setPattern(pattern);
    }
}

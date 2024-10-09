package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.lifterDefaultCommand;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
import org.stealthrobotics.library.opmodes.StealthOpMode;

@TeleOp(name = "TeleOp")
public abstract class TeleOpOpmode extends StealthOpMode {
    LifterSubsystem lifterSubsystem;
    ReacherSubsystem reacherSubsystem;
    DriveSubsystem driveSubsystem;

    GamepadEx driverGamepad;
    GamepadEx operatorGamepad;

    @Override
    public void whileWaitingToStart(){
        CommandScheduler.getInstance().run();
    }

    public void initialize(){
        lifterSubsystem = new LifterSubsystem(hardwareMap);
        reacherSubsystem = new ReacherSubsystem(hardwareMap);


        driverGamepad = new GamepadEx(gamepad1);
        operatorGamepad = new GamepadEx(gamepad2);

        lifterSubsystem.setDefaultCommand(new lifterDefaultCommand(lifterSubsystem,() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_DOWN),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_RIGHT),() -> driverGamepad.getButton(GamepadKeys.Button.DPAD_UP),()->driverGamepad.getButton(GamepadKeys.Button.LEFT_BUMPER)));
        driveSubsystem.setDefaultCommand(driveSubsystem.driveTeleop(()->driverGamepad.getLeftX(),()->driverGamepad.getLeftY(),()->driverGamepad.getRightX()));


    }


}

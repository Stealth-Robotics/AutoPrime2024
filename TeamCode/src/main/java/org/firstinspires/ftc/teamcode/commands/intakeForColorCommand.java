//package org.firstinspires.ftc.teamcode.commands;
//
//import com.arcrobotics.ftclib.command.CommandBase;
//
//import org.firstinspires.ftc.teamcode.subsystems.IntakeSensorSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
//
//import java.util.function.BooleanSupplier;
//
////This command is not functioning and is not used
//public class intakeForColorCommand extends CommandBase {
//    IntakeSubsystem intake;
//    IntakeSensorSubsystem intakeSensor;
//    BooleanSupplier endCondition;
//    IntakeSensorSubsystem.ColorList alliance;
//
//    public intakeForColorCommand(IntakeSubsystem intake, IntakeSensorSubsystem intakeSensorSubsystem, IntakeSensorSubsystem.ColorList alliance, BooleanSupplier endCondition){
//        this.intake = intake;
//        this.intakeSensor = intakeSensor;
//        this.alliance = alliance;
//        this.endCondition = endCondition;
//    }
//    public void execute(){
//        if(intakeSensor.readSensorColor() != alliance){
//            intake.setPower(-1);
//        } else {
//            intake.setPower(1);
//        }
//    }
//    @Override
//    public boolean isFinished(){
//        return endCondition.getAsBoolean();
//    }
//}

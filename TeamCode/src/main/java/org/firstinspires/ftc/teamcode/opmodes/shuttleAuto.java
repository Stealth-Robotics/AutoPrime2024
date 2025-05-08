//package org.firstinspires.ftc.teamcode.opModes;
//
//import com.arcrobotics.ftclib.command.Command;
//import com.arcrobotics.ftclib.command.CommandScheduler;
//import com.arcrobotics.ftclib.command.InstantCommand;
//import com.arcrobotics.ftclib.command.RunCommand;
//import com.arcrobotics.ftclib.command.SequentialCommandGroup;
//import com.arcrobotics.ftclib.command.WaitCommand;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//
//import org.firstinspires.ftc.teamcode.commands.deployIntakeCommand;
//import org.firstinspires.ftc.teamcode.commands.retractIntakeCommand;
//import org.firstinspires.ftc.teamcode.commands.reverseIntakeCommand;
//import org.firstinspires.ftc.teamcode.paths.BluePaths;
//import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
//import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
//import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.FlipperSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.IntakeSensorSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.LifterPanSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.LifterSubsystem;
//import org.firstinspires.ftc.teamcode.subsystems.ReacherSubsystem;
//import org.stealthrobotics.library.opmodes.StealthOpMode;
//
////This code is not used
//@Autonomous(name = "shuttleAuto")
//public class shuttleAuto extends StealthOpMode {
//    DriveSubsystem driveSubsystem;
//    IntakeSubsystem intakeSubsystem;
//    ReacherSubsystem reacherSubsystem;
//    FlipperSubsystem flipperSubsystem;
//    LifterSubsystem lifterSubsystem;
//    LifterPanSubsystem panSubsystem;
//    BluePaths bluePaths;
//    Follower follower;
//
//    static PathChain startToShuttle;
//    static PathChain shuttleFromStartPath;
//    static Pose shuttleStartingPose = new Pose(36.74, 65.84, 0);
//    final Pose shuttleFirstScorePose = new Pose(59.44,20.45, 0);
//    final Pose shuttleEndingPose = new Pose(19,22.52,Math.toRadians(180));
//    @Override
//    public void initialize(){
//        driveSubsystem = new DriveSubsystem(hardwareMap, telemetry);
//        intakeSubsystem = new IntakeSubsystem(hardwareMap);
//        reacherSubsystem = new ReacherSubsystem(hardwareMap, telemetry);
//        flipperSubsystem = new FlipperSubsystem(hardwareMap);
//        lifterSubsystem = new LifterSubsystem(hardwareMap, telemetry);
//        panSubsystem = new LifterPanSubsystem(hardwareMap);
//        follower = new Follower(hardwareMap);
//        //bluePaths = new BluePaths(follower);
//        //bluePaths.buildPaths();
//        startToShuttle = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(shuttleStartingPose), new Point(28.49,23.74,Point.CARTESIAN), new Point(69.49,44.53,Point.CARTESIAN), new Point(shuttleFirstScorePose)))
//                .setConstantHeadingInterpolation(shuttleStartingPose.getHeading())
//                .build();
//        shuttleFromStartPath = follower.pathBuilder()
//                .addPath(new BezierCurve(new Point(shuttleFirstScorePose), new Point(shuttleEndingPose)))
//                .setConstantHeadingInterpolation(shuttleFirstScorePose.getHeading())
//                .build();
//        register(driveSubsystem, reacherSubsystem);
//    }
//    @Override
//    public void whileWaitingToStart() {
//        CommandScheduler.getInstance().run();
//    }
//    @Override
//    public Command getAutoCommand(){
//        return new SequentialCommandGroup(
//                new InstantCommand(()-> driveSubsystem.setPose(shuttleStartingPose)),
//                driveSubsystem.FollowPath(startToShuttle, false),
//                new InstantCommand(()-> flipperSubsystem.goToPos(0.35)),
//                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.in)),
//                new InstantCommand(()-> flipperSubsystem.goToPos(0.25)),
//                new InstantCommand(()-> intakeSubsystem.setPower(1)),
//                new WaitCommand(1000),
//                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
//                new WaitCommand(1000),
//                new deployIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, true),
//                new WaitCommand(1000),
//                new retractIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, lifterSubsystem, panSubsystem),
//                new WaitCommand(1000),
//                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out)),
//                driveSubsystem.FollowPath(shuttleFromStartPath, true),
//                new deployIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, true),
//                new WaitCommand(1000),
//                new retractIntakeCommand(reacherSubsystem, flipperSubsystem, intakeSubsystem, lifterSubsystem, panSubsystem),
//                new WaitCommand(1000),
//                new InstantCommand(()-> panSubsystem.setPos(panSubsystem.out))
//        );
//    }
//}

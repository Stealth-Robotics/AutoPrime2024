package org.firstinspires.ftc.teamcode.paths;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

public class BluePaths {
    public static PathChain shuttleFromStartPath;
    public static PathChain parkFromHumanPlayerSpot;
    private final Pose shuttleStartingPose = new Pose(12.48,59.61, 0);
    private final Pose shuttleEndingPose = new Pose(19.93,18.54,Math.toRadians(225));
    private final Pose shuttleParkPose = new Pose(58.74,100.50,0);
    private final Pose bucketStartingPose = new Pose (9.787, 84.983, Math.toRadians(0));
    Follower follower;

    public BluePaths(Follower follower){
        this.follower = follower;
    }

    public void buildPaths(){
        shuttleFromStartPath = follower.pathBuilder()
            .addPath(new BezierCurve(new Point(shuttleStartingPose), new Point(51.84,47.83,Point.CARTESIAN), new Point(shuttleEndingPose)))
            .setLinearHeadingInterpolation(shuttleStartingPose.getHeading(), shuttleEndingPose.getHeading())
            .build();
        parkFromHumanPlayerSpot = follower.pathBuilder()
            .addPath(new BezierCurve(new Point(shuttleEndingPose), new Point(13.34,103.62,Point.CARTESIAN), new Point(shuttleParkPose)))
            .setLinearHeadingInterpolation(shuttleEndingPose.getHeading(),shuttleParkPose.getHeading())
            .build();
    }
}

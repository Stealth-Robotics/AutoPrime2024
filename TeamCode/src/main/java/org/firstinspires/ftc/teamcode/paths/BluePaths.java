package org.firstinspires.ftc.teamcode.paths;

import org.firstinspires.ftc.teamcode.pedroPathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;

public class BluePaths {
    public static PathChain shuttleFromStartPath;
    public static PathChain parkFromHumanPlayerSpot;
    public static Path driveToBucket, parkLeftFromBucket, parkRightFromBucket;
    private final Pose shuttleStartingPose = new Pose(12.48,59.61, 0);
    private final Pose shuttleEndingPose = new Pose(19.93,18.54,Math.toRadians(225));
    private final Pose shuttleParkPose = new Pose(58.74,100.50,0);
    private final Pose bucketStartingPose = new Pose(9.787, 84.983, 0);
    private final Pose bucketScoringPose = new Pose(14.74, 128.4, Math.toRadians(315));
    private final Pose parkLeft = new Pose(61.8,99.78,Math.toRadians(270));
    private final Pose parkRight = new Pose(83.06,98.65, Math.toRadians(270));
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
        driveToBucket = follower.pathBuilder()
            .addPath(new BezierCurve(new Point(bucketStartingPose), new Point(51.59, 102.9, Point.CARTESIAN), new Point(bucketScoringPose)))
            .setLinearHeadingInterpolation(bucketStartingPose.getHeading(), bucketScoringPose.getHeading())
            .build();
        parkLeftFromBucket = follower.pathBuilder()
            .addPath(new BezierCurve(new Point(bucketScoringPose), new Pose(20.41,98.08,Point.CARTESIAN), new Pose(65.2,12.29,Point.CARTESIAN), new Pose(parkLeft)))
            .setLinearHeadingInterpolation(bucketScoringPose.getHeading(), parkLeft.getHeading)
            .build();
        parkRightFromBucket = follower.pathBuilder()
            .addPath(new BezierCurve(new Point(bucketScoringPose), new Pose(86.61,129.54,Point.CARTESIAN), new Pose(parkRight)))
            .setLinearHeadingInterpolation(bucketScoringPose.getHeading(), parkRight.getHeading)
            .build();
    }
}

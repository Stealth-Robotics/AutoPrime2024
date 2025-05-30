package org.firstinspires.ftc.teamcode.pedroPathing.constants;

import com.pedropathing.localization.Localizers;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.util.CustomFilteredPIDFCoefficients;
import com.pedropathing.util.CustomPIDFCoefficients;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class FConstants {
    static {
        FollowerConstants.localizers = Localizers.OTOS;

        FollowerConstants.leftFrontMotorName = "leftFront";
        FollowerConstants.leftRearMotorName = "leftRear";
        FollowerConstants.rightFrontMotorName = "rightFront";
        FollowerConstants.rightRearMotorName = "rightRear";

        FollowerConstants.leftFrontMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.leftRearMotorDirection = DcMotorSimple.Direction.FORWARD;
        FollowerConstants.rightFrontMotorDirection = DcMotorSimple.Direction.REVERSE;
        FollowerConstants.rightRearMotorDirection = DcMotorSimple.Direction.REVERSE;

        FollowerConstants.mass = 14.3;

        FollowerConstants.xMovement = (55.557 + 56.9015 + 55.2927) / 3;
        FollowerConstants.yMovement = (40.5601 + 38.791 + 38.7759) / 3;

        FollowerConstants.forwardZeroPowerAcceleration = (-35.0321 -35.563 -35.8993) / 3;
        FollowerConstants.lateralZeroPowerAcceleration = (-78.7199 -81.1063 -81.4752) / 3;

        FollowerConstants.translationalPIDFCoefficients.setCoefficients(0.11,0,0.006,0);
        FollowerConstants.useSecondaryTranslationalPID = true;
        FollowerConstants.secondaryTranslationalPIDFCoefficients.setCoefficients(0.185,0,0.016,0);

        FollowerConstants.headingPIDFCoefficients.setCoefficients(-7,0,-0.7,0);
        FollowerConstants.useSecondaryHeadingPID = true;
        FollowerConstants.secondaryHeadingPIDFCoefficients.setCoefficients(-2,0,-0.1,0);

        FollowerConstants.drivePIDFCoefficients.setCoefficients(0.01,0,0.0002,0.6,0);
        FollowerConstants.useSecondaryDrivePID = true;
        FollowerConstants.secondaryDrivePIDFCoefficients.setCoefficients(0.02,0,0.0005,0.6,0);

        FollowerConstants.zeroPowerAccelerationMultiplier = 4.4;
        FollowerConstants.centripetalScaling = 0.0005;

        FollowerConstants.pathEndTimeoutConstraint = 500;
        FollowerConstants.pathEndTValueConstraint = 0.995;
        FollowerConstants.pathEndVelocityConstraint = 0.1;
        FollowerConstants.pathEndTranslationalConstraint = 0.1;
        FollowerConstants.pathEndHeadingConstraint = 0.007;
    }
}

package org.stealthrobotics.library;

public class AnglePIDController {
    private final double kP;
    private final double kI;
    private final double kD;

    private double reference;
    private double measuredValue;

    private double integralSum;
    private double lastError;

    private double tolerance;

    public AnglePIDController(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public void setTolerance(double newTolerance) {
        tolerance = newTolerance;
    }

    public void setSetPoint(double setPoint) {
        reference = setPoint;
    }

    public double getSetPoint() {
        return reference;
    }

    public boolean atSetPoint() {
        return Math.abs(reference - measuredValue) <= tolerance;
    }

    public double calculate(double measuredValue) {
        this.measuredValue = measuredValue;

        double time = (double) System.nanoTime() / 1E9;
        double error = wrapAngle(reference - measuredValue);
        double derivative = (error - lastError) / time;

        integralSum += error * time;
        lastError = error;

        return (kP * error) + (kI * integralSum) + (kD * derivative);
    }

    private double wrapAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle <= -180) angle += 360;
        return angle;
    }
}

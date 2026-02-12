package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.geometry.Pose;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import smile.interpolation.BilinearInterpolation;
import smile.interpolation.Interpolation2D;

import static org.firstinspires.ftc.teamcode.math.PoseMirror.mirror;

public class WaveLength {
    private static final Interpolation2D farInterpolation = new BilinearInterpolation(
            new double[]{43, 71, 100},
            new double[]{6, 27},
            new double[][]{
                    {1700, 1635},
                    {1675, 1520},
                    {1620, 1520}
            });

    private static final Interpolation2D farTurretInterpolation = new BilinearInterpolation(
            new double[]{43, 71, 100},
            new double[]{6, 27},
            new double[][]{
                    {45, 48},
                    {62, 58},
                    {76, 72}
            });

    private static final Interpolation2D closeInterpolation = new BilinearInterpolation(
            new double[]{38, 61, 85},
            new double[]{135.5, 111, 88, 63},
            new double[][]{
                    {1440, 1450, 1470, 1520},
                    {1375, 1380, 1390, 1415},
                    {1338, 1325, 1330, 1350},
            }
    );

    private static final Interpolation2D closeTurretInterpolation = new BilinearInterpolation(
            new double[]{38, 61, 85},
            new double[]{135.5, 111, 88, 63},
            new double[][]{
                    {0, 2.8, 27.5, 37.5},
                    {3, 16.5, 32, 45},
                    {4.5, 26, 39, 56},
            }
    );

    public static double getVelocityWithInterpolation(Pose currentPosition, Alliance alliance) {
        Pose pose = alliance == Alliance.RED ? currentPosition : mirror(currentPosition);
        if (currentPosition.getY() > 48) {
            return closeInterpolation.interpolate(pose.getX(), pose.getY());
        } else {
            return farInterpolation.interpolate(pose.getX(), pose.getY());
        }
    }

    public static double getAngleWithInterpolation(Pose currentPosition, Alliance alliance) {
        Pose pose = alliance == Alliance.RED ? currentPosition : mirror(currentPosition);
        double angle;
        if (currentPosition.getY() > 48) {
            angle = closeTurretInterpolation.interpolate(pose.getX(), pose.getY());
        } else {
            angle = farTurretInterpolation.interpolate(pose.getX(), pose.getY());
        }
        return alliance == Alliance.RED ? angle : 180 - angle;
    }
}

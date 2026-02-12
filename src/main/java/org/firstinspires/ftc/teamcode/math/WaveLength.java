package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.geometry.Pose;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import smile.interpolation.BilinearInterpolation;
import smile.interpolation.Interpolation2D;

import static org.firstinspires.ftc.teamcode.math.PoseMirror.mirror;

public class WaveLength {
    private static final Interpolation2D farInterpolation = new BilinearInterpolation(
            new double[]{43, 71, 0},
            new double[]{4, 0},
            new double[][]{
                    {0, 0},
                    {0, 0},
                    {0, 0}
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

    private static final Interpolation2D closeTurretInterpolationDegrees = new BilinearInterpolation(
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
        return closeInterpolation.interpolate(pose.getX(), pose.getY());
    }

    public static double getAngleWithInterpolation(Pose currentPosition, Alliance alliance) {
        Pose pose = alliance == Alliance.RED ? currentPosition : mirror(currentPosition);
        double angle =  closeTurretInterpolationDegrees.interpolate(pose.getX(), pose.getY());
        return alliance == Alliance.RED ? angle : 180 - angle;
    }
}

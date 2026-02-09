package org.firstinspires.ftc.teamcode.math;

import com.pedropathing.geometry.Pose;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;
import smile.interpolation.BilinearInterpolation;
import smile.interpolation.Interpolation2D;

public class WaveLength {
    private static final Interpolation2D farInterpolation = new BilinearInterpolation(
            new double[]{43, 71, 0},
            new double[]{4, 0},
            new double[][]{
                    {1730, 0},
                    {1680, 0},
                    {0, 0}
            });

    private static final Interpolation2D closeInterpolation = new BilinearInterpolation(
            new double[]{38, 61, 85},
            new double[]{135, 111, 88, 63},
            new double[][]{
                    {1470, 1470, 1500, 1540},
                    {1330, 1380, 1420, 1470},
                    {1280, 1270, 1290, 1410}
            }
    );

    public static double getVelocityWithInterpolation(Pose currentPosition) {
        return closeInterpolation.interpolate(currentPosition.getX(), currentPosition.getY());
    }
}

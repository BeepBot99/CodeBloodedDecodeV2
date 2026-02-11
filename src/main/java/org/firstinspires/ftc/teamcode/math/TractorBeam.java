package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class TractorBeam {
    public static double minimumAngleDegrees = -90;

    public static void aimTurret(Pose currentPose, Robot robot, Alliance alliance) {
//        double targetAngleRadians = Math.atan2(alliance.goal.getY() - currentPose.getY(), alliance.goal.getX() - currentPose.getX());
        double targetAngleDegrees = WaveLength.getAngleWithInterpolation(currentPose, alliance);

//        double turretTargetRadians = AngleUnit.normalizeRadians(targetAngleRadians - currentPose.getHeading());
        double turretTargetDegrees = AngleUnit.normalizeDegrees(targetAngleDegrees - Math.toDegrees(currentPose.getHeading()));

//        double turretTargetDegrees = Math.toDegrees(turretTargetRadians);

        if (turretTargetDegrees < minimumAngleDegrees) {
            turretTargetDegrees += 360;
        }

        robot.telemetry.addData("Auto Aim Target", targetAngleDegrees);
        robot.telemetry.addData("Auto Aim Turret Target", turretTargetDegrees);

        robot.turret.setTargetDegrees(turretTargetDegrees);
    }

    public static void normalize(double angle) {

    }
}

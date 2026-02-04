package org.firstinspires.ftc.teamcode.math;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class TractorBeam {
    public static double maximumAngleDegrees = 145;

    public static void aimTurret(Robot robot, Alliance alliance) {
        Pose currentPose = robot.drivetrain.getPose();
        double targetAngleRadians = Math.atan2(alliance.goal.getY() - currentPose.getY(), alliance.goal.getX() - currentPose.getX());

        double turretTargetRadians = AngleUnit.normalizeRadians(targetAngleRadians - currentPose.getHeading());

        double turretTargetDegrees = Math.toDegrees(turretTargetRadians);

        if (turretTargetDegrees > maximumAngleDegrees) {
            turretTargetDegrees = turretTargetDegrees - 360;
        }

        robot.telemetry.addData("Auto Aim Target", Math.toDegrees(targetAngleRadians));
        robot.telemetry.addData("Auto Aim Turret Target", turretTargetDegrees);

        robot.turret.setTargetDegrees(turretTargetDegrees);
    }
}

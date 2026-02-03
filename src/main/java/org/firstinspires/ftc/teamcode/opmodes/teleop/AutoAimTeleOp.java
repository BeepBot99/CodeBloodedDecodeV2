package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name = "Auto Aim TeleOp", group = "Testing")
public class AutoAimTeleOp extends RobotOpMode {
    private static final Pose GOAL = new Pose(140, 140);

    @Override
    public void init() {
        super.init();

        robot.drivetrain.setStartingPose(new Pose(7.5, 7.6));
    }

    @Override
    public void loop() {
        Pose currentPose = robot.drivetrain.getPose();
        double targetAngle = Math.atan2(GOAL.getY() - currentPose.getY(), GOAL.getX() - currentPose.getX());

        double turretTarget = AngleUnit.normalizeRadians(targetAngle - currentPose.getHeading());

        robot.telemetry.addData("Auto Aim Target", Math.toDegrees(targetAngle));
        robot.telemetry.addData("Auto Aim Turret Target", Math.toDegrees(turretTarget));

        robot.turret.setTarget(turretTarget);

        super.loop();
    }
}

package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.math.TractorBeam;
import org.firstinspires.ftc.teamcode.math.TurretLocation;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name = "Testing TeleOp", group = "Testing")
@Config
public class TestingTeleOp extends RobotOpMode {

    @Override
    public void init() {
        super.init();

        robot.drivetrain.setStartingPose(new Pose(7.5, 7.6, Math.PI / 2));
    }

    @Override
    public void loop() {
        robot.drivetrain.arcadeDrive(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );

        Pose turretPose = TurretLocation.getTurretPose(robot.drivetrain.getPose());

        TractorBeam.aimTurret(turretPose, robot, Alliance.RED);

        if (gamepad1.rightTriggerWasPressed()) {
            robot.blocker.unblock();
            robot.intake.slowDown();
        }

        if (gamepad1.rightTriggerWasReleased()) {
            robot.blocker.block();
            robot.intake.speedUp();
        }
        if (gamepad1.leftTriggerWasPressed()) robot.blocker.assembly();

        if (gamepad1.rightBumperWasPressed()) robot.intake.toggle().schedule();
        if (gamepad1.leftBumperWasPressed()) robot.intake.shortReverse().schedule();

        if (gamepad1.triangleWasPressed()) robot.flywheel.toggle();

        if (gamepad2.crossWasPressed()) robot.turret.home();
        if (gamepad2.triangleWasPressed()) robot.turret.forceOff();

        if (gamepad2.squareWasPressed()) robot.drivetrain.setPose(new Pose(7.5, 7.6, Math.PI / 2));

        robot.telemetry.addData("Turret X", turretPose.getX());
        robot.telemetry.addData("Turret Y", turretPose.getY());

        super.loop();
    }
}

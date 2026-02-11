package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.math.TractorBeam;
import org.firstinspires.ftc.teamcode.math.TurretLocation;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

import static org.firstinspires.ftc.teamcode.math.PoseMirror.mirror;

@TeleOp(name = "Testing TeleOp", group = "Testing")
@Config
public class TestingTeleOp extends RobotOpMode {

    @Override
    public void init() {
        super.init();

        robot.drivetrain.usePreviousStartingPose();
    }

    @Override
    public void start() {
        robot.blocker.block();
        robot.intake.on().schedule();
    }

    @Override
    public void loop() {
        if (Math.abs(gamepad1.right_stick_x) < 0.1) robot.drivetrain.lockHeading();
        else robot.drivetrain.unlockHeading();


        robot.drivetrain.arcadeDrive(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                Alliance.current
        );

        Pose turretPose = TurretLocation.getTurretPose(robot.drivetrain.getPose());

        TractorBeam.aimTurret(turretPose, robot, Alliance.current);

        if (gamepad1.rightTriggerWasPressed()) {
            robot.blocker.unblock();
            robot.intake.slowDown();
        }

        if (gamepad1.rightTriggerWasReleased()) {
            robot.blocker.block();
            robot.intake.speedUp();
        }
        if (gamepad1.leftTriggerWasPressed()) robot.blocker.assembly();

//        if (gamepad1.rightBumperWasPressed()) robot.intake.toggle().schedule();
        if (gamepad1.rightBumperWasPressed()) robot.intake.off();
        if (gamepad1.rightBumperWasReleased()) robot.intake.on();
        if (gamepad1.leftBumperWasPressed()) robot.intake.shortReverse().schedule();

        if (gamepad1.triangleWasPressed()) robot.flywheel.toggle();

//        if (gamepad2.crossWasPressed()) robot.turret.home();
        if (gamepad2.triangleWasPressed()) robot.turret.forceOff = !robot.turret.forceOff;

        if (gamepad2.leftBumperWasPressed()) Alliance.current = Alliance.RED;
        if (gamepad2.rightBumperWasPressed()) Alliance.current = Alliance.BLUE;

        if (gamepad2.crossWasPressed()) robot.drivetrain.setPose(
                Alliance.current == Alliance.RED ? new Pose(7.5, 7.6, 0) : mirror(new Pose(7.5, 7.6, 0))
        );

        if (gamepad2.circleWasPressed()) robot.drivetrain.setPose(robot.drivetrain.getPose().withHeading(
                Alliance.current == Alliance.RED ? 0 : Math.PI
        ));

        if (gamepad2.squareWasPressed()) robot.drivetrain.gateHeading(Alliance.current);

        if (gamepad2.dpadLeftWasPressed()) robot.turret.moveLeft();
        if (gamepad2.dpadRightWasPressed()) robot.turret.moveRight();

        robot.telemetry.addData("Turret X", turretPose.getX());
        robot.telemetry.addData("Turret Y", turretPose.getY());

        super.loop();
    }
}

package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name = "PTO Testing", group = "Testing")
@Config
public class PtoTesting extends RobotOpMode {
    public static double power = 0.3;

    @Override
    public void loop() {
        if (gamepad1.dpadUpWasPressed()) robot.pto.disengageLeft();
        if (gamepad1.dpadDownWasPressed()) robot.pto.engageLeft();
        if (gamepad1.triangleWasPressed()) robot.pto.disengageRight();
        if (gamepad1.crossWasPressed()) robot.pto.engageRight();

        if (gamepad1.rightBumperWasPressed()) {
            robot.drivetrain.frontRight.setPower(-power);
            robot.drivetrain.frontLeft.setPower(-power);
            robot.drivetrain.backRight.setPower(power);
            robot.drivetrain.backLeft.setPower(power);
        }

        if (gamepad1.leftBumperWasPressed()) {
            robot.drivetrain.frontRight.setPower(0);
            robot.drivetrain.frontLeft.setPower(0);
            robot.drivetrain.backRight.setPower(0);
            robot.drivetrain.backLeft.setPower(0);
        }

        robot.telemetry.addData("front left current", robot.drivetrain.frontLeft.getCurrent(CurrentUnit.AMPS));
        robot.telemetry.addData("front right current", robot.drivetrain.frontRight.getCurrent(CurrentUnit.AMPS));
        robot.telemetry.addData("back left current", robot.drivetrain.backLeft.getCurrent(CurrentUnit.AMPS));
        robot.telemetry.addData("back right current", robot.drivetrain.backRight.getCurrent(CurrentUnit.AMPS));

        super.loop();
    }
}

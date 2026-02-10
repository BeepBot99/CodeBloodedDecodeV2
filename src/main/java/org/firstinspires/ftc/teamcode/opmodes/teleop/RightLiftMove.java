package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp
public class RightLiftMove extends RobotOpMode {
    @Override
    public void start() {
        robot.drivetrain.frontRight.setPower(-0.3);
        robot.drivetrain.backRight.setPower(0.3);
    }
}

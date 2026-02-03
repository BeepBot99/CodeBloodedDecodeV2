package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name = "Competition TeleOp", group = "Competition")
public class CompetitionTeleOp extends RobotOpMode {
    @Override
    public void loop() {
//        robot.drivetrain.arcadeDrive(
//                -gamepad1.left_stick_y,
//                gamepad1.left_stick_x,
//                gamepad1.right_stick_x
//        );

        if (gamepad1.rightTriggerWasPressed()) robot.blocker.unblock();
        if (gamepad1.rightTriggerWasReleased()) robot.blocker.block();

//        if (gamepad1.rightBumperWasPressed()) robot.intake.toggle().schedule();
//        if (gamepad1.leftBumperWasPressed()) robot.intake.shortReverse().schedule();
//
//        if (gamepad1.triangleWasPressed()) robot.flywheel.toggle();

        super.loop();
    }
}

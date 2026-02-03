package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

@TeleOp(name = "Testing TeleOp", group = "Testing")
@Config
public class TestingTeleOp extends RobotOpMode {

    public static boolean turretOverride = false;
    public static double turretTarget = 0;
    public static boolean turretWaveEnabled = false;
    public static double turretWavePeriod = 1;
    public static double turretWaveHigh = 90;
    public static double turretWaveLow = -5;
    private final ElapsedTime turretTimer = new ElapsedTime();

    @Override
    public void loop() {
        robot.drivetrain.arcadeDrive(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );

        if (gamepad1.rightTriggerWasPressed()) robot.blocker.unblock();
        if (gamepad1.rightTriggerWasReleased()) robot.blocker.block();
        if (gamepad1.leftTriggerWasPressed()) robot.blocker.assembly();

        if (gamepad1.rightBumperWasPressed()) robot.intake.toggle().schedule();
        if (gamepad1.leftBumperWasPressed()) robot.intake.shortReverse().schedule();

        if (gamepad1.triangleWasPressed()) robot.flywheel.toggle();

        if (turretOverride) {
            if (turretWaveEnabled) {
                if (turretTimer.seconds() > turretWavePeriod / 2) {
                    turretTimer.reset();
                    turretTarget = robot.turret.getTarget() == turretWaveLow ? turretWaveHigh : turretWaveLow;
                }
            }
            robot.turret.setTarget(turretTarget);
        }

        if (gamepad2.crossWasPressed()) robot.turret.home();

        super.loop();
    }
}

package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.ivy.Scheduler.schedule;

public abstract class RobotOpMode extends OpMode {
    protected Robot robot;

    @Override
    public final void init() {
        Scheduler.reset();
        robot = new Robot(this);

        schedule(/*robot.drivetrain.periodic(),*/ robot.flywheel.periodic(), robot.turret.periodic(), robot.tapeSensor.periodic(), robot.intake.periodic());
    }

    @Override
    public final void init_loop() {
        Scheduler.execute();
    }
}

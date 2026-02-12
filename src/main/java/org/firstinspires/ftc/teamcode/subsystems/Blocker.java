package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class Blocker {
    public static double blockPosition = 0.17;
    public static double unblockPosition = 0.28;
    public static double assemblyPosition = 0;

    private final Servo blockerServo;

    public Blocker(Robot robot) {
        blockerServo = robot.hardwareMap.get(Servo.class, "blocker");
    }

    public void block() {
        blockerServo.setPosition(blockPosition);
    }

    public void unblock() {
        blockerServo.setPosition(unblockPosition);
    }

    public void assembly() {
        blockerServo.setPosition(assemblyPosition);
    }
}

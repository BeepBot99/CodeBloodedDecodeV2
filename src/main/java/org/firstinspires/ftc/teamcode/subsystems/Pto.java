package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.robot.Robot;

@Config
public class Pto {
    public static double leftUnengagedPosition = 0.515;
    public static double rightUnengagedPosition = 0.465;
    public static double leftEngagedPosition = 0.475;
    public static double rightEngagedPosition = 0.506;


    private final Servo leftPtoServo;
    private final Servo rightPtoServo;

    public Pto(Robot robot) {
        leftPtoServo = robot.hardwareMap.get(Servo.class, "leftPto");
        rightPtoServo = robot.hardwareMap.get(Servo.class, "rightPto");
    }

    public void engageLeft() {
        leftPtoServo.setPosition(leftEngagedPosition);
    }

    public void engageRight() {
        rightPtoServo.setPosition(rightEngagedPosition);
    }

    public void disengageLeft() {
        leftPtoServo.setPosition(leftUnengagedPosition);
    }

    public void disengageRight() {
        rightPtoServo.setPosition(rightUnengagedPosition);
    }

    public void engage() {
        engageLeft();
        engageRight();
    }

    public void disengage() {
        disengageLeft();
        disengageRight();
    }
}

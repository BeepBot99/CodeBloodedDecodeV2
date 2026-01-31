package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.AnalogInput;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

import static com.pedropathing.ivy.commands.Commands.infinite;

public class TapeSensor {
    private final AnalogInput sensor1;
    private final Telemetry telemetry;

    public TapeSensor(Robot robot) {
        sensor1 = robot.hardwareMap.get(AnalogInput.class, "tapeSensor1");
        telemetry = robot.telemetry;
    }

    private double getSensor1Hue() {
        return sensor1.getVoltage() / 3.3 * 360;
    }

    public Command periodic() {
        return infinite(() -> {
            telemetry.addData("Tape Sensor 1 Hue", getSensor1Hue());
        });
    }
}

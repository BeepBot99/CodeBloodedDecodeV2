package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.robot.Robot;

import static com.pedropathing.ivy.commands.Commands.*;

@Config
public class Intake {
    public double onPower = -1;
    public static double normalPower = -1;
    public static double shootingPower = -0.7;
    public static double offPower = 0;
    public static double reversePower = 1;
    public static double shortReverseTimeMs = 150;

    private final DcMotorEx intakeMotor;

    private final Telemetry telemetry;

    public Intake(Robot robot) {
        intakeMotor = robot.hardwareMap.get(DcMotorEx.class, "intake");
        telemetry = robot.telemetry;
    }

    public Command on() {
        return instant(() -> intakeMotor.setPower(onPower)).requiring(intakeMotor);
    }

    public Command off() {
        return instant(() -> intakeMotor.setPower(offPower)).requiring(intakeMotor);
    }

    public Command reverse() {
        return instant(() -> intakeMotor.setPower(reversePower)).requiring(intakeMotor);
    }

    public Command shortReverse() {
        return reverse().then(waitMs(shortReverseTimeMs)).then(on());
    }

    public Command toggle() {
        return conditional(() -> intakeMotor.getPower() == offPower, on(), off());
    }

    public Command periodic() {
        return infinite(() -> {
            telemetry.addData("Intake Current", intakeMotor.getCurrent(CurrentUnit.MILLIAMPS));
        });
    }
}

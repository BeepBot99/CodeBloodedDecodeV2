package org.firstinspires.ftc.teamcode.subsystems;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;
import com.pedropathing.ivy.Command;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.Robot;

import static com.pedropathing.ivy.commands.Commands.infinite;

@Config
public class Turret {
    private static final double TICKS_PER_REVOLUTION = 384.5 * 3;
    public static PIDFCoefficients coefficients = new PIDFCoefficients(0.0112, 0, 0.00047, 0);
    public static double homedAngle = 0;
    public static double homingPower = -0.18;
    private final DcMotorEx turretMotor;
    private final Telemetry telemetry;
    private final PIDFController controller = new PIDFController(coefficients);
    private final TouchSensor limitSwitch;
    private double target = 0;
    private double power = 0;
    private Mode mode = Mode.OFF;
    private double angleOffset = 0;

    public Turret(Robot robot) {
        turretMotor = robot.hardwareMap.get(DcMotorEx.class, "turret");
        telemetry = robot.telemetry;

        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        limitSwitch = robot.hardwareMap.get(TouchSensor.class, "turretMagnet");
    }

    private double getRawAngle() {
        return turretMotor.getCurrentPosition() / TICKS_PER_REVOLUTION * 360;

    }

    private double getAngle() {
        return getRawAngle() + angleOffset;
    }

    private void setAngle(double angle) {
        angleOffset = angle - getRawAngle();
    }

    public void off() {
        mode = Mode.OFF;
        power = 0;
    }

    public void setPower(double power) {
        mode = Mode.POWER;
        this.power = power;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        mode = Mode.POSITION;
        this.target = target;
    }

    public void home() {
        mode = Mode.HOME;
    }

    public Command periodic() {
        return infinite(() -> {
            switch (mode) {
                case POSITION:
                    controller.updateError(target - getAngle());
                    turretMotor.setPower(controller.run() * 0.5);
                    break;
                case POWER:
                    turretMotor.setPower(power);
                    break;
                case OFF:
                    turretMotor.setPower(0);
                    break;
                case HOME:
                    if (limitSwitch.isPressed()) {
                        turretMotor.setPower(0);
                        setAngle(homedAngle);
                        mode = Mode.OFF;
                    } else {
                        turretMotor.setPower(homingPower);
                    }
                    break;
            }

            telemetry.addData("Turret Angle", getAngle());
            telemetry.addData("Turret Target", target);
            telemetry.addData("Turret Power", turretMotor.getPower());
            telemetry.addData("Turret Magnet Activated", limitSwitch.isPressed());
            telemetry.addData("Turret Mode", mode);
            telemetry.addData("Turret Velocity", turretMotor.getVelocity());
        });
    }

    public enum Mode {
        POSITION,
        POWER,
        OFF,
        HOME
    }
}

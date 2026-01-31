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
    public static double positiveVelocityMagnetPosition = 0;
    public static double negativeVelocityMagnetPosition = 5;
    public static boolean useMagnet = false;
    public static PIDFCoefficients coefficients = new PIDFCoefficients(0.0112, 0, 0.00047, 0);
    private final DcMotorEx turretMotor;
    private final Telemetry telemetry;
    private final PIDFController controller = new PIDFController(coefficients);
    private final TouchSensor limitSwitch;
    private double target = 0;
    private double power = 0;
    private Mode mode = Mode.OFF;
    private boolean magnetActivated = false;
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

    public Command periodic() {
        return infinite(() -> {
            if (useMagnet && magnetActivated && !limitSwitch.isPressed()) {
                if (turretMotor.getVelocity() > 0) {
                    setAngle(positiveVelocityMagnetPosition);
                } else {
                    setAngle(negativeVelocityMagnetPosition);
                }
            }

            switch (mode) {
                case POSITION:
                    controller.updateError(target - getAngle());
                    turretMotor.setPower(controller.run());
                    break;
                case POWER:
                    turretMotor.setPower(power);
                    break;
                case OFF:
                    turretMotor.setPower(0);
                    break;
            }

            telemetry.addData("Turret Angle (Robot-Frame)", getAngle());
            telemetry.addData("Turret Target", target);
            telemetry.addData("Turret Power", turretMotor.getPower());
            telemetry.addData("Turret Magnet Activated", limitSwitch.isPressed());
            telemetry.addData("Turret Mode", mode);
            telemetry.addData("Turret Velocity", turretMotor.getVelocity());

            magnetActivated = limitSwitch.isPressed();
        });
    }

    private enum Mode {
        POSITION,
        POWER,
        OFF
    }
}

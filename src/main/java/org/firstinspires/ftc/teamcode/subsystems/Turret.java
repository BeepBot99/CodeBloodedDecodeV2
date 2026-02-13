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
    private static double angleTransfer = 0;
    public static double incrementDegrees = 2.5;
    public static PIDFCoefficients coefficients = new PIDFCoefficients(0.06, 0, 0.0013, 0);
    public static double homedAngleDegrees = 145;
    public static double homingPower = 0.25;
    private final DcMotorEx turretMotor;
    private final Telemetry telemetry;
    private final PIDFController controller = new PIDFController(coefficients);
    private final TouchSensor limitSwitch;
    public boolean forceOff = false;
    private double targetDegrees = 0;
    private Mode mode = Mode.OFF;
    private double angleOffsetDegrees = 0;

    public Turret(Robot robot) {
        turretMotor = robot.hardwareMap.get(DcMotorEx.class, "turret");
        telemetry = robot.telemetry;

        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        limitSwitch = robot.hardwareMap.get(TouchSensor.class, "turretMagnet");
    }

    private double getRawAngleDegrees() {
        return turretMotor.getCurrentPosition() / TICKS_PER_REVOLUTION * 360;
    }

    private double getAngleDegrees() {
        return getRawAngleDegrees() + angleOffsetDegrees;
    }

    private void setAngleDegrees(double angle) {
        angleOffsetDegrees = angle - getRawAngleDegrees();
    }

    public static void localize(double angle) {
        angleTransfer = angle;
    }

    public void off() {
        if (mode == Mode.HOME) return;
        mode = Mode.OFF;
    }

    public double getTargetDegrees() {
        return targetDegrees;
    }

    public void setTargetDegrees(double targetDegrees) {
        if (mode == Mode.HOME) return;
        mode = Mode.POSITION;
        this.targetDegrees = targetDegrees;
    }

    public void home() {
        mode = Mode.HOME;
    }

    public void moveLeft() {
        setAngleDegrees(getAngleDegrees() - incrementDegrees);
    }

    public void moveRight() {
        setAngleDegrees(getAngleDegrees() + incrementDegrees);
    }

    public void setStartingAngle(double angle) {
        setAngleDegrees(angle);
    }

    public void usePreviousStartingAngle() {
        setAngleDegrees(angleTransfer);
    }

    public Command periodic() {
        return infinite(() -> {
            if (forceOff) turretMotor.setPower(0);
            else {
                switch (mode) {
                    case POSITION:
                        controller.updateError(targetDegrees - getAngleDegrees());
                        turretMotor.setPower(controller.run() * 0.5);
                        break;
                    case OFF:
                        turretMotor.setPower(0);
                        break;
                    case HOME:
                        if (limitSwitch.isPressed()) {
                            turretMotor.setPower(0);
                            setAngleDegrees(homedAngleDegrees);
                            mode = Mode.OFF;
                        } else {
                            turretMotor.setPower(homingPower);
                        }
                        break;
                }
            }

            angleTransfer = getAngleDegrees();

            telemetry.addData("Turret Angle", getAngleDegrees());
            telemetry.addData("Turret Target", targetDegrees);
            telemetry.addData("Turret Power", turretMotor.getPower());
            telemetry.addData("Turret Magnet Activated", limitSwitch.isPressed());
            telemetry.addData("Turret Mode", mode);
            telemetry.addData("Turret Velocity", turretMotor.getVelocity());
            telemetry.addData("Turret Forced Off", forceOff ? "Yes" : "No");
        });
    }

    public enum Mode {
        POSITION,
        OFF,
        HOME
    }
}

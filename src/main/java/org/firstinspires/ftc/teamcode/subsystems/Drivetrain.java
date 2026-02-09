package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robot.Robot;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

public class Drivetrain {
    private static Pose poseTransfer = new Pose();

    private final Follower follower;
    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;

    private final Telemetry telemetry;

    public Drivetrain(Robot robot) {
        follower = Constants.createFollower(robot.hardwareMap);
        frontLeft = robot.hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = robot.hardwareMap.get(DcMotorEx.class, "frontRight");
        backLeft = robot.hardwareMap.get(DcMotorEx.class, "backLeft");
        backRight = robot.hardwareMap.get(DcMotorEx.class, "backRight");

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry = robot.telemetry;
    }

    private static double signedSquare(double raw) {
        return Math.signum(raw) * Math.pow(raw, 2);
    }

    public void arcadeDrive(double forward, double strafe, double turn) {
        forward = signedSquare(forward);
        strafe = signedSquare(strafe);
        turn = signedSquare(turn);

        double x = strafe * Math.cos(follower.getHeading()) + forward * Math.sin(follower.getHeading());
        double y = strafe * -Math.sin(follower.getHeading()) + forward * Math.cos(follower.getHeading());
//        double x = strafe;
//        double y = forward;
        y *= 1.1;

        double denominator = Math.max(Math.abs(x) + Math.abs(y) + Math.abs(turn), 1);

        frontLeft.setPower((y + x + turn) / denominator);
        frontRight.setPower((y - x - turn) / denominator);
        backLeft.setPower((y - x + turn) / denominator);
        backRight.setPower((y + x - turn) / denominator);
    }

    public Pose getPose() {
        return follower.getPose();
    }

    public void setStartingPose(Pose pose) {
        follower.setStartingPose(pose);
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }

    public void usePreviousStartingPose() {
        setStartingPose(poseTransfer);
    }

    public Command followPath(PathChain path) {
        return follow(follower, path);
    }

    public Command periodic() {
        return infinite(() -> {
            follower.update();
            poseTransfer = follower.getPose();

            telemetry.addData("Current X", follower.getPose().getX());
            telemetry.addData("Current Y", follower.getPose().getY());
            telemetry.addData("Current Heading", follower.getHeading());
        });
    }
}

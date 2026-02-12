package org.firstinspires.ftc.teamcode.opmodes.autos;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.math.TractorBeam;
import org.firstinspires.ftc.teamcode.math.TurretLocation;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.robot.RobotOpMode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.sequential;

@Autonomous(name = "Red Close 15", group = "Competition")
public class RedClose15 extends RobotOpMode {
    private Paths paths;

    private static Pose transformed(double x, double y) {
        return new Pose(
                x - 1.5 * Math.cos(Math.toRadians(36)),
                y - 1.5 * Math.sin(Math.toRadians(36))
        );
    }

    @Override
    public void init() {
        super.init();

        robot.blocker.block();

        Alliance.current = Alliance.RED;

        robot.drivetrain.setStartingPose(transformed(120, 126).withHeading(Math.toRadians(216)));
        robot.turret.setStartingAngle(0);

        paths = new Paths(robot.drivetrain.follower);
    }

    private Command aimForPose(Pose pose) {
        return instant(() -> {
            Pose turretLocation = TurretLocation.getTurretPose(pose);

            robot.turret.setTargetDegrees(
                    TractorBeam.getTurretTargetDegrees(turretLocation, robot.telemetry, Alliance.current)
            );
        });
    }

    private Command aimForPath(PathChain path) {
        return aimForPose(path.endPose().withHeading(path.getFinalHeadingGoal()));
    }

    @Override
    public void start() {
        robot.flywheel.setTarget(1340);

        schedule(
                sequential(
                        instant(robot.flywheel::turnOn),
                        aimForPath(paths.toFirstShoot),
                        robot.drivetrain.followPath(paths.toFirstShoot),
                        robot.intake.on(),
                        waitUntil(robot.flywheel::atTarget),
                        shoot(),
                        robot.drivetrain.followPath(paths.middleRowIntake),
                        robot.intake.off(),
                        aimForPath(paths.toSecondShoot),
                        robot.drivetrain.followPath(paths.toSecondShoot),
                        robot.intake.on(),
                        shoot(),
                        robot.drivetrain.followPath(paths.toGateIntake),
                        waitMs(75),
                        instant(() -> robot.drivetrain.follower.setMaxPower(0.7)),
                        robot.drivetrain.followPath(paths.awayFromGate),
                        instant(() -> robot.drivetrain.follower.setMaxPower(1)),
                        waitMs(1250),
                        robot.intake.off(),
                        aimForPath(paths.toThirdShoot),
                        robot.drivetrain.followPath(paths.toThirdShoot),
                        robot.intake.on(),
                        shoot(),
                        instant(() -> robot.drivetrain.follower.setMaxPower(0.6)),
                        robot.drivetrain.followPath(paths.firstRowIntake),
                        instant(() -> robot.drivetrain.follower.setMaxPower(1)),
                        robot.intake.off(),
                        aimForPath(paths.toFourthShoot),
                        robot.drivetrain.followPath(paths.toFourthShoot),
                        robot.intake.on(),
                        shoot(),
                        robot.drivetrain.followPath(paths.lastRowIntake),
                        robot.intake.off(),
                        instant(() -> robot.flywheel.setTarget(1365)),
                        aimForPath(paths.toFifthShoot),
                        robot.drivetrain.followPath(paths.toFifthShoot),
                        robot.intake.on(),
                        shoot(),
                        // end
                        instant(robot.flywheel::turnOff),
                        robot.intake.off()
                )
        );
    }

    private Command shoot() {
        return sequential(
                instant(robot.blocker::unblock),
                waitMs(1000),
                instant(robot.blocker::block)
        );
    }

    private static class Paths {
        public final PathChain toFirstShoot;
        public final PathChain middleRowIntake;
        public final PathChain toSecondShoot;
        public final PathChain toGateIntake;
        public final PathChain awayFromGate;
        public final PathChain toThirdShoot;
        public final PathChain firstRowIntake;
        public final PathChain toFourthShoot;
        public final PathChain lastRowIntake;
        public final PathChain toFifthShoot;

        public Paths(Follower follower) {
            toFirstShoot = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(120, 126),
                            transformed(87, 83)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(216), Math.toRadians(300))
                    .build();

            middleRowIntake = follower.pathBuilder()
                    .addPath(new BezierCurve(
                            transformed(87, 83),
                            transformed(90, 58),
                            transformed(125, 58)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(0))
                    .build();

            toSecondShoot = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(125, 58),
                            transformed(87, 83)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(300))
                    .build();

            toGateIntake = follower.pathBuilder()
                    .addPath(new BezierCurve(
                            transformed(87, 83),
                            transformed(110, 64),
                            transformed(120, 64.75)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(300), Math.toRadians(0))
                    .build();

            awayFromGate = follower.pathBuilder()
                    .addPath(new BezierCurve(
                            transformed(120, 64.75),
                            transformed(120, 50),
                            transformed(126, 49)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(36.5))
                    .build();

            toThirdShoot = follower.pathBuilder()
                    .addPath(new BezierCurve(
                            transformed(126, 49),
                            transformed(96, 48),
                            transformed(87, 83)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(36.5), Math.toRadians(0))
                    .build();

            firstRowIntake = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(87, 83),
                            transformed(123, 83)
                    ))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();

            toFourthShoot = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(123, 83),
                            transformed(87, 83)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(300))
                    .build();

            lastRowIntake = follower.pathBuilder()
                    .addPath(new BezierCurve(
                            transformed(87, 83),
                            transformed(85, 35),
                            transformed(125, 34)
                    ))
                    .setTangentHeadingInterpolation()
                    .build();

            toFifthShoot = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(125, 34),
                            transformed(84, 103)
                    ))
                    .setTangentHeadingInterpolation()
                    .build();
        }
    }
}

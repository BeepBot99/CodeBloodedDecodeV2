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
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static org.firstinspires.ftc.teamcode.math.PoseMirror.mirror;

@Autonomous(name = "Blue Far 9", group = "Competition")
public class BlueFar9 extends RobotOpMode {
    private Paths paths;

    private static Pose transformed(double x, double y) {
        return mirror(new Pose(
                x + 1,
                y - 1.5
        ));
    }

    @Override
    public void init() {
        super.init();

        robot.blocker.block();

        Alliance.current = Alliance.BLUE;

        robot.drivetrain.setStartingPose(transformed(88.25, 9).withHeading(Math.toRadians(90)));
        robot.turret.setStartingAngle(0);

        paths = new Paths(robot.drivetrain.follower);
    }

    private Command aimForPose(Pose pose) {
        Pose newPose = pose.withHeading(pose.getHeading() - Math.toRadians(2));
        return instant(() -> {
            Pose turretLocation = TurretLocation.getTurretPose(newPose);

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
        robot.flywheel.setTarget(1600);

        schedule(
                sequential(
                        instant(robot.flywheel::turnOn),
                        aimForPose(transformed(88.25, 9).withHeading(Math.toRadians(90))),
                        waitUntil(robot.flywheel::atTarget),
                        robot.intake.on(),
                        shoot(),
                        instant(() -> robot.drivetrain.follower.setMaxPower(0.5)),
                        robot.drivetrain.followPath(paths.lastRowIntake),
                        instant(() -> robot.drivetrain.follower.setMaxPower(1)),
                        robot.intake.off(),
                        aimForPath(paths.toSecondShoot),
                        robot.drivetrain.followPath(paths.toSecondShoot),
                        robot.intake.on(),
                        shoot(),
                        instant(() -> robot.drivetrain.follower.setMaxPower(0.35)),
                        race(
                                sequential(
                                        robot.drivetrain.followPath(paths.cornerIntake),
                                        robot.drivetrain.followPath(paths.awayFromCorner),
                                        robot.drivetrain.followPath(paths.backToCorner),
                                        robot.drivetrain.followPath(paths.awayFromCorner)
                                ),
                                waitMs(8000)
                        ),
                        instant(() -> robot.drivetrain.follower.setMaxPower(1)),
                        aimForPath(paths.toThirdShoot),
                        robot.drivetrain.followPath(paths.toThirdShoot),
                        shoot(),
                        robot.drivetrain.followPath(paths.outOfZone),
                        // end
                        instant(robot.flywheel::turnOff),
                        robot.intake.off()
                )
        );
    }

    private Command shoot() {
        return sequential(
                instant(robot.intake::slowDown),
                instant(robot.blocker::unblock),
                waitMs(1000),
                instant(robot.blocker::block),
                instant(robot.intake::speedUp)
        );
    }

    private static class Paths {
        public final PathChain lastRowIntake;
        public final PathChain toSecondShoot;
        public final PathChain toCorner;
        public final PathChain cornerIntake;
        public final PathChain awayFromCorner;
        public final PathChain backToCorner;
        public final PathChain toThirdShoot;
        public final PathChain outOfZone;

        public Paths(Follower follower) {
            lastRowIntake = follower.pathBuilder()
                    .addPath(new BezierCurve(
                            transformed(88.25, 9),
                            transformed(88.25, 25),
                            transformed(95, 35.5),
                            transformed(132, 35.5)
                    ))
                    .setTangentHeadingInterpolation()
                    .build();

            toSecondShoot = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(130, 35.5),
                            transformed(82, 26)
                    ))
                    .setConstantHeadingInterpolation(Math.toRadians(180))
                    .build();

            toCorner = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(82, 26),
                            transformed(136.25, 35)
                    ))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(270))
                    .build();

            cornerIntake = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(136.25, 35),
                            transformed(136.25, 10)
                    ))
                    .setTangentHeadingInterpolation()
                    .build();

            awayFromCorner = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(136.25, 10),
                            transformed(131, 16)
                    ))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            backToCorner = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(131, 16),
                            transformed(136, 12)
                    ))
                    .setConstantHeadingInterpolation(Math.toRadians(270))
                    .build();

            toThirdShoot = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(131, 16),
                            transformed(95, 11)
                    ))
                    .setTangentHeadingInterpolation()
                    .build();

            outOfZone = follower.pathBuilder()
                    .addPath(new BezierLine(
                            transformed(95, 11),
                            transformed(109, 11)
                    ))
                    .setConstantHeadingInterpolation(Math.toRadians(0))
                    .build();
        }
    }
}

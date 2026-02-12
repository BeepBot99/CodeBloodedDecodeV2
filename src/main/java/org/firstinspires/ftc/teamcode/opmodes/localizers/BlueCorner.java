package org.firstinspires.ftc.teamcode.opmodes.localizers;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.robot.Alliance;
import org.firstinspires.ftc.teamcode.subsystems.Drivetrain;
import org.firstinspires.ftc.teamcode.subsystems.Turret;

import static org.firstinspires.ftc.teamcode.math.PoseMirror.mirror;

@Autonomous(name = "Blue Corner", group = "Localizers")
public class BlueCorner extends LinearOpMode {
    @Override
    public void runOpMode() {
        Turret.localize(0);
        Drivetrain.localize(mirror(new Pose(7.5, 8.1, Math.PI / 2)));
        Alliance.current = Alliance.BLUE;
    }
}

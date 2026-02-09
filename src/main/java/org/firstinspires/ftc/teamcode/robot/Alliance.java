package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.geometry.Pose;

public enum Alliance {
    RED(new Pose(141, 141)),
    BLUE(new Pose(1.5, 140));

    public static Alliance current = Alliance.RED;
    public final Pose goal;

    Alliance(Pose goal) {
        this.goal = goal;
    }
}

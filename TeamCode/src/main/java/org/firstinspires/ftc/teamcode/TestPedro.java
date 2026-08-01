package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp()
public class TestPedro extends OpMode {
    public static Follower follower;
    private PathChain path;

    public TestPedro() {
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        Pose scorePose = new Pose(0, 0, 0);
        Pose pickupPose = new Pose(20, 30, Math.toRadians(90));
        follower.setPose(scorePose);
        path = follower.pathBuilder()
                .addPath(
                        new BezierLine(scorePose, pickupPose)
                )
                .setLinearHeadingInterpolation(
                        scorePose.getHeading(),
                        pickupPose.getHeading()
                )
                .addPath(
                        new BezierLine(pickupPose, scorePose)
                )
                .setLinearHeadingInterpolation(
                        pickupPose.getHeading(),
                        scorePose.getHeading()
                )
                .build();

    }
    public void init_loop() {

    }

    public void start() {
        follower.followPath(path);
    }

    public void loop() {
        follower.update();

    }
}

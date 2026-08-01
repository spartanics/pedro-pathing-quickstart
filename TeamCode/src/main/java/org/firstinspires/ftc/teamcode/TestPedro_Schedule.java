package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.commands.Commands.waitMs;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;



import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@TeleOp()
public class TestPedro_Schedule extends OpMode {
    public static Follower follower;
    private PathChain path, path_back;

    public TestPedro_Schedule() {
    }

    public Command followPath(PathChain path) {
        return Command.build()
                .setStart(() -> follower.followPath(path))
                .setExecute(() -> follower.update())
                .setDone(() -> ! follower.isBusy());
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

                .build();

        path_back = follower.pathBuilder()
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
        Scheduler.schedule(
                sequential(
                        followPath(path),
                        waitMs(1000),
                        followPath(path_back)
                )
        );
    }

    public void loop() {
        Scheduler.execute();

    }
}

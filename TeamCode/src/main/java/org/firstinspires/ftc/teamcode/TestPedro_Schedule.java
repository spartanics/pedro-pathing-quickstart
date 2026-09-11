package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.groups.Groups.race;
import static com.pedropathing.ivy.commands.Commands.waitMs;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

import java.util.function.BooleanSupplier;

@TeleOp()
public class TestPedro_Schedule extends OpMode {
    public static Follower follower;
    private PathChain path, path_back;
    private DcMotor intake;

    public TestPedro_Schedule() {
    }

    public Command followPath(PathChain path) {
        return Command.build()
                .setStart(() -> follower.followPath(path))
                .setExecute(() -> follower.update())
                .setDone(() -> ! follower.isBusy());
    }

    public Command runIntake (double ms){
        return race(
                //does waitMs and Command.build() at the same time
                //Because setDone never finishes, waitMs will always finish first
                //now, race has finished. setEnd is executed even though setDone is false
                //.requiring() what
                //.setInterruptedBehavior() what other objects should do when I(an object) interrupt
                //.setBlockedBehavior()
                //.setConflictBehavior()
                //.setPriority()
                waitMs(ms),
                Command.build()
                        .setExecute(() -> {
                            intake.setPower(1);
                        })
                        .setDone(() -> false)
                        .setEnd(endCondition -> {
                            intake.setPower(0);
                        })
        );
    }

    @Override
    public void init() {
        follower = Constants.createFollower(hardwareMap);
        intake = hardwareMap.get(DcMotor.class, "intake");
        Pose scorePose = new Pose(0, 0, 0);
        Pose pickupPose = new Pose(20, 30, Math.toRadians(90));
        //follower.setPose(scorePose);
        follower.setStartingPose(scorePose);
        follower.startTeleopDrive();
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
        //follower.update();




    }
    public void init_loop() {
        follower.update();
    }

    public void start() {
        Scheduler.schedule(
                joystickToIntake(() -> gamepad1.a, () -> gamepad1.b)
        );

        Scheduler.schedule(
                sequential(
                        /*followPath(path),
                        waitMs(1000),
                        followPath(path_back),*/
                        runIntake(3000)

                )
        );
    }

    public void loop() {
        follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
        follower.update();
        Scheduler.execute();

    }

    public Command joystickToIntake(BooleanSupplier intakeButton, BooleanSupplier spittingButton) {
        return infinite(() -> {
            if (intakeButton.getAsBoolean()) {
                intake.setPower(1);
            } else if (spittingButton.getAsBoolean()) {
                intake.setPower(-1);
            }
        });
    }
}

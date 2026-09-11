package org.firstinspires.ftc.teamcode;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.pedropathing.ivy.Command;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.infinite;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class PedroAutonomous extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class

    private DcMotor intake; // Intake motor

    @Override
    public void init() {
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        intake = hardwareMap.get(DcMotor.class, "intake");
        follower.setStartingPose(new Pose(72, 8, Math.toRadians(90)));

        paths = new Paths(follower); // Build paths

        // Must reset or otherwise subsequent runs will not work
        Scheduler.reset();

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);
    }

    public Command sweepFirstRow() {
        return sequential(
                follow(follower, paths.ToFirstRow),
                waitMs(200),
                parallel(
                        instant(() -> intake.setPower(1)),
                        follow(follower, paths.ForwardSweep, 0.3)
                ),
                instant(() -> intake.setPower(0))
        );

    }

    @Override
    public void start() {
        schedule(sweepFirstRow());

    }
    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();
        pathState = autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }

    public static class Paths {
        public PathChain ToFirstRow;
        public PathChain ForwardSweep;

        public Paths(Follower follower) {
            ToFirstRow = follower.pathBuilder()
                    .addPath(
                            new BezierCurve(
                                    new Pose(58.407, 8.344),
                                    new Pose(61.896, 36.106),
                                    new Pose(42.014, 34.946)
                            )
                    )
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(0))
                    .build();

            ForwardSweep = follower.pathBuilder()
                    .addPath(
                            new BezierLine(
                                    new Pose(42.014, 34.946),
                                    new Pose(13.411, 34.730)
                            )
                    )
                    .setTangentHeadingInterpolation()
                    .build();
        }
    }


    public int autonomousPathUpdate() {
        // Add your state machine Here
        // Access paths with paths.pathName
        // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine
        return 0;
    }
}
# TeamCode - Pedro Pathing Command System Example

This module contains examples and implementations for FTC robot control using [Pedro Pathing](https://github.com/Pedro-Pathing/PedroPathing).

## TestPedro_Schedule.java

`TestPedro_Schedule.java` is a sample TeleOp OpMode designed to demonstrate how to use the **Pedro Pathing Command System** and **Scheduler**.

### Key Concepts

#### 1. Command Building
The OpMode showcases how to wrap robot actions into `Command` objects:
* **Path Following**: The `followPath(PathChain path)` method creates a command that initiates path following and remains active until the follower is no longer busy.
* **Intake Control**: The `runIntake(double ms)` method demonstrates the use of a `race` group. It runs the intake motor while simultaneously running a timer (`waitMs`). When the timer finishes, the "race" ends, stopping the motor via the `setEnd` callback.
* **Infinite Commands**: The `joystickToIntake` method uses `infinite` to continuously map gamepad inputs to motor power throughout the OpMode's execution.

#### 2. The Scheduler
The OpMode utilizes the `Scheduler` to manage concurrent and sequential tasks:
* **Scheduling**: In the `start()` method, commands are added to the scheduler using `Scheduler.schedule()`.
* **Execution**: In the `loop()` method, `Scheduler.execute()` is called to process the active commands, ensuring that state transitions and logic are handled every frame.

#### 3. Integrated TeleOp Drive
While commands are running in the background (or foreground), the OpMode also maintains standard TeleOp drive control using `follower.setTeleOpDrive`, allowing for a hybrid of automated pathing/actions and manual control.

### How to Use
1. **Initialize Hardware**: Ensure your `DcMotor` named "intake" and your robot's drive motors are correctly configured in the hardware map.
2. **Select OpMode**: Choose `TestPedro_Schedule` on the Driver Station.
3. **Run**:
    - Pressing **A** or **B** on `gamepad1` will control the intake manually via an infinite command.
    - Upon starting, the robot will automatically run the intake for 3 seconds as part of a scheduled sequence.
    - Standard driving is available via the joysticks.

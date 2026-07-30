# 3. Code tour: how this project (and most FRC code) is organized

This project uses WPILib's command-based framework. Learn the mental model below and every
file in `src/` will make sense, along with most other teams' code you'll ever read.

## The mental model

| Piece | What it is | In this project |
| --- | --- | --- |
| Subsystem | One mechanism plus the only code allowed to touch its hardware | `DriveSubsystem` |
| Command | A job that uses subsystems: start, run every 20ms, finish | `TeleopDriveCommand`, `TurnToAngleCommand`, the autos |
| Scheduler | Runs 50 times per second: reads buttons, starts and stops commands, and makes sure two commands never fight over one subsystem | the one line in `Robot.robotPeriodic()` |
| Trigger | "When this button or condition is true, run that command" | `RobotContainer.configureBindings()` |

Two rules make the whole thing work:

1. Every command declares which subsystems it requires. The scheduler never runs two
   commands that require the same subsystem; a new one interrupts the old.
2. A subsystem may have a default command that runs whenever nothing else claims it. Ours
   is `TeleopDriveCommand`, which is why the robot "just drives" in teleop.

## Life of a joystick input (worth tracing once in the actual files)

The driver pushes the left stick. `Robot.robotPeriodic()` runs the CommandScheduler
(every 20 ms), which executes `TeleopDriveCommand.execute()`. That method reads the stick
suppliers and applies deadband, squaring, slow mode, and the slew limit, then calls
`drive.arcadeDrive(forward, rotation)`. `DifferentialDrive` turns that into left and right
outputs and sets the two leader motors (the followers copy in firmware), and the motors
spin. Meanwhile `DriveSubsystem.periodic()` has updated odometry and logged everything.

## File by file

### `Robot.java`: when things happen
The season-independent heartbeat. Sets up logging, runs the scheduler, and starts or
cancels the auto command at mode changes. You'll rarely edit it.

### `RobotContainer.java`: what the robot is
Creates each subsystem once, wires buttons to commands, and fills the autonomous chooser.
When you add a mechanism, this is where it gets plugged in.

### `Constants.java`: the numbers
CAN IDs, gear ratio, control feel, PID gains. Change behavior here first. Every constant's
comment explains how to measure or tune it.

### `subsystems/DriveSubsystem.java`: the hardware
Configures the four SPARK MAXes (leader/follower, inversion, current limits, encoder
units), reads the Pigeon gyro, updates odometry ("where am I?"), logs everything, and
contains the physics-simulation hookup. Its public methods (`arcadeDrive`, `getPose`,
`resetOdometry`, `stop`) are the only way the rest of the code touches the drivetrain.

### `commands/TeleopDriveCommand.java`: the important verb
The default command. Read this file to learn the command lifecycle
(`initialize`/`execute`/`isFinished`/`end`); it's annotated line by line.

### `commands/TurnToAngleCommand.java`: closed-loop 101
A PID controller turning the robot a precise amount using the gyro. This is the gateway to
most advanced control in FRC.

### `commands/Autos.java`: composition
Autonomous routines built by chaining small pieces (`run`, `withTimeout`, `until`,
`andThen`) instead of writing classes. Both styles are valid, and you'll use both.

### `ExampleSubsystem.java` and `ExampleCommand.java`: the templates
Ready-to-copy skeletons for your next mechanism, with the activation checklist in their
comments. Nothing constructs them, so they have no effect on the robot until you copy,
rename, and wire them up in RobotContainer (recipe in docs/07). There's a matching
`ExampleSubsystemTest.java` to copy on the test side.

### `src/test/java/...`: the safety net
JUnit tests that boot the whole robot against simulated hardware and drive it. They run on
every build. A red build means you caught a bug at your desk instead of on the field.

## "Where do I change..." index

| I want to... | Go to |
| --- | --- |
| Fix a robot that drives backwards or spins instead of driving straight | `Constants.DriveConstants` inversion flags |
| Make controls less or more sensitive | `Constants.OperatorConstants` (deadband, slew, slow mode) |
| Change a CAN ID | `Constants.DriveConstants` plus the device itself (docs/07) |
| Add or modify an autonomous routine | `commands/Autos.java`, then register it in `RobotContainer.configureAutos()` |
| Change button mappings | `RobotContainer.configureBindings()` |
| Add a whole new mechanism | Recipe in docs/07-common-tasks.md |
| Log a new value | One `Logger.recordOutput(...)` line; see docs/05 |

## The one diagram to remember

```
        RobotContainer (assembles)
        +--------------------------------------------------+
Buttons -> Triggers --> Commands -------> Subsystems --> Motors/Sensors
        |                  ^                            |
        +------------------+----------------------------+
                CommandScheduler runs it all, 50x/sec
                     (Robot.robotPeriodic)
```

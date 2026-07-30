# 6. Simulation: practice without a robot

This project includes a physics simulation of the drivetrain (mass, inertia, and motor
curves; see the bottom of `DriveSubsystem.java`). You can code, drive, tune, and test
autos on any laptop. On most teams the robot spends much of the season being built, wired,
or fixed, so simulation is where programmers get most of their robot time.

## Starting it

In WPILib VS Code: WPILib icon > Simulate Robot Code (or `./gradlew simulateJava` in a
terminal). Two things appear:

- The Robot Simulation GUI, a mini driver station: robot state (Disabled, Autonomous,
  Teleoperated), joystick list, NetworkTables data, timing.
- Your code is now "a robot on the network", so dashboards and AdvantageScope can connect
  to it exactly like a real one.

## Hooking up a controller

The easy path is plugging a real Xbox controller into the laptop. In the sim GUI, drag it
from System Joysticks onto slot [0] of Joysticks, and you can drive.

No controller? Use the keyboard. Drag Keyboard 0 onto Joystick[0]. By default W/S drive
axis 1, which is our forward/back. Turning uses axis 4 (right stick X on a real pad),
which the keyboard doesn't map out of the box: open DS > Keyboard 0 Settings, set Axis
Count to 5, and give axis 4 a key pair such as A/D. Settings save to `simgui-ds.json`.

## Driving

1. In the sim GUI, click Teleoperated.
2. Drive. Watch the Field widget (NetworkTables > SmartDashboard > Field), or better,
   AdvantageScope's Odometry tab with `Drive/Pose`.
3. The robot accelerates, coasts, and turns with realistic physics. Even the battery
   voltage sags when you floor it; graph `BatteryVoltage` under systemstats to see it.

## Running autonomous in sim (do this before every real match)

1. While Disabled, open the auto chooser: sim GUI > NetworkTables > SmartDashboard >
   Auto Choices (or use Elastic connected to localhost).
2. Pick a routine, for example "Drive Out and Back".
3. Click Autonomous and watch it execute on the field view.
4. Click Disabled, tweak code or constants, and repeat. This loop takes about 30 seconds;
   on a real robot it takes 10 minutes.

## The automated version: unit tests

`src/test/java/frc/robot/` contains tests that do a robot-less version of the above on
every single build: boot the container, drive the sim forward, spin, and check that the
encoders, gyro, and odometry agree. Run them explicitly with `./gradlew test`. When you
add a mechanism, copy the pattern in `DriveSubsystemSimTest` so it's covered too.

## What sim won't tell you

- Wheel slip, carpet scrub, chain slack, real battery health. The model is ideal.
- Anything about miswired hardware. That's what docs/02 and the real-robot checklist are
  for.
- Loop-timing problems that only appear on the slower roboRIO CPU.

Treat sim as "is my logic right?", not "will the robot behave exactly like this?".

## Under the hood (read when curious)

Every 20ms in sim, `DriveSubsystem.simulationPeriodic()`:

1. reads what voltage the code applied to each side,
2. advances a `DifferentialDrivetrainSim` physics model one step,
3. writes the resulting wheel positions and velocities back into the simulated SPARK MAX
   encoders and the simulated Pigeon's yaw.

The rest of the code (odometry, logging, commands) cannot tell it isn't real hardware.
That "sensors in, outputs out" symmetry is the same idea that full AdvantageKit IO layers
take to its conclusion (docs/08).

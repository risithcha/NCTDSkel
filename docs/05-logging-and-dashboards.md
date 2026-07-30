# 5. Logging and dashboards: see what the robot is thinking

When something goes wrong in a match ("it just stopped", "auto turned the wrong way"),
memory and guesswork won't find the cause. Recorded data will. This project logs
everything interesting automatically, two ways at once:

1. Live over the network (NetworkTables), so you can watch values in real time.
2. To a `.wpilog` file, so you can open it after the match and scrub through time.

We use [AdvantageKit](https://docs.advantagekit.org) in its simple "output logging" mode:
one line of code per value, no special architecture. AdvantageKit can do far more; see
docs/08-next-steps.md.

## Where the data goes

| Situation | Live viewing | Log file |
| --- | --- | --- |
| Real robot | Connect AdvantageScope to the robot | A USB stick in the roboRIO. Buy a small FAT32 one and leave it in; logs survive reboots and would otherwise fill internal storage |
| Simulation | Connect AdvantageScope to the simulator | The `logs/` folder in this project |

This is configured in `Robot.java`'s constructor, and the comments there explain each line.

## AdvantageScope in five minutes (installed with WPILib)

1. Open AdvantageScope, then File > Connect to Simulator (or Connect to Robot with the
   robot on).
2. The left sidebar fills with every logged value. Our data lives under:
   - `/RealOutputs/Drive/...` for pose, velocities, currents, and gyro
   - `/RealOutputs/TeleopDrive/...` for what the driver commanded
   - `/RealOutputs/TurnToAngle/...` for the PID target versus error while tuning
3. Odometry tab: drag `Drive/Pose` in and watch the robot drive on a field map.
4. Line graph tab: drag `Drive/LeftVelocityMetersPerSec` and
   `TeleopDrive/ForwardCommand` onto the same graph to compare command against response.
5. File > Open Log does all of the same on a saved `.wpilog`, with a timeline scrubber.

Every log also carries metadata (git commit, branch, build date, whether there were
uncommitted changes), visible in the metadata tab. That answers "which code was on the
robot?" for any log file.

## What's already logged

| Key | Meaning | Typical use |
| --- | --- | --- |
| `Drive/Pose` | Where odometry thinks the robot is | Field view; comparing autos |
| `Drive/HeadingDegrees` | Robot heading (counter-clockwise positive) | Turn tuning |
| `Drive/Left(Right)PositionMeters` | Wheel distance traveled | Distance auto debugging |
| `Drive/Left(Right)VelocityMetersPerSec` | Wheel speeds | "Is one side slower?" |
| `Drive/AppliedOutputLeft(Right)` | What the controllers actually output (-1 to 1) | "Did the code even ask it to move?" |
| `Drive/CurrentAmps` (array of 4) | Per-motor current | One motor reading high usually means a mechanical bind |
| `Drive/GyroYawDegrees` | Raw gyro | Sensor sanity check |
| `Drive/SimTruePose` | (Sim only) ground-truth pose | Seeing how far odometry drifts |
| `TeleopDrive/*` | The driver's shaped commands and slow mode | Separating code, driver, and robot problems |
| `TurnToAngle/*` | PID setpoint, error, output | Gain tuning |
| `Robot/SelectedAuto` | Which auto actually ran | Post-match disputes |

## Logging your own values

Any time you debug or add a mechanism, log its inputs and outputs:

```java
// Anywhere, one line: numbers, booleans, strings, arrays, poses...
Logger.recordOutput("Intake/RollerRPM", rollerEncoder.getVelocity());

// Or annotate a getter in a subsystem and it's logged automatically every loop:
@AutoLogOutput(key = "Intake/HasGamePiece")
public boolean hasGamePiece() { ... }
```

A rule of thumb from experienced teams: if you would print it, log it instead.
`System.out.println` scrolls away; a log key can be graphed any time.

## Elastic: the driver's dashboard

AdvantageScope is for engineers. Elastic (also installed with WPILib) is the clean
match-day dashboard. Open it, connect (enter your team number in settings), then add
widgets. The two that matter today:

- Auto Choices (under SmartDashboard): the drive team must set this before each match.
- Field: the live field view with the robot on it.

Drag, resize, save the layout, done. Keep the driver station screen simple: chooser,
field, match time, battery.

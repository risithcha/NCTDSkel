# FRC tank drive starter, a complete first robot project

This repository is a working, competition-legal robot program for a classic tank-drive
(differential) robot, plus the documentation a brand-new team needs to go from nothing to
driving and logging. Every file is heavily commented so the code doubles as the tutorial.

Built for WPILib 2026.2.1 (Java), with four NEO motors on SPARK MAX controllers (two per
side, KitBot-style chassis), a Pigeon 2.0 gyro, and AdvantageKit logging in its simple
"output logging" mode. No advanced architecture is required to use or extend it.

If your hardware differs, almost everything still applies. See
[docs/07-common-tasks.md](docs/07-common-tasks.md) for what to change.

## Quick start (about 10 minutes once software is installed)

1. Install the software on your programming laptop: WPILib, Driver Station, and the vendor
   tools. Full walkthrough in [docs/01-software-setup.md](docs/01-software-setup.md).
2. Open this folder in "2026 WPILib VS Code" (File, then Open Folder). When prompted to
   import and trust the project, say yes.
3. Set your team number (it ships as `0`). Click the WPILib icon (the red and white
   hexagon, top right) and pick "Set Team Number", or edit
   `.wpilib/wpilib_preferences.json`. Set it in the Driver Station app as well (gear icon,
   Team Number).
4. Build it: WPILib icon > Build Robot Code. The first build downloads dependencies and
   runs the tests, so expect a few minutes. You want to see `BUILD SUCCESSFUL`.
5. No robot yet? Run the simulator instead: WPILib icon > Simulate Robot Code. You can
   drive a physics-simulated robot around a virtual field. See
   [docs/06-simulation.md](docs/06-simulation.md).
6. Have a robot? Wire it and set CAN IDs per
   [docs/02-hardware-and-wiring.md](docs/02-hardware-and-wiring.md), connect, then WPILib
   icon > Deploy Robot Code. Driving basics are in
   [docs/04-driving-and-controls.md](docs/04-driving-and-controls.md).

## Default controls (Xbox controller on port 0)

| Control | Action |
| --- | --- |
| Left stick up/down | Drive forward and backward |
| Right stick left/right | Turn |
| Right bumper (hold) | Slow mode (50% speed) for precise lining-up |
| Start | Reset odometry ("I'm at the field origin") |
| B | Demo: PID quarter-turn to the left |

## What's in the box

```
+-- src/main/java/frc/robot/
|   +-- Main.java                    entry point (never edit)
|   +-- Robot.java                   the heartbeat: modes + logging setup
|   +-- RobotContainer.java          the assembly: subsystems + buttons + auto chooser
|   +-- Constants.java               every tunable number, explained
|   +-- subsystems/
|   |   +-- DriveSubsystem.java      motors, encoders, gyro, odometry, sim, logging
|   |   +-- ExampleSubsystem.java    template to copy for your next mechanism
|   +-- commands/
|       +-- TeleopDriveCommand.java  driver control (the command lifecycle, explained)
|       +-- TurnToAngleCommand.java  intro to PID and closed-loop control
|       +-- ExampleCommand.java      template to copy for a class-based command
|       +-- Autos.java               autonomous routines (command composition)
+-- src/test/java/frc/robot/         tests that drive the simulated robot on every build
+-- docs/                            start here if you're new; numbered in reading order
+-- vendordeps/                      REVLib, Phoenix 6, AdvantageKit library definitions
+-- .github/workflows/build.yml      auto-build every push once this repo is on GitHub
+-- build.gradle                     how the project compiles and deploys
```

## The docs

| Read this | To learn |
| --- | --- |
| [01-software-setup.md](docs/01-software-setup.md) | Installing everything, firmware, imaging the roboRIO |
| [02-hardware-and-wiring.md](docs/02-hardware-and-wiring.md) | Wiring checklist and the CAN ID table this code expects |
| [03-code-tour.md](docs/03-code-tour.md) | How command-based code works, with a guided tour of every file |
| [04-driving-and-controls.md](docs/04-driving-and-controls.md) | Driver Station, enabling safely, driving |
| [05-logging-and-dashboards.md](docs/05-logging-and-dashboards.md) | AdvantageScope and Elastic; reading the logs this code records |
| [06-simulation.md](docs/06-simulation.md) | Practicing without a robot |
| [07-common-tasks.md](docs/07-common-tasks.md) | Cookbook: change IDs, tune feel, add autos, add a subsystem |
| [08-next-steps.md](docs/08-next-steps.md) | Where to go from here: PID, paths, vision, full AdvantageKit |
| [glossary.md](docs/glossary.md) | What all the acronyms mean |

## Working rules

1. Only a subsystem touches its own hardware. Everything else asks the subsystem.
2. Numbers live in `Constants.java`, not buried in logic.
3. Movement happens in commands, never in a subsystem's `periodic()`.
4. Log the inputs and outputs of anything you debug.
5. Build (and run the tests) before every deploy. They run automatically; let them.

## License

Template portions are copyright the WPILib contributors (see `WPILib-License.md`). Use
this freely for your team. Keeping this notice is appreciated.

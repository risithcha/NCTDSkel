# 4. Driving the robot (and not hurting anyone)

## Safety rules to teach before anyone touches Enable

1. An enabled robot is live machinery. Nobody reaches toward a robot unless it is disabled
   (or better, powered off). Bench testing happens with the wheels on blocks.
2. The person at the Driver Station announces "enabling" and waits a beat. Anyone can call
   "disable" at any time, and the DS operator obeys immediately, no questions asked.
3. Know the two important keys in the Driver Station app: Enter disables (the normal
   stop), and Spacebar is the emergency stop, which kills everything until the roboRIO
   reboots. Practice both until they're reflexes.
4. If the battery isn't strapped in, the robot doesn't move. A dropped battery can end
   your event.

## Driver Station app in 60 seconds

Open FRC Driver Station (installed with the Game Tools):

- The team number (gear tab) must match the roboRIO's; that's how they find each other.
- Connect the laptop to the robot's Wi-Fi (the SSID is your team number).
- Three status lights matter: Communications, Robot Code, and Joysticks. All green means
  you can enable. Something red? See the table in docs/07-common-tasks.md.
- Mode buttons: TeleOperated, Autonomous, Practice, Test. Pick a mode, then click Enable.
  At a real event the field controls this for you.
- On the USB tab, confirm the Xbox controller shows up as device 0 and its axes move when
  you wiggle the sticks. The code expects the driver on port 0
  (`OperatorConstants.DRIVER_CONTROLLER_PORT`).

## The controls this code ships with

| Control | Action | Where it's defined |
| --- | --- | --- |
| Left stick up/down | Forward and backward | `RobotContainer.configureDefaultCommands()` |
| Right stick left/right | Turn left and right | same |
| Right bumper (hold) | 50% slow mode | `OperatorConstants.SLOW_MODE_MULTIPLIER` |
| Start | Reset odometry to the origin | `RobotContainer.configureBindings()` |
| B | Demo: PID quarter-turn left | same; remove it when it stops being educational |

Driving feel (deadband, acceleration ramp, slow-mode strength) is tuned in
`Constants.OperatorConstants`. Each constant's comment says which way to turn the knob.

## First-drive checklist (every new robot, every big code change)

1. Robot on blocks, wheels free. Enable teleop.
2. Push the left stick gently forward. All four wheels should spin forward. If everything
   runs backwards, flip both inversion flags in `Constants`. If one side runs backwards,
   flip that side's flag. Don't "fix" it with minus signs in RobotContainer.
3. Right stick right: left wheels forward, right wheels backward (the robot would turn
   clockwise).
4. On the floor at walking pace: drive a square, then practice slow mode against a wall
   target.
5. Watch the Driver Station voltage graph while driving hard. Dips below about 7V cause
   brownouts (the robot stutters), which usually means a tired battery or a loose main
   power connection.

## Practice mode and match flow

A real match is 15 seconds of autonomous (hands off the controllers while the routine
picked on the dashboard runs), then 2 minutes 15 seconds of teleop. The DS Practice mode
replays that sequence with a countdown. Use it in the shop so the auto-to-teleop handoff
never surprises you. Remember to pick your auto in the dashboard chooser before every
match; the default is "Do Nothing" on purpose.

## Switching to true tank controls (two sticks)

Some drivers prefer a stick per side. In `RobotContainer.configureDefaultCommands()`,
replace the `TeleopDriveCommand` with:

```java
drive.setDefaultCommand(
    drive.run(
        () -> drive.tankDrive(-driverController.getLeftY(), -driverController.getRightY())));
```

You lose the input shaping TeleopDriveCommand does. A good exercise is writing a
`TeleopTankCommand` that adds it back.

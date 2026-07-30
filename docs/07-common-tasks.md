# 7. Common tasks cookbook

Recipes for the things every team does in season. Times assume you've read docs/03.

## Set or change the team number
1. `.wpilib/wpilib_preferences.json`, set `"teamNumber": 1234` (or WPILib icon > Set Team
   Number).
2. Driver Station, gear tab, same number.

## Deploy code to the robot
1. Connect to the robot (its Wi-Fi, or a USB-B cable into the roboRIO).
2. WPILib icon > Deploy Robot Code (Shift+F5).
3. Success looks like `BUILD SUCCESSFUL` plus a green "Robot Code" light in the Driver
   Station within about 30 seconds.

### Deploy and connection troubleshooting

| Symptom | Usual cause and fix |
| --- | --- |
| "Target roborio could not be found" | Wrong network (join the robot Wi-Fi or plug in USB); team number mismatch (recipe above); roboRIO still booting (wait 45s) |
| Deploy OK but "Robot Code" light red | Code crashed on startup. Open riolog (WPILib icon > Start RioLog) and read the exception; a wrong CAN ID is the usual suspect |
| "Communications" red | Radio not configured or not powered; laptop firewall blocking the DS apps |
| Robot enabled but nothing moves | Controller on the wrong USB port (DS USB tab; the driver belongs on port 0); breakers not seated; motor controllers blinking an error color |
| Robot stutters and the DS shows voltage dips | Brownout: tired battery, loose main power crimps, or current limits raised too high |

## Change a CAN ID
1. REV Hardware Client (or Phoenix Tuner X for the Pigeon): set the new ID on the device.
2. Update the matching constant in `Constants.DriveConstants`.
3. Update the table in docs/02 so it stays true. Deploy and test.

## Robot drives backwards or spins when asked to go straight
Flip the inversion flags in `Constants.DriveConstants` (the comments there tell you which
one). Don't "fix" direction with minus signs sprinkled around the code.

## Tune how driving feels
All in `Constants.OperatorConstants`, and all safe to experiment with in sim first:
`DEADBAND` (creep versus dead zone), `FORWARD_SLEW_RATE` (smoothness versus
responsiveness), `SLOW_MODE_MULTIPLIER` (precision mode strength).

## Add an autonomous routine (about 10 minutes)
1. Add a factory in `commands/Autos.java`, composing existing pieces:
   ```java
   /** Example of adding your own: drive out, wait, come back. */
   public static Command driveOutWaitReturn(DriveSubsystem drive) {
     return Commands.sequence(
             driveForwardDistance(drive),
             Commands.waitSeconds(1.0),
             new TurnToAngleCommand(drive, 180.0),
             driveForwardDistance(drive))
         .withName("Out, Wait, Return");
   }
   ```
2. Register it in `RobotContainer.configureAutos()`:
   ```java
   autoChooser.addOption("Out, Wait, Return", Autos.driveOutWaitReturn(drive));
   ```
3. Run it in simulation (docs/06) before it ever touches carpet.

## Add a whole new mechanism (worked example: an intake roller)

The fastest route: copy the templates. `subsystems/ExampleSubsystem.java`,
`commands/ExampleCommand.java`, the `ExampleConstants` block in Constants.java, and
`ExampleSubsystemTest.java` are this recipe in file form, with the checklist in their
comments. Rename "Example" to your mechanism's name and fill in the blanks. The rest of
this section walks the same steps by hand so you can see what each piece is for.

Say you bolt on an intake: one NEO 550 on a SPARK MAX (CAN ID 31), and the driver holds A
to run it.

Step 1, `Constants.java`: a new nested class.
```java
public static final class IntakeConstants {
  public static final int ROLLER_CAN_ID = 31;
  public static final int CURRENT_LIMIT_AMPS = 25; // NEO 550s burn out above ~25A
  public static final double INTAKE_SPEED = 0.8;
}
```

Step 2, `subsystems/IntakeSubsystem.java`: copy DriveSubsystem's shape, one motor, no sim
needed on day one.
```java
public class IntakeSubsystem extends SubsystemBase {
  private final SparkMax roller = new SparkMax(IntakeConstants.ROLLER_CAN_ID, MotorType.kBrushless);

  public IntakeSubsystem() {
    SparkMaxConfig config = new SparkMaxConfig();
    config.idleMode(IdleMode.kBrake).smartCurrentLimit(IntakeConstants.CURRENT_LIMIT_AMPS);
    roller.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void run(double speed) { roller.set(speed); }
  public void stop() { roller.set(0.0); }

  @Override
  public void periodic() {
    Logger.recordOutput("Intake/AppliedOutput", roller.getAppliedOutput());
    Logger.recordOutput("Intake/CurrentAmps", roller.getOutputCurrent()); // spikes when a game piece arrives
  }
}
```

Step 3, `RobotContainer`: create it and bind a button. `startEnd` means "do this while
held, do that on release".
```java
private final IntakeSubsystem intake = new IntakeSubsystem();

// in configureBindings():
driverController.a().whileTrue(
    intake.startEnd(() -> intake.run(IntakeConstants.INTAKE_SPEED), intake::stop)
          .withName("Run Intake"));
```

That's the entire pattern, and it's the same for arms, shooters, and climbers. Add PID
(docs/08) once "just set a speed" isn't precise enough.

## New season checklist (each January)
1. Install the new season's WPILib and Game Tools; re-image the roboRIO; re-configure the
   radio.
2. WPILib icon > Import Gradle Project brings this project into the new version.
3. Update `vendordeps/*.json` (WPILib icon > Manage Vendor Libraries > check for updates).
4. Read the "New for 20XX" page on docs.wpilib.org, because the APIs move a little every
   year.

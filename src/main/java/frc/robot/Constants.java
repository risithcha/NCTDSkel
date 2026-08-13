package frc.robot;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
/**
 * All of the robot's "magic numbers" live in this one file.
 *
 * <p>WHY: When something on the robot changes (a motor gets rewired, a gear ratio changes, a
 * driver wants the controls to feel different), you should only ever have to edit ONE place.
 * If a number is buried inside a subsystem or command, nobody will find it at a competition.
 *
 * <p>HOW TO USE: Everything here is {@code public static final} (a constant). Never put code
 * or robot logic in this file; numbers and simple math on numbers only.
 *
 * <p>Constants are grouped into nested classes ({@link DriveConstants}, {@link
 * OperatorConstants}, ...) so that a constant reads like a sentence at the call site, e.g.
 * {@code DriveConstants.LEFT_LEADER_CAN_ID}.
 */
public final class Constants {

  /** Constants for the drivetrain: CAN IDs, physical dimensions, and limits. */
  public static final class DriveConstants {

    // ------------------------------------------------------------------------------------
    // CAN IDs
    //
    // Every device on the CAN bus (motor controllers, the Pigeon gyro, the power hub) needs
    // a unique ID number. You assign SPARK MAX IDs with the REV Hardware Client and the
    // Pigeon's ID with Phoenix Tuner X (see docs/01-software-setup.md).
    //
    // These MUST match what you configured on the real hardware, or the robot will not move
    // (and you'll see "CAN timeout" style errors in the Driver Station).
    //
    // Our convention (feel free to keep it as your team grows):
    //   1         = Power Distribution Hub (REV default)
    //   11-19     = drivetrain
    //   20-29     = sensors (gyro, etc.)
    //   30+       = future mechanisms (arm, intake, shooter, ...)
    // ------------------------------------------------------------------------------------
    public static final int LEFT_LEADER_CAN_ID = 11;
    public static final int LEFT_FOLLOWER_CAN_ID = 12;
    public static final int RIGHT_LEADER_CAN_ID = 13;
    public static final int RIGHT_FOLLOWER_CAN_ID = 14;
    public static final int PIGEON_CAN_ID = 20;

    // ------------------------------------------------------------------------------------
    // Which side is "backwards"?
    //
    // The two sides of a tank drivetrain physically face opposite directions, so one side
    // must be electrically inverted or the robot spins in circles when you push "forward".
    // With the motors mounted like the KitBot's, inverting the RIGHT side makes positive =
    // forward on both sides. If your robot drives backwards when you command forward, flip
    // BOTH of these; if it spins in place, flip ONE of them.
    // ------------------------------------------------------------------------------------
    public static final boolean LEFT_INVERTED = false;
    public static final boolean RIGHT_INVERTED = true;

    // ------------------------------------------------------------------------------------
    // Physical dimensions - used to convert "motor rotations" into real-world meters, which
    // is what makes odometry (tracking where the robot is on the field) possible.
    // ------------------------------------------------------------------------------------

    /**
     * How many times the MOTOR spins for ONE rotation of the WHEEL. Read this off your
     * gearbox: the AM14U (KitBot) Toughbox Mini is 10.71:1 by default (8.45:1 is the common
     * "fast" option). Getting this wrong makes every distance the robot reports wrong by the
     * same factor.
     */
    public static final double GEAR_RATIO = 10.71;

    /** KitBot wheels are 6 inches. Measure yours, since worn wheels are slightly smaller. */
    public static final double WHEEL_DIAMETER_METERS = Units.inchesToMeters(6.0);

    /**
     * Distance between the CENTERS of the left and right wheels ("track width"). Used by the
     * simulator and, later, by trajectory following. Measure with a tape measure; close is
     * fine to start.
     */
    public static final double TRACK_WIDTH_METERS = Units.inchesToMeters(22.0);

    // ------------------------------------------------------------------------------------
    // Encoder conversion factors.
    //
    // The NEO's built-in encoder natively reports MOTOR rotations and RPM. We tell the
    // SPARK MAX to multiply by these factors so that everywhere else in the code:
    //   position is in METERS and velocity is in METERS PER SECOND (of the wheel/robot).
    //
    //   meters per motor rotation = (wheel circumference) / (gear ratio)
    //   m/s per motor RPM         = that, divided by 60 (seconds per minute)
    // ------------------------------------------------------------------------------------
    public static final double POSITION_CONVERSION_FACTOR =
        (Math.PI * WHEEL_DIAMETER_METERS) / GEAR_RATIO; // motor rotations -> meters
    public static final double VELOCITY_CONVERSION_FACTOR =
        POSITION_CONVERSION_FACTOR / 60.0; // motor RPM -> meters per second

    // ------------------------------------------------------------------------------------
    // Electrical limits - these protect your robot. Don't remove them.
    // ------------------------------------------------------------------------------------

    /**
     * Max current (amps) each motor may draw. 50A is a safe, competitive value for a NEO on
     * a 40A-breaker drivetrain (brief spikes above the breaker rating are fine). Lowering
     * this reduces brownouts (robot momentarily losing power) and wheel slip; raising it
     * gives more pushing power but risks tripping breakers and browning out.
     */
    public static final int CURRENT_LIMIT_AMPS = 50;

    /**
     * The SPARK MAX scales its output as if the battery always sat at this voltage, so the
     * robot drives the same on a fresh battery (12.8V) as on a tired one (11.5V).
     */
    public static final double NOMINAL_VOLTAGE = 12.0;

    // ------------------------------------------------------------------------------------
    // Simulation-only physical properties (rough numbers are fine; they just make the
    // simulated robot accelerate realistically). No effect on the real robot.
    // ------------------------------------------------------------------------------------

    /** Full robot weight including battery and bumpers. ~125 lb is a typical KitBot. */
    public static final double ROBOT_MASS_KG = 55.0;

    /** Moment of inertia - how hard the robot is to SPIN. 7.5 is a reasonable default. */
    public static final double ROBOT_MOI_KG_M2 = 7.5;
  }

  /** Constants for driver controls: ports, deadbands, and "feel" tuning. */
  public static final class OperatorConstants {

    /** USB port of the driver's controller, as shown on the Driver Station's USB tab. */
    public static final int DRIVER_CONTROLLER_PORT = 0;

    /**
     * Sticks never rest at exactly zero; they drift a few percent off-center. Any input
     * smaller than this is treated as zero so the robot doesn't creep on its own. If your
     * robot creeps, raise this; if small careful movements do nothing, lower it.
     */
    public static final double DEADBAND = 0.10;

    /**
     * Limits how fast the FORWARD command may change, in "full sticks per second".
     * 3.0 means the drive can go from 0% to 100% in 1/3 of a second. This stops the robot
     * from tipping or burning rubber when the driver slams the stick, while turning is left
     * unfiltered so steering still feels instant.
     */
    public static final double FORWARD_SLEW_RATE = 3.0;

    /** Everything is multiplied by this while the slow-mode button is held. */
    public static final double SLOW_MODE_MULTIPLIER = 0.5;
  }

  public static final class AutoConstants {

    /** Speed (fraction of full power, 0-1) used when driving forward in auto. */
    public static final double DRIVE_SPEED = 0.4;

    /** How long the "drive forward by time" auto drives. */
    public static final double DRIVE_TIME_SECONDS = 2.0;

    /** How far the "drive forward by distance" auto drives. */
    public static final double DRIVE_DISTANCE_METERS = 2.0;

    // --- Turn-to-angle PID gains (see commands/TurnToAngleCommand.java) ------------------
    // Start with only kP. If the robot oscillates around the target, lower kP or add a
    // little kD. If it stops short of the target, raise kP slightly.
    public static final double TURN_KP = 0.008; // output per degree of error
    public static final double TURN_KD = 0.002;
    public static final double TURN_MAX_OUTPUT = 0.5; // never spin faster than half power
    public static final double TURN_TOLERANCE_DEGREES = 2.0; // "close enough" to finish
    public static final double TURN_RATE_TOLERANCE_DEG_PER_SEC = 5.0; // and nearly stopped

    public static final double DRIVE_KP = 1.2;
    public static final double DRIVE_KD = 0.1;
    public static final double DRIVE_HEADING_KP = 0.02; // start at 0.02
    public static final double DRIVE_MAX_OUTPUT = 0.6; // never drive faster than 60% power
    public static final double DRIVE_TOLERANCE_METERS = 0.05; // "close enough" to finish
    public static final double DRIVE_RATE_TOLERANCE_M_PER_SEC = 0.1; // and nearly stopped
    public static final double TARGET_DISTANCE_METERS = 2.0; // never drive slower than 60% power

    public static final double DRIVE_TIMEOUT_SECONDS = 4.0;
    public static final double TURN_TIMEOUT_SECONDS = 2.0;
    public static final Pose2d GP_START_POSE = new Pose2d(2.0, 2.0, Rotation2d.kZero);
  }

  /**
   * Constants for the TEMPLATE mechanism in subsystems/ExampleSubsystem.java. When you copy
   * the example files to start a real mechanism, copy this block too and rename it (the
   * full recipe is in docs/07-common-tasks.md).
   */
  public static final class ExampleConstants {

    /** Placeholder. Assign the real ID with the REV Hardware Client, then update this. */
    public static final int MOTOR_CAN_ID = 30;

    /**
     * Pick the limit for the motor you actually use: NEO 550s overheat above about 25A,
     * while full-size NEOs are fine at 40-50A.
     */
    public static final int CURRENT_LIMIT_AMPS = 25;

    /** Fraction of full power (0 to 1) that ExampleCommand runs the mechanism at. */
    public static final double RUN_SPEED = 0.5;
  }

  private Constants() {} // prevent instantiation; this class is just a bag of numbers
}

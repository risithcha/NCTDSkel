package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.DriveSubsystem;

/**
 * Factory methods for the autonomous routines. RobotContainer puts these into a dashboard
 * chooser so the drive team picks one before each match.
 *
 * <p>STYLE NOTE, the second way to write commands: TeleopDriveCommand and
 * TurnToAngleCommand are full classes so you can see the command lifecycle. But most
 * commands don't need a whole file. Here we COMPOSE commands inline from small pieces:
 *
 * <ul>
 *   <li>{@code drive.run(...)} - "call this every 20ms" (requires the drive subsystem)
 *   <li>{@code Commands.runOnce(...)} - "call this once, then finish"
 *   <li>{@code .withTimeout(x)} / {@code .until(condition)} - end conditions
 *   <li>{@code .andThen(...)} - sequencing
 *   <li>{@code .finallyDo(...)} - cleanup that runs no matter how the command ends
 * </ul>
 *
 * Chaining these decorators builds real routines in a few readable lines. As your autos
 * grow ("score, then drive out, then balance"), they stay this readable.
 */
public final class Autos {

  /** For when you want the robot to sit still in auto (sometimes the right strategy call). */
  public static Command doNothing() {
    return Commands.none().withName("Do Nothing");
  }

  /**
   * The simplest auto that scores mobility points: drive forward at a fixed power for a
   * fixed time. Works even if every sensor on the robot is broken, which is exactly why
   * every team keeps one like it as a fallback.
   */
  public static Command driveForwardTime(DriveSubsystem drive) {
    return drive
        .run(() -> drive.arcadeDrive(AutoConstants.DRIVE_SPEED, 0.0))
        .withTimeout(AutoConstants.DRIVE_TIME_SECONDS)
        .finallyDo(drive::stop)
        .withName("Drive Forward (time)");
  }

  /**
   * Better: drive forward a specific DISTANCE using the encoders + odometry. Unlike the
   * timed version, this goes the same distance with a fresh or dying battery.
   *
   * <p>How it reads: reset the pose to the origin, then drive forward until x (meters
   * traveled away from the start) reaches the target, then stop.
   */
  public static Command driveForwardDistance(DriveSubsystem drive) {
    return Commands.runOnce(() -> drive.resetOdometry(Pose2d.kZero), drive)
        .andThen(
            drive
                .run(() -> drive.arcadeDrive(AutoConstants.DRIVE_SPEED, 0.0))
                .until(() -> drive.getPose().getX() >= AutoConstants.DRIVE_DISTANCE_METERS))
        .finallyDo(drive::stop)
        .withName("Drive Forward (distance)");
  }

  /** Demo of using a class-based command in a routine: quarter turn to the left. */
  public static Command turn90(DriveSubsystem drive) {
    return new TurnToAngleCommand(drive, 90.0).withName("Turn 90 Left");
  }

  /**
   * A taste of composition: drive out, turn around, drive back. Try it in simulation and
   * watch the pose trace the path on the field view. Build your real routines like this.
   */
  public static Command driveOutAndBack(DriveSubsystem drive) {
    return Commands.sequence(
            driveForwardDistance(drive),
            new TurnToAngleCommand(drive, 180.0),
            driveForwardDistance(drive))
        .withName("Drive Out and Back");
  }

  private Autos() {} // static factories only; never make an Autos object
}

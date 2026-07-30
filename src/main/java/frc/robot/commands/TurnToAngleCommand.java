package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.DriveSubsystem;
import org.littletonrobotics.junction.Logger;

/**
 * Rotates the robot in place BY a given number of degrees (e.g. +90 = quarter turn to the
 * left), using the gyro and a PID controller.
 *
 * <p>This is the skeleton's introduction to CLOSED-LOOP control, one of the most useful
 * concepts to learn next in FRC. Open loop ("apply 40% power for 2 seconds") gives a
 * different result every time as the battery drains and the carpet changes. Closed loop
 * measures the error (how far from the target we still are) every 20ms and computes the
 * output from it, so the robot actually arrives where you asked.
 *
 * <p>The P in PID: {@code output = kP * error}. Far from the target -> big output; close ->
 * gentle output. The D term dampens oscillation by reacting to how fast the error is
 * changing. (We leave I at zero; most FRC mechanisms don't need it.) Tune the gains in
 * Constants.AutoConstants, where the comments include the tuning recipe.
 */
public class TurnToAngleCommand extends Command {

  private final DriveSubsystem drive;
  private final double deltaDegrees;

  // WPILib's PID implementation. We give it gains now and a setpoint in initialize().
  private final PIDController controller =
      new PIDController(AutoConstants.TURN_KP, 0.0, AutoConstants.TURN_KD);

  /**
   * @param drive the drivetrain
   * @param deltaDegrees how far to turn from wherever the robot is pointing when the command
   *     starts. Positive = counter-clockwise (left), matching WPILib's convention.
   */
  public TurnToAngleCommand(DriveSubsystem drive, double deltaDegrees) {
    this.drive = drive;
    this.deltaDegrees = deltaDegrees;
    addRequirements(drive);

    // Angles wrap around: 179 degrees and -181 degrees are the same direction. This tells
    // the controller to always take the short way around instead of spinning the long way.
    controller.enableContinuousInput(-180.0, 180.0);

    // "Close enough to call it done": within 2 degrees AND turning slower than 5 deg/s
    // (so we don't declare victory while still spinning past the target).
    controller.setTolerance(
        AutoConstants.TURN_TOLERANCE_DEGREES, AutoConstants.TURN_RATE_TOLERANCE_DEG_PER_SEC);
  }

  @Override
  public void initialize() {
    // Compute the target from wherever we're pointing RIGHT NOW (this is why it's a
    // "turn BY" and not "turn TO" command, which is more intuitive for auto routines).
    double targetDegrees = drive.getHeadingDegrees() + deltaDegrees;
    controller.reset(); // forget any stale state from a previous run
    controller.setSetpoint(MathUtil.inputModulus(targetDegrees, -180.0, 180.0));
  }

  @Override
  public void execute() {
    // error -> output, then clamp so we never command a violent spin.
    double output = controller.calculate(drive.getHeadingDegrees());
    output = MathUtil.clamp(output, -AutoConstants.TURN_MAX_OUTPUT, AutoConstants.TURN_MAX_OUTPUT);

    // forward = 0: rotate in place.
    drive.arcadeDrive(0.0, output);

    // Log the controller's view of the world. Graph these three together in AdvantageScope
    // while tuning and you can SEE the P and D terms doing their jobs.
    Logger.recordOutput("TurnToAngle/SetpointDegrees", controller.getSetpoint());
    Logger.recordOutput("TurnToAngle/ErrorDegrees", controller.getError());
    Logger.recordOutput("TurnToAngle/Output", output);
  }

  @Override
  public boolean isFinished() {
    return controller.atSetpoint(); // within tolerance and nearly stopped
  }

  @Override
  public void end(boolean interrupted) {
    drive.stop();
  }
}

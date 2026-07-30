package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ExampleConstants;
import org.littletonrobotics.junction.Logger;

/**
 * TEMPLATE - copy this file to start your next mechanism (intake, roller, simple shooter,
 * and so on). It is a complete, working subsystem for "one motor that spins", which is the
 * starting point of most mechanisms. Nothing in RobotContainer creates it, so it has no
 * effect on the robot until you wire it up.
 *
 * <p>To turn this into a real mechanism:
 *
 * <ol>
 *   <li>Copy this file and {@code commands/ExampleCommand.java}; rename "Example" to your
 *       mechanism's name everywhere (VS Code: right-click the class name, Rename Symbol).
 *   <li>Copy the {@code ExampleConstants} block in Constants.java the same way. Set the
 *       real CAN ID there and on the device itself (REV Hardware Client).
 *   <li>In RobotContainer: create the subsystem as a field and bind a button to the
 *       command in configureBindings().
 *   <li>Copy {@code ExampleSubsystemTest.java} so every build exercises your mechanism.
 * </ol>
 *
 * <p>The same recipe with a worked intake example is in docs/07-common-tasks.md. For a
 * bigger subsystem with sensors, odometry-style math, and physics simulation, use
 * DriveSubsystem as the reference instead.
 */
public class ExampleSubsystem extends SubsystemBase {

  // ----------------------------------------------------------------------------------------
  // Hardware. Only this file may touch it (see the working rules in the README).
  // ----------------------------------------------------------------------------------------

  private final SparkMax motor = new SparkMax(ExampleConstants.MOTOR_CAN_ID, MotorType.kBrushless);

  // The NEO's built-in encoder. Without conversion factors it reads motor rotations and
  // RPM, which is fine for a roller; see DriveSubsystem for converting to real-world units.
  private final RelativeEncoder encoder = motor.getEncoder();

  /** Configures the hardware. Runs once when the robot code starts. */
  public ExampleSubsystem() {
    SparkMaxConfig config = new SparkMaxConfig();
    config
        .idleMode(IdleMode.kBrake) // brake so the mechanism stops when commands end
        .smartCurrentLimit(ExampleConstants.CURRENT_LIMIT_AMPS); // protects the motor
    motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  // ----------------------------------------------------------------------------------------
  // Public API - the verbs commands are allowed to use. Keep these small and named for
  // what they do on the robot, not how they do it.
  // ----------------------------------------------------------------------------------------

  /**
   * Run the mechanism.
   *
   * @param speed fraction of full power, -1.0 to 1.0
   */
  public void run(double speed) {
    motor.set(speed);
  }

  /** Stop the mechanism. Commands call this in end() so nothing keeps spinning. */
  public void stop() {
    motor.set(0.0);
  }

  /** Motor speed in RPM. Handy for "is it actually spinning?" checks and logging. */
  public double getVelocityRPM() {
    return encoder.getVelocity();
  }

  // ----------------------------------------------------------------------------------------
  // periodic() - runs every 20ms in every mode. Bookkeeping and logging only; movement
  // belongs in commands.
  // ----------------------------------------------------------------------------------------
  @Override
  public void periodic() {
    // Log the mechanism's inputs and outputs (working rule 4). When this mechanism
    // misbehaves at an event, these three values are where diagnosis starts.
    Logger.recordOutput("Example/AppliedOutput", motor.getAppliedOutput());
    Logger.recordOutput("Example/VelocityRPM", getVelocityRPM());
    // Current spikes when the mechanism grabs a game piece or jams. Many teams detect
    // "we have a game piece" from exactly this signal.
    Logger.recordOutput("Example/CurrentAmps", motor.getOutputCurrent());
  }

  // If you want this mechanism to work in simulation, add a simulationPeriodic() here.
  // DriveSubsystem shows the pattern: feed the applied output into a physics model, then
  // write the model's results back into the simulated sensors.
}

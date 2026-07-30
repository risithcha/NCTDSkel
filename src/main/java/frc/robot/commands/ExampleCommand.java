package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.ExampleConstants;
import frc.robot.subsystems.ExampleSubsystem;

/**
 * TEMPLATE - copy this file when a new mechanism needs a class-based command. It runs the
 * example mechanism while scheduled and stops it when it ends, which is exactly the shape a
 * hold-a-button binding wants:
 *
 * <pre>driverController.a().whileTrue(new ExampleCommand(example));</pre>
 *
 * <p>Before copying, decide whether you need a class at all. A command this small is
 * usually written inline instead (see Autos.java, and the startEnd example in
 * docs/07-common-tasks.md). Reach for a class when the command has real state or logic of
 * its own, like TeleopDriveCommand (input shaping) or TurnToAngleCommand (a PID
 * controller). The lifecycle below is the same either way.
 */
public class ExampleCommand extends Command {

  // The subsystem this command drives. Commands receive subsystems through their
  // constructor; they never create subsystems themselves.
  private final ExampleSubsystem example;

  public ExampleCommand(ExampleSubsystem example) {
    this.example = example;

    // Tell the scheduler which subsystems this command uses. Required, or two commands
    // could fight over the same motors.
    addRequirements(example);
  }

  /** Runs once when the command starts. One-time setup goes here. */
  @Override
  public void initialize() {
    // Nothing to set up for a simple roller. A PID command would reset its controller
    // here (see TurnToAngleCommand).
  }

  /** Runs every 20ms while the command is scheduled. The actual work goes here. */
  @Override
  public void execute() {
    example.run(ExampleConstants.RUN_SPEED);
  }

  /** Checked after each execute(). Return true when the job is done. */
  @Override
  public boolean isFinished() {
    // false = run until something else stops us, such as the driver releasing the button
    // of a whileTrue binding. A "finish on your own" command would return a condition
    // here, like TurnToAngleCommand's atSetpoint().
    return false;
  }

  /** Runs once when the command ends or is interrupted. Always leave the robot safe. */
  @Override
  public void end(boolean interrupted) {
    example.stop();
  }
}

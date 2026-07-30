package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.TeleopDriveCommand;
import frc.robot.commands.TurnToAngleCommand;
import frc.robot.subsystems.DriveSubsystem;

/**
 * This is where the robot is ASSEMBLED. Robot.java owns the clock (what runs when);
 * RobotContainer owns the robot's structure:
 *
 * <ul>
 *   <li>create each subsystem (exactly once)
 *   <li>connect controller buttons to commands ("bindings")
 *   <li>build the list of autonomous routines for the drive team to pick from
 * </ul>
 *
 * <p>When you add a mechanism to the robot, you'll add its subsystem here, give it a default
 * command or some button bindings, and you're on the field. The step-by-step recipe is in
 * docs/07-common-tasks.md.
 */
public class RobotContainer {

  // --- Subsystems ---------------------------------------------------------------------------
  // Each subsystem is created exactly once, here, and handed to whoever needs it. (If two
  // pieces of code each made their own DriveSubsystem, they'd fight over the motors.)
  private final DriveSubsystem drive = new DriveSubsystem();

  // --- Controllers --------------------------------------------------------------------------
  // CommandXboxController wraps a normal Xbox controller with a `Trigger` for every button,
  // which makes wiring buttons to commands one readable line (see configureBindings).
  private final CommandXboxController driverController =
      new CommandXboxController(OperatorConstants.DRIVER_CONTROLLER_PORT);

  // --- Autonomous chooser -------------------------------------------------------------------
  // A dropdown that appears on the dashboard (Elastic/AdvantageScope). The drive team picks
  // the routine before each match; getAutonomousCommand() reads the selection. (Robot.java
  // also logs which one actually ran, so you can prove it after a match.)
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    configureDefaultCommands();
    configureBindings();
    configureAutos();
  }

  /** A DEFAULT command runs automatically whenever nothing else is using that subsystem. */
  private void configureDefaultCommands() {
    // Teleop arcade drive, always on unless an auto (or the turn demo) borrows the
    // drivetrain. The lambdas are read live every 20ms:
    //  - Left stick Y  = forward/back. Pushing a stick FORWARD reads NEGATIVE (a USB HID
    //    convention as old as flight sims), so we negate it. If your robot drives backwards,
    //    do NOT remove this minus sign; flip the motor inversions in Constants instead.
    //  - Right stick X = turning. WPILib treats counter-clockwise as positive, but drivers
    //    expect "push right = turn right (clockwise)", so this is negated too.
    //  - Right bumper  = slow mode while held.
    drive.setDefaultCommand(
        new TeleopDriveCommand(
            drive,
            () -> -driverController.getLeftY(),
            () -> -driverController.getRightX(),
            driverController.rightBumper()));
  }

  /** Button -> command wiring. Add your own bindings here as the robot grows. */
  private void configureBindings() {
    // START button: resets odometry to "the robot is at the origin, facing forward". Handy
    // while practicing with the field view open. ignoringDisable lets it work in the pits
    // with the robot safely disabled.
    driverController
        .start()
        .onTrue(
            Commands.runOnce(() -> drive.resetOdometry(Pose2d.kZero))
                .ignoringDisable(true)
                .withName("Reset Odometry"));

    // B button: demo of a closed-loop command, a quarter turn to the left. Note it
    // interrupts the default drive command while it runs (both require the drivetrain),
    // then driving resumes automatically. Remove or remap once you've played with it.
    driverController.b().onTrue(new TurnToAngleCommand(drive, 90.0));
  }

  /** Register every autonomous routine the drive team should be able to pick. */
  private void configureAutos() {
    // The default is what runs if nobody touches the dashboard, so make it safe.
    autoChooser.setDefaultOption("Do Nothing", Autos.doNothing());
    autoChooser.addOption("Drive Forward (time)", Autos.driveForwardTime(drive));
    autoChooser.addOption("Drive Forward (distance)", Autos.driveForwardDistance(drive));
    autoChooser.addOption("Turn 90 Left", Autos.turn90(drive));
    autoChooser.addOption("Drive Out and Back", Autos.driveOutAndBack(drive));

    // Publish the dropdown to the dashboard (it appears as "Auto Choices").
    SmartDashboard.putData("Auto Choices", autoChooser);
  }

  /** Robot.java calls this when autonomous starts to find out what to run. */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}

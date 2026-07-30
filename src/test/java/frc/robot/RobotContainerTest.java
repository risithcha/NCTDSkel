package frc.robot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * A "smoke test": does the robot code even start?
 *
 * <p>It builds the whole RobotContainer (which constructs every subsystem against simulated
 * hardware) and runs the auto command for a moment. This catches a large class of mistakes
 * (duplicate CAN IDs, a typo'd constant that throws in a constructor, two commands requiring
 * a subsystem incorrectly) on your laptop, before they take down the robot at practice.
 *
 * <p>Tests run automatically on every build, and in CI on GitHub (see
 * .github/workflows/build.yml). Run just the tests with: ./gradlew test
 */
class RobotContainerTest {

  @BeforeAll
  static void setup() {
    // Boot the simulated Hardware Abstraction Layer, the same thing "Simulate Robot Code"
    // does. Without this, creating any hardware object throws.
    assert HAL.initialize(500, 0);

    // Note: we deliberately do NOT start the AdvantageKit Logger here, because it only runs
    // inside a LoggedRobot (it exits the JVM otherwise). With the logger stopped, all the
    // Logger.recordOutput(...) calls in the robot code become harmless no-ops.

    // Pretend a Driver Station is attached and the robot is enabled in autonomous, so
    // motor-safety and the simulated motor controllers behave like a real match.
    DriverStationSim.setDsAttached(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
  }

  @Test
  void robotBootsAndAutoRuns() {
    // If any subsystem constructor throws (bad CAN ID, bad config...), this line fails.
    RobotContainer container = assertDoesNotThrow(RobotContainer::new);

    // The chooser must always have a safe default selected before the dashboard loads.
    Command auto = container.getAutonomousCommand();
    assertNotNull(auto, "Auto chooser must provide a default command");

    // Run the scheduler for ~1 simulated second with the auto scheduled, the same loop
    // Robot.robotPeriodic runs. Any exception in a command or periodic() fails the test.
    CommandScheduler.getInstance().schedule(auto);
    assertDoesNotThrow(
        () -> {
          for (int i = 0; i < 50; i++) {
            CommandScheduler.getInstance().run();
          }
        });

    CommandScheduler.getInstance().cancelAll();
  }
}

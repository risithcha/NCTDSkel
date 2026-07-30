package frc.robot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.subsystems.ExampleSubsystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * TEMPLATE - the minimal smoke test to copy alongside a new mechanism. It proves the
 * subsystem constructs against simulated hardware and survives its basic lifecycle, which
 * catches config typos and bad CAN IDs on every build.
 *
 * <p>When your mechanism matters enough to deserve real physics checks (does it move the
 * right way, do the sensors agree), grow it using DriveSubsystemSimTest as the reference.
 *
 * <p>Reminder from build.gradle: each test CLASS gets its own JVM (forkEvery = 1), because
 * simulated CAN devices are global to the process. That's why this file creates its own
 * subsystem instead of sharing one with another test class.
 */
class ExampleSubsystemTest {

  @BeforeAll
  static void setup() {
    assert HAL.initialize(500, 0);

    // Enable the simulated robot so the motor controller accepts output.
    DriverStationSim.setDsAttached(true);
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
  }

  @Test
  void constructsRunsAndStops() {
    ExampleSubsystem example = assertDoesNotThrow(ExampleSubsystem::new);

    assertDoesNotThrow(
        () -> {
          for (int i = 0; i < 10; i++) {
            example.run(0.5);
            example.periodic();
          }
          example.stop();
        });
  }
}

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.subsystems.DriveSubsystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Drives the SIMULATED drivetrain and checks that physics, encoders, gyro, and odometry all
 * agree with common sense. If someone flips a motor inversion or breaks a conversion factor,
 * this test fails before the change ever reaches the robot.
 *
 * <p>Notice there is no robot here: the exact same DriveSubsystem code runs against the
 * physics model in simulationPeriodic(). That's the payoff of putting sim support in the
 * subsystem.
 *
 * <p>Testing quirk worth knowing: simulated CAN devices are global to the process, so a
 * device ID can only be constructed ONCE per test run. We therefore build a single shared
 * DriveSubsystem for the whole class and re-zero it before each test. (Test CLASSES are
 * isolated from each other by `forkEvery = 1` in build.gradle.)
 */
class DriveSubsystemSimTest {

  private static DriveSubsystem drive;

  @BeforeAll
  static void setup() {
    assert HAL.initialize(500, 0);
    // (The AdvantageKit Logger stays stopped in tests, because it only runs inside a
    // LoggedRobot. While stopped, Logger.recordOutput(...) calls are harmless no-ops.)

    // Enable the "robot" so the simulated SPARK MAXes actually apply output.
    DriverStationSim.setDsAttached(true);
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();

    drive = new DriveSubsystem();
  }

  @BeforeEach
  void resetBetweenTests() {
    // Let any motion from the previous test die out (and the simulated gyro catch up),
    // then call "here" the origin again.
    step(50, () -> drive.stop());
    drive.resetOdometry(Pose2d.kZero);
  }

  /**
   * Step the subsystem the way the real 20ms loop does.
   *
   * <p>We deliberately pace the loop with a small real sleep: the simulated Pigeon
   * publishes its yaw on a real-time schedule (like the real sensor), so a test loop that
   * spins thousands of iterations per second would read stale headings. ~10ms per 20ms sim
   * step keeps the sensor comfortably in sync while still running 2x real time.
   */
  private static void step(int loops, Runnable eachLoop) {
    for (int i = 0; i < loops; i++) {
      eachLoop.run();
      drive.periodic();
      drive.simulationPeriodic();
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(e);
      }
    }
  }

  @Test
  void drivingForwardMovesForward() {
    // Half power forward for 2 simulated seconds.
    step(100, () -> drive.arcadeDrive(0.5, 0.0));

    // The robot should have gone somewhere...
    assertTrue(
        drive.getPose().getX() > 0.5,
        "Robot should move forward (+x); got pose " + drive.getPose());
    // ...the encoders should agree (this catches broken conversion factors/inversions)...
    assertTrue(
        drive.getAverageDistanceMeters() > 0.5,
        "Encoders should measure forward travel; got " + drive.getAverageDistanceMeters());
    // ...and driving straight shouldn't change the heading much.
    assertEquals(
        0.0, drive.getHeadingDegrees(), 5.0, "Heading should stay near 0 when driving straight");

    // Let go of the stick: the robot should coast to a stop and STAY stopped.
    step(100, () -> drive.stop());
    double xAfterStopping = drive.getPose().getX();
    step(25, () -> drive.stop());
    assertEquals(xAfterStopping, drive.getPose().getX(), 0.05, "Robot should stop moving");
  }

  @Test
  void turningLeftIncreasesHeading() {
    // Positive rotation = counter-clockwise = heading increases (WPILib convention).
    // Keep the spin SHORT: headings wrap at +/-180 degrees, so a robot that turns "very
    // left" for long enough eventually reads as a negative angle.
    step(15, () -> drive.arcadeDrive(0.0, 0.3));

    double heading = drive.getHeadingDegrees();
    assertTrue(
        heading > 5.0 && heading < 175.0,
        "Positive rotation should turn counter-clockwise; heading = " + heading);

    // Spinning in place shouldn't move the robot across the floor.
    assertEquals(0.0, drive.getPose().getX(), 0.3, "Spin in place: x should stay ~0");
    assertEquals(0.0, drive.getPose().getY(), 0.3, "Spin in place: y should stay ~0");
  }
}

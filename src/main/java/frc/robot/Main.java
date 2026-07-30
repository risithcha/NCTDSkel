package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * The program's entry point. Its only job is handing your Robot class to WPILib.
 *
 * <p>Do not add code here, and do not change this file unless you rename or move Robot.java
 * (if you do, also update ROBOT_MAIN_CLASS in build.gradle).
 */
public final class Main {
  private Main() {}

  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}

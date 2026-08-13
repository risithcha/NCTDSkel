package frc.robot.util;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.event.EventLoop;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * A {@link CommandXboxController} implementation that works correctly in simulation on all
 * platforms (including macOS). The standard {@code CommandXboxController} has issues
 * interpreting inputs correctly in simulation on some platforms; this class wraps a
 * {@link SimXboxController} to provide reliable simulated input.
 *
 * <p>This class is used automatically by {@link frc.robot.RobotContainer} when running in
 * simulation (see {@link frc.robot.RobotContainer#RobotContainer()}). It defaults to the
 * {@link ControllerMappings#XBOX_MAPPING} but can be extended to support other mappings.
 */
public class CommandSimXboxController extends CommandXboxController {
  // ----------------------------------------------------------------------------------------
  // Fields
  // ----------------------------------------------------------------------------------------

  private final SimXboxController m_hid;
  private final ControllerMapping mapping;

  // ----------------------------------------------------------------------------------------
  // Constructor
  // ----------------------------------------------------------------------------------------

  /**
   * Creates a new simulated command controller on the given port.
   *
   * @param port The port index on the Driver Station.
   */
  public CommandSimXboxController(int port) {
    super(port);
    mapping = ControllerMappings.XBOX_MAPPING;
    m_hid = new SimXboxController(port, mapping);
  }

  // ----------------------------------------------------------------------------------------
  // HID Access
  // ----------------------------------------------------------------------------------------

  @Override
  public XboxController getHID() {
    return m_hid;
  }

  // ----------------------------------------------------------------------------------------
  // Button Triggers
  // ----------------------------------------------------------------------------------------

  @Override
  public Trigger a(EventLoop loop) {
    return button(mapping.getButton("A"), loop);
  }

  @Override
  public Trigger b(EventLoop loop) {
    return button(mapping.getButton("B"), loop);
  }

  @Override
  public Trigger x(EventLoop loop) {
    return button(mapping.getButton("X"), loop);
  }

  @Override
  public Trigger y(EventLoop loop) {
    return button(mapping.getButton("Y"), loop);
  }

  @Override
  public Trigger leftBumper(EventLoop loop) {
    return button(mapping.getButton("LeftBumper"), loop);
  }

  @Override
  public Trigger rightBumper(EventLoop loop) {
    return button(mapping.getButton("RightBumper"), loop);
  }

  @Override
  public Trigger back(EventLoop loop) {
    return button(mapping.getButton("Back"), loop);
  }

  @Override
  public Trigger start(EventLoop loop) {
    return button(mapping.getButton("Start"), loop);
  }

  @Override
  public Trigger leftStick(EventLoop loop) {
    return button(mapping.getButton("LeftStick"), loop);
  }

  @Override
  public Trigger rightStick(EventLoop loop) {
    return button(mapping.getButton("RightStick"), loop);
  }

  @Override
  public Trigger leftTrigger(double threshold, EventLoop loop) {
    return axisGreaterThan(mapping.getAxis("LeftTrigger"), threshold * 2 - 1, loop);
  }

  @Override
  public Trigger rightTrigger(double threshold, EventLoop loop) {
    return axisGreaterThan(mapping.getAxis("RightTrigger"), threshold * 2 - 1, loop);
  }

  // ----------------------------------------------------------------------------------------
  // Axis Getters
  // ----------------------------------------------------------------------------------------

  @Override
  public double getLeftX() {
    return getRawAxis(mapping.getAxis("LeftX"));
  }

  @Override
  public double getRightX() {
    return getRawAxis(mapping.getAxis("RightX"));
  }

  @Override
  public double getLeftY() {
    return getRawAxis(mapping.getAxis("LeftY"));
  }

  @Override
  public double getRightY() {
    return getRawAxis(mapping.getAxis("RightY"));
  }

  @Override
  public double getLeftTriggerAxis() {
    return m_hid.getLeftTriggerAxis();
  }

  @Override
  public double getRightTriggerAxis() {
    return m_hid.getRightTriggerAxis();
  }
}
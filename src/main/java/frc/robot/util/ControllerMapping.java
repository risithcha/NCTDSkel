package frc.robot.util;

import java.util.Map;

/**
 * Immutable mapping of logical button/axis names (e.g., "A", "LeftX") to raw controller
 * indices. Used by {@link SimXboxController} and {@link CommandSimXboxController} to provide
 * a consistent API across different physical controllers (Xbox, DualSense, etc.).
 *
 * <p>This class is intentionally simple: it holds two {@link Map}s and provides getters.
 * The actual mappings for specific controllers are defined in {@link ControllerMappings}.
 */
public class ControllerMapping {
  // ----------------------------------------------------------------------------------------
  // Fields
  // ----------------------------------------------------------------------------------------

  private final Map<String, Integer> buttonMap;
  private final Map<String, Integer> axisMap;

  // ----------------------------------------------------------------------------------------
  // Constructor
  // ----------------------------------------------------------------------------------------

  /**
   * Creates a new controller mapping.
   *
   * @param buttonMap maps logical button names to raw button indices
   * @param axisMap maps logical axis names to raw axis indices
   */
  public ControllerMapping(Map<String, Integer> buttonMap, Map<String, Integer> axisMap) {
    this.buttonMap = buttonMap;
    this.axisMap = axisMap;
  }

  // ----------------------------------------------------------------------------------------
  // Public API
  // ----------------------------------------------------------------------------------------

  /**
   * Gets the raw button index for a logical button name.
   *
   * @param name the logical button name (e.g., "A", "LeftBumper")
   * @return the raw button index, or {@code null} if the name is not mapped
   */
  public Integer getButton(String name) {
    return buttonMap.get(name);
  }

  /**
   * Gets the raw axis index for a logical axis name.
   *
   * @param name the logical axis name (e.g., "LeftX", "LeftTrigger")
   * @return the raw axis index, or {@code null} if the name is not mapped
   */
  public Integer getAxis(String name) {
    return axisMap.get(name);
  }
}
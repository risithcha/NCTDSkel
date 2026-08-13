package frc.robot.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Predefined {@link ControllerMapping}s for common gamepad types. The skeleton includes
 * mappings for Xbox and DualSense controllers; add more here if your team uses different
 * hardware.
 *
 * <p>These mappings are used by {@link CommandSimXboxController} (which defaults to the Xbox
 * mapping) and can be passed to {@link SimXboxController} directly for simulation testing.
 */
public class ControllerMappings {
  // ----------------------------------------------------------------------------------------
  // Predefined Mappings
  // ----------------------------------------------------------------------------------------

  /**
   * Raw HID indices an Xbox controller reports when the sim GUI's "Map Gamepad" option is off.
   * macOS has no Map Gamepad option, so this is the only layout available there. Windows and
   * Linux map to WPILib's standard layout by default, which stock CommandXboxController
   * already handles, so this mapping is not used on those platforms.
   */
  public static final ControllerMapping MACOS_XBOX_MAPPING;

  /** PlayStation DualSense controller layout. */
  public static final ControllerMapping DUALSENSE_MAPPING;

  // ----------------------------------------------------------------------------------------
  // Static Initialization
  // ----------------------------------------------------------------------------------------

  static {
    // --- Xbox Mapping ---------------------------------------------------------------------
    Map<String, Integer> xboxButtons = new HashMap<>();
    xboxButtons.put("A", 1);
    xboxButtons.put("B", 2);
    xboxButtons.put("X", 4);
    xboxButtons.put("Y", 5);
    xboxButtons.put("LeftBumper", 7);
    xboxButtons.put("RightBumper", 8);
    xboxButtons.put("Back", 11);
    xboxButtons.put("Start", 12);
    xboxButtons.put("LeftStick", 14);
    xboxButtons.put("RightStick", 15);

    Map<String, Integer> xboxAxes = new HashMap<>();
    xboxAxes.put("LeftX", 0);
    xboxAxes.put("LeftY", 1);
    xboxAxes.put("RightX", 2);
    xboxAxes.put("RightY", 3);
    xboxAxes.put("RightTrigger", 4);
    xboxAxes.put("LeftTrigger", 5);

    MACOS_XBOX_MAPPING = new ControllerMapping(xboxButtons, xboxAxes);

    // --- DualSense Mapping ----------------------------------------------------------------
    Map<String, Integer> dualSenseButtons = new HashMap<>();
    dualSenseButtons.put("A", 1); // Cross
    dualSenseButtons.put("B", 2); // Circle
    dualSenseButtons.put("X", 3); // Square
    dualSenseButtons.put("Y", 4); // Triangle
    dualSenseButtons.put("LeftBumper", 5); // L1
    dualSenseButtons.put("RightBumper", 6); // R1
    dualSenseButtons.put("Back", 7); // Create/Share
    dualSenseButtons.put("Start", 8); // Options
    dualSenseButtons.put("LeftStick", 10); // L3
    dualSenseButtons.put("RightStick", 11); // R3

    Map<String, Integer> dualSenseAxes = new HashMap<>();
    dualSenseAxes.put("LeftX", 0);
    dualSenseAxes.put("LeftY", 1);
    dualSenseAxes.put("RightX", 4);
    dualSenseAxes.put("RightY", 5);
    dualSenseAxes.put("LeftTrigger", 2); // L2
    dualSenseAxes.put("RightTrigger", 3); // R2

    DUALSENSE_MAPPING = new ControllerMapping(dualSenseButtons, dualSenseAxes);
  }
}
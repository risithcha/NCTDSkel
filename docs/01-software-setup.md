# 1. Software setup

Everything the programming laptop (and the robot's electronics) needs, in install order.
Budget an evening for this the first time. The official reference is
[docs.wpilib.org, "Zero to Robot"](https://docs.wpilib.org/en/stable/docs/zero-to-robot/introduction.html).

## On the programming laptop

### 1. WPILib 2026 (required; this is the big one)

Download the 2026.2.1 installer from [wpilib.org/release](https://wpilib.org/release)
and choose "Everything" plus "Install for this User". You get:

- 2026 WPILib VS Code, the editor you'll live in. It's a separate, specially configured
  VS Code. Always open the project with this one, because a plain VS Code install won't
  have the WPILib commands.
- A matching JDK (Java), Gradle support, and the build system.
- The simulation GUI, which lets you drive your code without a robot.
- AdvantageScope, the log viewer and robot data visualizer used in the logging chapter.
- Elastic, the driver dashboard.

Note: the WPILib version is per-season. This project targets 2026.x. When you update
WPILib for a new season later, see the "New season checklist" in docs/07-common-tasks.md.

### 2. NI FRC Game Tools (required to control a real robot; Windows only)

Get the 2026 version from
[ni.com/frc](https://www.ni.com/en/support/downloads/drivers/download.first-robotics-software.html).
You get:

- FRC Driver Station, the app that enables and disables the robot. It is the only way to
  drive.
- The roboRIO Imaging Tool, which flashes the robot's controller with the season's image.

### 3. REV Hardware Client (required for SPARK MAXes)

From [revrobotics.com/software](https://www.revrobotics.com/software/). Used over USB-C to:

- Update SPARK MAX firmware. Do this first; fresh-from-box firmware is always old.
- Assign each SPARK MAX its CAN ID. These are the numbers in `Constants.java`, and the
  exact assignments are listed in docs/02-hardware-and-wiring.md.

### 4. Phoenix Tuner X (required for the Pigeon 2.0)

From the Microsoft Store or [CTRE's site](https://docs.ctr-electronics.com/). Used to
update the Pigeon 2.0's firmware and set its CAN ID (20 in this project).

## On the robot

### 5. Image the roboRIO

Once per season, the roboRIO must be flashed with the season's image. For the roboRIO 2.0
(the current model), use the Imaging Tool's SD-card mode: pop out the microSD card, write
the image, reinsert, and set your team number when prompted. Guide:
[Imaging your roboRIO](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-3/imaging-your-roborio.html).

### 6. Configure the radio

The VH-109 radio needs a one-time season configuration with your team number so your
laptop can connect to the robot's Wi-Fi. Follow
[the radio guide](https://docs.wpilib.org/en/stable/docs/zero-to-robot/step-3/radio-programming.html);
it's a short web-page setup.

### 7. Device firmware pass (15 minutes that saves hours)

With the REV Hardware Client and Phoenix Tuner X, walk every device: latest firmware,
correct CAN ID, and a human-readable name for each SPARK MAX (for example "Left Leader").
Do this before chasing any "robot won't move" bug. Mismatched firmware is the number one
rookie time sink.

## Sanity checklist before you call setup done

- [ ] 2026 WPILib VS Code opens this project and Build Robot Code prints `BUILD SUCCESSFUL`
- [ ] Driver Station launches and shows your team number
- [ ] Every SPARK MAX has current firmware, the CAN ID from the table, and blinks when identified
- [ ] The Pigeon 2.0 is visible in Phoenix Tuner X with ID 20
- [ ] The roboRIO is imaged for 2026 with the team number set
- [ ] The laptop connects to the robot's Wi-Fi and Driver Station shows green Communications

# 8. Next steps: from "it drives" to "it competes"

You have a driving, logging, simulating robot. Here is a proven order for leveling up,
with the best resource for each step. Don't rush. Each step is a season's worth of
learning for a new team, and a robot that reliably does a little beats one that
unreliably does everything.

## 1. Closed-loop control everywhere (start here)

You've met PID in `TurnToAngleCommand`. The same idea (measure, compare to target,
correct) runs every good mechanism: arm angles, shooter RPM, drive velocity.

- [WPILib PID docs](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/introduction/introduction-to-pid.html)
- SysId (installed with WPILib) measures your drivetrain and computes feedforward gains
  from data instead of guess-and-check:
  [System Identification](https://docs.wpilib.org/en/stable/docs/software/advanced-controls/system-identification/introduction.html)
- The SPARK MAX can also run PID onboard, which is smoother than roboRIO loops:
  [REVLib closed loop](https://docs.revrobotics.com/revlib/spark/closed-loop)

## 2. Real autonomous: path following

Driving curves instead of drive-turn-drive. Two good free tools:

- [PathPlanner](https://pathplanner.dev) lets you draw paths in a GUI and follow them with
  its library (differential drive is supported via the LTV controller).
- [Choreo](https://choreo.autos) generates time-optimal trajectories.

Both need good odometry (you have it) plus SysId characterization (step 1).

## 3. Vision: let the robot see

FRC fields are covered in AprilTags, QR-like markers with known positions. A camera plus
a coprocessor recognizes them and corrects your odometry, which removes drift and makes
auto-aim possible.

- [PhotonVision](https://photonvision.org) is free and runs on an Orange Pi or Raspberry
  Pi.
- [Limelight](https://limelightvision.io) is a buy-it-and-it-works camera unit.
- The code change in this project: swap `DifferentialDriveOdometry` for
  `DifferentialDrivePoseEstimator` (same API, plus `addVisionMeasurement()`).

## 4. Full AdvantageKit: IO layers and replay

This project logs outputs. Full AdvantageKit also records every hardware input, which
unlocks replay: feed a match log back through modified code and see what it would have
done. It is a powerful way to debug problems that only happened once.

The price is structuring each subsystem as an interface with hardware and sim
implementations ("IO layers"). Take this on when the team is comfortable with everything
else on this list.

- [What is AdvantageKit / recording inputs](https://docs.advantagekit.org/getting-started/what-is-advantagekit/)
- [Team 6328's template projects](https://docs.advantagekit.org/getting-started/template-projects/)
  include a SPARK-based differential drive, which is a direct upgrade path from this
  skeleton.

## 5. Grow the codebase like a team

- Push this repo to GitHub. CI is already set up in `.github/workflows/build.yml`.
- Branch per feature, use pull requests, and require a green build to merge.
- Write a sim test for each new subsystem (copy `DriveSubsystemSimTest`).
- Rotate students through the codebase so knowledge never lives in one head.

## On the horizon: the 2027 control system ("SystemCore")

FIRST is replacing the roboRIO with a new controller starting in the 2027 season, with a
long transition. Legacy hardware is expected to stay legal into the early 2030s; see
[FIRST's updates](https://community.firstinspires.org/march-updates-on-the-future-robot-controller).
Don't panic, and don't over-buy roboRIO-specific spares. Everything this project teaches
(command-based structure, subsystems, logging, PID, simulation) carries over, and WPILib
is being ported to the new hardware by the same people.

## Where to get help

- [docs.wpilib.org](https://docs.wpilib.org) is the manual, and it's genuinely good.
- [Chief Delphi](https://www.chiefdelphi.com) is the FRC forum. Search first; there are 25
  years of answers. Post logs and code when asking.
- The [FRC Discord](https://discord.gg/frc) has real-time help in #programming-java.
- Nearby veteran teams: most will happily mentor a rookie team's programmers if you ask.

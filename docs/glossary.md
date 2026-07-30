# Glossary: FRC words, decoded

| Term | Meaning |
| --- | --- |
| AdvantageKit | Logging framework (by Team 6328) this project uses in its simple mode |
| AdvantageScope | The log viewer and data visualizer installed with WPILib |
| AprilTag | Barcode-like marker on the field that cameras use to locate the robot |
| Arcade drive | One stick for speed plus one for turning (what this project ships) |
| Auto / Autonomous | First 15s of a match; the robot runs a pre-picked routine, hands off |
| Brownout | Battery voltage dips so low the roboRIO cuts motor power to survive |
| Brushless motor | Modern FRC motor (NEO, Kraken). Its sensor cable must be connected |
| CAN bus | The daisy-chained data network connecting the roboRIO, motor controllers, and sensors |
| CAN ID | The unique number identifying each device on the CAN bus |
| Command | A "verb": code that runs start, loop, end, using one or more subsystems |
| Command-based | WPILib's recommended code structure (subsystems, commands, scheduler) |
| Deadband | Small stick range around center treated as zero so the robot doesn't creep |
| Default command | Runs on a subsystem whenever nothing else claims it (our teleop drive) |
| Deploy | Compile the code and send it to the roboRIO over the network |
| Differential / tank drive | Drivetrain with independent left and right sides; turns by speed difference |
| Driver Station (DS) | The app (and laptop) that enables, disables, and drives the robot |
| Elastic | The driver-facing dashboard app installed with WPILib |
| Encoder | Sensor measuring how far and how fast a motor spins (built into every NEO) |
| FMS | Field Management System; runs real matches and replaces your enable button |
| Follower | Motor controller configured to copy another (the "leader") automatically |
| Gear ratio | Motor turns per wheel turn (10.71:1 on the stock KitBot gearbox) |
| Gyro / IMU | Sensor measuring robot heading (our Pigeon 2.0) |
| HAL | Hardware Abstraction Layer, WPILib's bridge to real or simulated hardware |
| KitBot | The standard-parts robot design FIRST publishes every season |
| NetworkTables (NT) | The pub/sub network protocol robots use to talk to dashboards |
| NEO | REV's standard brushless motor (our drive motors) |
| Odometry | Math combining encoders and gyro into "where am I on the field" |
| PDH | Power Distribution Hub; splits battery power and holds the breakers |
| PID | Feedback control: output proportional to error (see TurnToAngleCommand) |
| Pigeon 2.0 | CTRE's gyro/IMU product |
| Pose | Position plus heading: (x meters, y meters, angle) |
| roboRIO | The robot's onboard computer that runs your deployed code |
| RSL | Robot Signal Light, the orange light; solid means disabled, blinking means enabled |
| Scheduler | The command-based engine: runs commands and enforces subsystem ownership |
| SPARK MAX | REV's motor controller (one per motor, on the CAN bus) |
| Subsystem | A "noun": one mechanism plus the only code allowed to touch its hardware |
| Teleop | The driver-controlled phase of a match |
| Trigger | A condition (usually a button) wired to start or stop commands |
| Vendordep | JSON file telling the build to pull a vendor's library (REV, CTRE, and so on) |
| WPILib | The official FRC software library and tools this project stands on |
| .wpilog | The log file format AdvantageKit writes and AdvantageScope reads |

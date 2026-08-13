package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.DriveSubsystem;
import org.littletonrobotics.junction.Logger;

public class DriveToDistanceCommand extends Command {
    private final DriveSubsystem drive;
    private final double distanceMeters;
    private double startDistanceMeters;
    private double targetHeadingDegrees;

  // WPILib's PID implementation. We give it gains now and a setpoint in initialize().
    private final PIDController controller = new PIDController(AutoConstants.DRIVE_KP, 0.0, AutoConstants.DRIVE_KD);

    public DriveToDistanceCommand(DriveSubsystem drive, double distanceMeters) {
      this.drive = drive;
      this.distanceMeters = distanceMeters;
      addRequirements(drive);

      controller.setTolerance(
        AutoConstants.DRIVE_TOLERANCE_METERS, AutoConstants.DRIVE_RATE_TOLERANCE_M_PER_SEC);
    }

    @Override
  public void initialize() {
    controller.reset();
    startDistanceMeters = drive.getAverageDistanceMeters();
    targetHeadingDegrees = drive.getHeadingDegrees();
        // Target is relative to where the robot started
    controller.setSetpoint(distanceMeters);
  }

  @Override
  public void execute() {
    // error -> output, then clamp so we never command a violent spin.
    double output = controller.calculate(drive.getAverageDistanceMeters() - startDistanceMeters);
    output = MathUtil.clamp(output, -AutoConstants.DRIVE_MAX_OUTPUT, AutoConstants.DRIVE_MAX_OUTPUT);

    double headingError = targetHeadingDegrees - drive.getHeadingDegrees();
    double turnOutput = headingError * AutoConstants.DRIVE_HEADING_KP;
    turnOutput = MathUtil.clamp(turnOutput, -AutoConstants.TURN_MAX_OUTPUT, AutoConstants.TURN_MAX_OUTPUT);
    
    drive.arcadeDrive(output, turnOutput);

    // Log the controller's view of the world. Graph these three together in AdvantageScope
    // while tuning and you can SEE the P and D terms doing their jobs.
    Logger.recordOutput("DriveToDistance/SetpointMeters", controller.getSetpoint());
    Logger.recordOutput("DriveToDistance/ErrorMeters", controller.getError());
    Logger.recordOutput("DriveToDistance/Output", output);
    SmartDashboard.putNumber("DriveToDistance/GyroYawDegrees", drive.getHeadingDegrees());
    SmartDashboard.putNumber("DriveToDistance/HeadingErrorDegrees", headingError);
    SmartDashboard.putNumber("DriveToDistance/TurnOutput", turnOutput);
  }

  @Override
  public boolean isFinished() {
    return controller.atSetpoint(); // within tolerance and nearly stopped
  }

  @Override
  public void end(boolean interrupted) {
    drive.stop();
  }
}
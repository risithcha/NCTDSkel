package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class WiggleCommand extends Command {
    private final DriveSubsystem drive;
    private final double periodSeconds;
    private final double timeSeconds;
    private final double magnitude;
    private final Timer timer = new Timer();

    public WiggleCommand(DriveSubsystem drive, double periodSeconds, double timeSeconds, double magnitude) {
        this.drive = drive;
        this.periodSeconds = periodSeconds;
        this.timeSeconds= timeSeconds;
        this.magnitude = magnitude;
        addRequirements(drive);
        setName("WiggleCommand");
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        double elapsedTime = timer.get();
        double rotation = ((int) (elapsedTime / 0.5) % 2 == 0) ? 0.5 : -0.5;
        drive.arcadeDrive(0.0, rotation);
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(3.0);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }
}
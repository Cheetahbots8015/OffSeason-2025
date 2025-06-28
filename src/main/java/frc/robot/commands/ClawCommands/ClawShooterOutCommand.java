package frc.robot.commands.ClawCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.claw.ClawSubsystem;

public class ClawShooterOutCommand extends Command {
  private final ClawSubsystem m_subsystem;
  private double m_volts;

  public ClawShooterOutCommand(ClawSubsystem subsystem, double volts) {
    m_subsystem = subsystem;
    m_volts = volts;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setShooterVoltage(-m_volts);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setShooterVoltage(0.0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

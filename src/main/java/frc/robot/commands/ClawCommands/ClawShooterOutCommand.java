package frc.robot.commands.ClawCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.claw.ClawSubsystem;

public class ClawShooterOutCommand extends Command {
  private final ClawSubsystem m_subsystem;

  public ClawShooterOutCommand(ClawSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setShooterVoltage(-SmartDashboard.getNumber("ClawShooterVolts", 0.0));
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

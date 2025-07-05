package frc.robot.commands.SuperstructureCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Superstructure;

public class RequestIdleCommand extends Command {
  private final Superstructure m_subsystem;

  public RequestIdleCommand(Superstructure subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setIdleRequest(true);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setIdleRequest(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

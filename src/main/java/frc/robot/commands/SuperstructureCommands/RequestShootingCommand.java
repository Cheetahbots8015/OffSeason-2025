package frc.robot.commands.SuperstructureCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Superstructure;

public class RequestShootingCommand extends Command {
  private final Superstructure m_subsystem;

  public RequestShootingCommand(Superstructure subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setShootingRequest(true);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setShootingRequest(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

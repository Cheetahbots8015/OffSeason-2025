package frc.robot.commands.SuperstructureCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Superstructure;

public class RequestForL2Command extends Command {
  private final Superstructure m_subsystem;

  public RequestForL2Command(Superstructure subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setL2Request(true);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setL2Request(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

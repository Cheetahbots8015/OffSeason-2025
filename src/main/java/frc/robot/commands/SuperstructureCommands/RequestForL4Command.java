package frc.robot.commands.SuperstructureCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Superstructure;

public class RequestForL4Command extends Command {
  private final Superstructure m_subsystem;

  public RequestForL4Command(Superstructure subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setL4Request(true);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setL4Request(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

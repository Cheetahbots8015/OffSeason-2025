package frc.robot.commands.SuperstructureCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Superstructure;

public class RequestForL1Command extends Command {
  private final Superstructure m_subsystem;

  public RequestForL1Command(Superstructure subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setL1Request(true);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setL1Request(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

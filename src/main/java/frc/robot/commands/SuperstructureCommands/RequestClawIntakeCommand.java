package frc.robot.commands.SuperstructureCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Superstructure;

public class RequestClawIntakeCommand extends Command {
  private final Superstructure m_subsystem;

  public RequestClawIntakeCommand(Superstructure subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setClawIntakeRequest(true);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setClawIntakeRequest(false);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

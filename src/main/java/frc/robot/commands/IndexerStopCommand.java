package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.indexer.indexerSubsystem;

public class IndexerStopCommand extends Command {
  private final indexerSubsystem m_subsystem;

  public IndexerStopCommand(indexerSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.shutDown();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pivot.PivotSubsystem;

public class PivotClockWiseCommand extends Command {
  private final PivotSubsystem m_subsystem;

  public PivotClockWiseCommand(PivotSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.defaultIdleVelocity();
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.shutDown();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}


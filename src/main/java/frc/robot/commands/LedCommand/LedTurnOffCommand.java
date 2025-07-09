package frc.robot.commands.LedCommand;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.led.LedSubsystem;

public class LedTurnOffCommand extends Command {
  private final LedSubsystem m_subsystem;

  public LedTurnOffCommand(LedSubsystem subsystem) {
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
  public void end(boolean interrupted) {
    m_subsystem.shutDown();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

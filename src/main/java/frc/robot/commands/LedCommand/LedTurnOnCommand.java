package frc.robot.commands.LedCommand;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.LedConstants;
import frc.robot.subsystems.led.LedSubsystem;

public class LedTurnOnCommand extends Command {
  private final LedSubsystem m_subsystem;
  private RGBWColor m_color;
  private LedConstants.AnimationType m_type;
  private boolean m_offWhenStoped;

  public LedTurnOnCommand(
      LedSubsystem subsystem,
      RGBWColor color,
      LedConstants.AnimationType type,
      boolean offWhenStoped) {
    m_subsystem = subsystem;
    m_color = color;
    m_type = type;
    m_offWhenStoped = offWhenStoped;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setLedMode(m_type, m_color);
  }

  @Override
  public void end(boolean interrupted) {
    if (m_offWhenStoped) m_subsystem.shutDown();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

package frc.robot.commands.LedCommand;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.led.LedSubsystem;

public class LedTurnSomeOnCommand extends Command {
  private final LedSubsystem m_subsystem;
  private RGBWColor[] m_colors;
  private boolean m_offWhenStoped;

  public LedTurnSomeOnCommand(LedSubsystem subsystem, RGBWColor[] colors, boolean offWhenStoped) {
    m_subsystem = subsystem;
    m_colors = colors;
    m_offWhenStoped = offWhenStoped;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setLEDRange(m_colors);
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

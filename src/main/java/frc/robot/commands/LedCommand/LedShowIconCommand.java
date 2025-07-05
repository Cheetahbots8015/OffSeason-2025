package frc.robot.commands.LedCommand;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.LedConstants;
import frc.robot.subsystems.led.LedSubsystem;

public class LedShowIconCommand extends Command {
  private final LedSubsystem m_subsystem;
  private boolean m_offWhenStoped;
  private int m_colorIndex;

  public LedShowIconCommand(LedSubsystem subsystem, int ColorIndex, boolean offWhenStoped) {
    m_subsystem = subsystem;
    m_colorIndex = ColorIndex;
    m_offWhenStoped = offWhenStoped;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    switch (m_colorIndex) {
      case 0:
        TestIcon();
        break;
      case 1:
        TestIcon2();
        break;

      default:
        break;
    }
  }

  private void TestIcon() {
    boolean isWhite = true;
    for (int i = 0; i < LedConstants.LedHeight * LedConstants.LedWidth; i++) {
      if (isWhite) {
        m_subsystem.setSingleLed(i, new RGBWColor(0, 255, 0));
        isWhite = false;
      } else {
        m_subsystem.setSingleLed(i, new RGBWColor(0, 0, 0));
        isWhite = true;
      }
    }
  }

  private void TestIcon2() {
    int line = 0;
    for (int i = 0; i < LedConstants.LedHeight * LedConstants.LedWidth; i++) {
      if (i % LedConstants.LedHeight == 0) {
        line++;
      }
      m_subsystem.setSingleLed(i, new RGBWColor(line * 5, line * 8, line * 10));
    }
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

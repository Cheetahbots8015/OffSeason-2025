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
      case 2:
        TestIcon3();
        break;
      default:
        break;
    }
  }

  private void TestIcon() {
    m_subsystem.setSingleLed(0, new RGBWColor(0, 255, 0));

    for (int i = 0; i < LedConstants.LedSize(); i += 2) {
      m_subsystem.setSingleLed(i, new RGBWColor(255, 255, 255));
    }

    m_subsystem.update();
  }

  private void TestIcon2() {
    m_subsystem.update();
  }

  private void TestIcon3() {
    int step = 255 / (LedConstants.LedSize());
    int i = 0;
    for (; i < LedConstants.LedSize() / 2; i++) {
      m_subsystem.setSingleLed(i, new RGBWColor(255 - i * step, 255 - i * step, 255 - i * step));
    }

    for (; i < LedConstants.LedSize(); i++) {
      m_subsystem.setSingleLed(i, new RGBWColor(i * step, i * step, i * step));
    }

    m_subsystem.update();
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

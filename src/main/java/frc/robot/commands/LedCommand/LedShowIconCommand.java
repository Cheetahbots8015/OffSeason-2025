package frc.robot.commands.LedCommand;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.LedConstants;
import frc.robot.subsystems.led.LedSubsystem;
import frc.robot.util.LedUtil;

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

    // boolean isWhite = true;
    // for (int i = 10; i < LedConstants.LedHeight * LedConstants.LedWidth; i++) {
    //   if (isWhite) {
    //     isWhite = false;
    //   } else {
    //     m_subsystem.setSingleLed(i, new RGBWColor(0, 0, 0));
    //     isWhite = true;
    //   }
    // }

    m_subsystem.update();
  }

  private void TestIcon2() {
    int line = 0;
    for (int i = 0; i < LedConstants.LedHeight * LedConstants.LedWidth; i++) {
      if (i % LedConstants.LedHeight == 0) {
        line++;
      }
      m_subsystem.setSingleLed(i, new RGBWColor(line / 5, line / 5, line / 5));
    }
    m_subsystem.update();
  }

  private void TestIcon3() {
    int pos = 0;
    int line = 0;
    for (int i = 0; i < LedConstants.LedHeight * LedConstants.LedWidth; i++) {
      if (LedUtil.isInStartOfRaw(i)) {
        line++;
      }

      if (LedUtil.isInSpecificPositionInTheRow(
          LedUtil.isInPositiveSequenceRow(i) ? line - 1 : line, i)) {
        m_subsystem.setSingleLed(i, new RGBWColor(69, 69, 69));
      }

      pos += LedUtil.isInPositiveSequenceRow(i) ? 1 : -1;
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

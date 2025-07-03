package frc.robot.subsystems.led;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.LedConstants;

public class LedSubsystem extends SubsystemBase {
  private LedIO io;

  public LedSubsystem(LedIO io) {
    this.io = io;
  }

  public void periodic() {}

  public void shutDown() {
    io.AllOff();
  }

  public void setLedMode(LedConstants.AnimationType modeId, RGBWColor color) {
    io.setLedMode(modeId, color);
  }

  public void setLEDRange(RGBWColor[] colors) {
    int i = 0;
    for (RGBWColor rgbwColor : colors) {
      io.setSingleLed(i, rgbwColor);
      i++;
    }
  }
}

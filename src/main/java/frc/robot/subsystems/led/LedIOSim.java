package frc.robot.subsystems.led;

import com.ctre.phoenix6.signals.RGBWColor;
import frc.robot.constants.LedConstants;

public class LedIOSim implements LedIO {
  public LedIOSim() {}

  public LedConstants.AnimationType mode = LedConstants.AnimationType.None;

  public double r = 0, g = 0, b = 0, w = 0;

  @Override
  public void setLedMode(LedConstants.AnimationType modeId, RGBWColor color) {
    mode = modeId;
    r = color.Red;
    g = color.Green;
    b = color.Blue;
    w = color.White;
  }

  @Override
  public void AllOff() {
    mode = LedConstants.AnimationType.None;
    r = 0;
    g = 0;
    b = 0;
    w = 0;
  }

  @Override
  public void setSingleLed(int id, RGBWColor color) {
    // not available
  }
}

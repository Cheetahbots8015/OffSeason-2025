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

  public void setSingleLed(int id, RGBWColor color) {
    io.setSingleLed(id, color);
  }

  public void setBrightness(double number) {
    io.setBrightness(number);
  }
}

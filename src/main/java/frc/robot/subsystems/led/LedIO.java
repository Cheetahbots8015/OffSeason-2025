package frc.robot.subsystems.led;

import com.ctre.phoenix6.signals.RGBWColor;
import frc.robot.constants.LedConstants;
import org.littletonrobotics.junction.AutoLog;

public interface LedIO {
  @AutoLog
  public static class LedIOInputs {
    double CandleOutCurrent = 0.0d;
    double Candle5VOutVoltage = 0.0d;

    double CandleTemperature = 0.0d;
  }

  public default void setLedMode(LedConstants.AnimationType modeId, RGBWColor color) {}

  public default void setSingleLedWithoutAnyIndexAdd(int id, RGBWColor color) {}

  public default void setSingleLed(int id, RGBWColor color) {}

  public default void AllOff() {}

  public default void updateInputs(LedIOInputs inputs) {}

  public default void setBrightness(double number) {}

  public default void updateLEDs() {}
}

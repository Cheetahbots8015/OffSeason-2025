package frc.robot.subsystems.led;

import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.controls.TwinkleOffAnimation;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.RGBWColor;
import com.ctre.phoenix6.signals.StatusLedWhenActiveValue;
import com.ctre.phoenix6.signals.StripTypeValue;
import frc.robot.constants.LedConstants;

public class LedIOCANdle implements LedIO {
  private final CANdle m_candle = new CANdle(LedConstants.CandleDeviceID, "rio");
  private double brightness = 1.0d;

  public LedIOCANdle() {
    var cfg = new CANdleConfiguration();
    /* set the LED strip type and brightness */
    cfg.LED.StripType = StripTypeValue.GRB;
    cfg.LED.BrightnessScalar = 1.0;
    /* disable status LED when being controlled */
    cfg.CANdleFeatures.StatusLedWhenActive = StatusLedWhenActiveValue.Disabled;

    m_candle.getConfigurator().apply(cfg);

    /* clear all previous animations */
    for (int i = 0; i < 8; ++i) {
      m_candle.setControl(new EmptyAnimation(i));
    }
    /* set the onboard LEDs to a solid color */
    m_candle.setControl(new SolidColor(0, 7).withColor(new RGBWColor(0, 0, 0)));
  }

  @Override
  public void setLedMode(LedConstants.AnimationType modeId, RGBWColor color) {

    switch (modeId) {
      default:
      case AllOn:
        m_candle.setControl(
            new SolidColor(LedConstants.LedIndexStart, LedConstants.LedIndexEnd).withColor(color));
        break;
      case ColorFlow:
        m_candle.setControl(
            new ColorFlowAnimation(LedConstants.LedIndexStart, LedConstants.LedIndexEnd)
                .withSlot(0)
                .withColor(color));
        break;
      case Rainbow:
        m_candle.setControl(
            new RainbowAnimation(LedConstants.LedIndexStart, LedConstants.LedIndexEnd).withSlot(0));
        break;
      case Twinkle:
        m_candle.setControl(
            new TwinkleAnimation(LedConstants.LedIndexStart, LedConstants.LedIndexEnd)
                .withSlot(0)
                .withColor(color));
        break;
      case TwinkleOff:
        m_candle.setControl(
            new TwinkleOffAnimation(LedConstants.LedIndexStart, LedConstants.LedIndexEnd)
                .withSlot(0)
                .withColor(color));
        break;
      case Fire:
        m_candle.setControl(
            new FireAnimation(LedConstants.LedIndexStart, LedConstants.LedIndexEnd).withSlot(0));
        break;
      case None:
        m_candle.setControl(
            new SolidColor(LedConstants.LedIndexStart, LedConstants.LedIndexEnd)
                .withColor(new RGBWColor(0, 0, 0, 0)));

        break;
    }
  }

  @Override
  public void AllOff() {
    m_candle.setControl(new SolidColor(0, 399).withColor(new RGBWColor(0, 0, 0, 0)));

    for (int i = 0; i < 8; ++i) {
      m_candle.setControl(new EmptyAnimation(i));
    }
  }

  @Override
  public void setSingleLed(int id, RGBWColor color) {
    if (id < 0 && id > 399) {
      throw new IllegalArgumentException("LED ID must be between 0 and 399");
    }
    m_candle.setControl(new SolidColor(id, id).withColor(color));
  }

  @Override
  public void updateInputs(LedIOInputs inputs) {
    inputs.CandleOutCurrent = m_candle.getOutputCurrent().getValueAsDouble();
    inputs.CandleOutTemperature = m_candle.getDeviceTemp().getValueAsDouble();
    inputs.CandleOutBrightness = brightness;
  }

  @Override
  public void setBrightness(double number) {
    brightness = number;
    var cfg = new CANdleConfiguration();
    cfg.LED.StripType = StripTypeValue.GRB;
    cfg.LED.BrightnessScalar = number;
    cfg.CANdleFeatures.StatusLedWhenActive = StatusLedWhenActiveValue.Disabled;

    m_candle.getConfigurator().apply(cfg);
  }
}

package frc.robot.constants;

public class LedConstants {
  public static final int CandleDeviceID = 6;

  public static final int LedIndexStart = 0;

  public static final int LedIndexEnd = 399;

  public static final int ExternalLedStripIndexStart = 8;

  public static final int LedWidth = 52;

  public static final int LedHeight = 1;

  public static final double statusUpdateFrequency = 50.0;

  public static int LedSize() {
    return LedWidth * LedHeight;
  }

  public enum AnimationType {
    None,
    AllOn,
    ColorFlow,
    Fire,
    Larson,
    Rainbow,
    RgbFade,
    SingleFade,
    Strobe,
    Twinkle,
    TwinkleOff,
  }
}

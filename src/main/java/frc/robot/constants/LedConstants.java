package frc.robot.constants;

public class LedConstants {
  public static final int CandleDeviceID = 6;

  public static final int LedIndexStart = 0;

  public static final int LedIndexEnd = 399;

  public static final int LedWidth = 15;

  public static final int LedHeight = 15;

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

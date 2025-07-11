package frc.robot.subsystems.led;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.LedConstants;
import frc.robot.subsystems.led.LedIO.LedIOInputs;
import org.littletonrobotics.junction.Logger;

public class LedSubsystem extends SubsystemBase {
  private LedIO io;
  private final LedIOInputsAutoLogged inputs = new LedIOInputsAutoLogged();

  private double m_intervalPerFrame = 0.022d; // around 45fps
  private double m_lastTimeStamp = 0d;
  private boolean m_isSim = false;

  public LedSubsystem(LedIO io) {
    this.io = io;
    m_isSim = RobotBase.isSimulation();
    m_lastTimeStamp = Timer.getFPGATimestamp();
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Led", inputs);

    if (m_lastTimeStamp + m_intervalPerFrame <= Timer.getFPGATimestamp()) { // refresh animation
      m_lastTimeStamp = Timer.getFPGATimestamp();
      if (CANdleInternalLedRingAnimation_isOn) CANdleInternalLedRingAnimation();
      if (LedMovingGradientAnimation_isOn) LedMovingGradientAnimation();
      if (LedMiddleMovingGradientAnimation_isOn) LedMiddleMovingGradientAnimation();
    }
  }

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

  public void update() {
    io.updateLEDs();
  }

  public LedIOInputs getInput() {
    return inputs;
  }

  // animation moudle

  public void setAnimation(int id, boolean state) {
    switch (id) {
      case 0:
        LedMovingGradientAnimation_isOn = state;
        break;
      case 1:
        LedMiddleMovingGradientAnimation_isOn = state;
        break;

      case -255:
        CANdleInternalLedRingAnimation_isOn = state;
        break;
      default:
        break;
    }
  }

  public void setAnimation(int id) {
    switch (id) {
      case 0:
        LedMovingGradientAnimation_isOn = !LedMovingGradientAnimation_isOn;
        break;
      case 1:
        LedMiddleMovingGradientAnimation_isOn = !LedMiddleMovingGradientAnimation_isOn;
        break;

      case -255:
        CANdleInternalLedRingAnimation_isOn = !CANdleInternalLedRingAnimation_isOn;
        break;

      default:
        break;
    }
  }

  private boolean CANdleInternalLedRingAnimation_isOn = false;
  private int CANdleInternalLedRingAnimation_LedRingCount = 0;

  public void CANdleInternalLedRingAnimation() {
    if (m_isSim) return; // no CANdle in emulator
    if (CANdleInternalLedRingAnimation_LedRingCount == 7)
      CANdleInternalLedRingAnimation_LedRingCount = 0;
    else CANdleInternalLedRingAnimation_LedRingCount++;

    for (int i = 0; i < 8; i++) {
      if (i == CANdleInternalLedRingAnimation_LedRingCount)
        io.setSingleLedWithoutAnyIndexAdd(i, new RGBWColor(0, 255, 0));
      else io.setSingleLedWithoutAnyIndexAdd(i, new RGBWColor(0, 0, 0));
    }
  }

  private boolean LedMovingGradientAnimation_isOn = true;
  private int LedMovingGradientAnimation_CurrentLed = 0;
  private boolean LedMovingGradientAnimation_TouchEnd = false;

  public void LedMovingGradientAnimation() {
    int stepR = 21;
    int stepG = 12;
    int stepB = 2;
    int step;
    io.AllOff();

    io.setSingleLed(LedMovingGradientAnimation_CurrentLed, new RGBWColor(248, 146, 35));

    if (LedMovingGradientAnimation_TouchEnd) {
      for (int i = 1; i <= 12; i++) {
        if (LedMovingGradientAnimation_CurrentLed - i < 0) {
          LedMovingGradientAnimation_TouchEnd = false;

          if (m_isSim) io.updateLEDs();
          return;
        }
        step = 12 - i;
        io.setSingleLed(
            LedMovingGradientAnimation_CurrentLed - i,
            new RGBWColor(stepR * step, stepG * step, stepB * step + 1));
      }

      for (int i = 12; i >= 1; i--) {
        step = 12 - i;
        if (LedMovingGradientAnimation_CurrentLed + i > LedConstants.LedSize() - 1) continue;
        io.setSingleLed(
            LedMovingGradientAnimation_CurrentLed + i,
            new RGBWColor(stepR * step, stepG * step, stepB * step + 1));
      }

      if (LedMovingGradientAnimation_CurrentLed == LedConstants.LedSize() - 1)
        LedMovingGradientAnimation_CurrentLed = 0;
      else LedMovingGradientAnimation_CurrentLed--;
      if (m_isSim) io.updateLEDs();

      return;
    }

    for (int i = 1; i <= 12; i++) {
      if (LedMovingGradientAnimation_CurrentLed - i < 0) continue;

      step = 12 - i;
      io.setSingleLed(
          LedMovingGradientAnimation_CurrentLed - i,
          new RGBWColor(stepR * step, stepG * step, stepB * step + 1));
    }

    for (int i = 12; i >= 1; i--) {
      step = 12 - i;
      if (LedMovingGradientAnimation_CurrentLed + i > LedConstants.LedSize() - 1) {
        LedMovingGradientAnimation_TouchEnd = true;
        if (m_isSim) io.updateLEDs();
        return;
      }

      io.setSingleLed(
          LedMovingGradientAnimation_CurrentLed + i,
          new RGBWColor(stepR * step, stepG * step, stepB * step + 1));
    }

    if (LedMovingGradientAnimation_CurrentLed == LedConstants.LedSize() - 1)
      LedMovingGradientAnimation_CurrentLed = 0;
    else LedMovingGradientAnimation_CurrentLed++;

    if (m_isSim) io.updateLEDs();
  }

  private boolean LedMiddleMovingGradientAnimation_isOn = false;
  private int LedMiddleMovingGradientAnimation_CurrentSideLed = 0;
  private boolean LedMiddleMovingGradientAnimation_touchesEnd = false;

  public void LedMiddleMovingGradientAnimation() {
    int middle = LedConstants.LedSize() / 2;
    double stepR = 248 / (LedConstants.LedSize() / 2);
    double stepG = 146 / (LedConstants.LedSize() / 2);
    double stepB = 35 / (LedConstants.LedSize() / 2);
    int step;
    io.AllOff();
    io.setSingleLed(middle, new RGBWColor(248, 146, 35));
    for (int i = 0; i <= LedMiddleMovingGradientAnimation_CurrentSideLed; i++) {
      step = middle - i;
      if (LedConstants.LedSize() > i + middle) {
        io.setSingleLed(
            i + middle, new RGBWColor((int) stepR * step, (int) stepG * step, (int) stepB * step));
      }
      if (middle - i > 0) {
        io.setSingleLed(
            middle - i, new RGBWColor((int) stepR * step, (int) stepG * step, (int) stepB * step));
      }
    }

    if (LedMiddleMovingGradientAnimation_CurrentSideLed
                + (LedMiddleMovingGradientAnimation_touchesEnd ? -1 : 1)
            < 0
        || LedMiddleMovingGradientAnimation_CurrentSideLed
                + (LedMiddleMovingGradientAnimation_touchesEnd ? -1 : 1)
            > middle) { // calculate the next step of LED
      if (LedMiddleMovingGradientAnimation_touchesEnd) {
        LedMiddleMovingGradientAnimation_CurrentSideLed = 0;
      } else {
        LedMiddleMovingGradientAnimation_CurrentSideLed = middle;
      }
      LedMiddleMovingGradientAnimation_touchesEnd = !LedMiddleMovingGradientAnimation_touchesEnd;
    } else {
      LedMiddleMovingGradientAnimation_CurrentSideLed +=
          LedMiddleMovingGradientAnimation_touchesEnd ? -2 : 2;
    }

    if (m_isSim) io.updateLEDs();
  }
}

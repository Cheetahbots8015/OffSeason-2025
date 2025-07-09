package frc.robot.subsystems.led;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.LedConstants;
import frc.robot.subsystems.led.LedIO.LedIOInputs;
import org.littletonrobotics.junction.Logger;

public class LedSubsystem extends SubsystemBase {
  private LedIO io;
  private final LedIOInputsAutoLogged inputs = new LedIOInputsAutoLogged();

  public LedSubsystem(LedIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs(
        "Led",
        inputs); // Send input data to the logging framework (or update from the log during replay)
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
}

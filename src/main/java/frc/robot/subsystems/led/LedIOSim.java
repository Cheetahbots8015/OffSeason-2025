package frc.robot.subsystems.led;

import com.ctre.phoenix6.signals.RGBWColor;
import edu.wpi.first.math.MathUtil;
import frc.robot.constants.LedConstants;

public class LedIOSim implements LedIO {
  public LedIOSim() {
    this.cols = LedConstants.LedWidth;
    this.rows = LedConstants.LedHeight;
    pixels = new RGBWColor[rows][cols];
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        pixels[y][x] = new RGBWColor(0, 0, 0, 0);
      }
    }
  }

  public LedConstants.AnimationType mode = LedConstants.AnimationType.None;

  private int cols, rows;
  private RGBWColor[][] pixels;

  private int[] indexToCoord(int index) {
    int row = index / cols;
    int colInRow = index % cols;
    int col = (row % 2 == 0) ? colInRow : (cols - 1 - colInRow);
    return new int[] {row, col};
  }

  @Override
  public void setSingleLed(int index, RGBWColor color) {
    int[] rc = indexToCoord(index);
    pixels[rc[0]][rc[1]] = color;
  }

  private void render() { // AI generated stuff
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        RGBWColor c = pixels[y][x];
        System.err.print(String.format("\u001B[48;2;%d;%d;%dm  ", c.Red, c.Green, c.Blue));
      }
      System.err.print("\u001B[0m\n");
    }
    System.err.print(String.format("\u001B[%dA", rows));
  }

  @Override
  public void AllOff() {
    mode = LedConstants.AnimationType.None;
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        pixels[y][x] = new RGBWColor(0, 0, 0, 0);
      }
    }
  }

  @Override
  public void setBrightness(double number) {}

  @Override
  public void updateInputs(LedIOInputs inputs) {
    inputs.CandleOutCurrent = MathUtil.clamp(0, 0, 10.0);
    inputs.CandleTemperature = MathUtil.clamp(0, -273.15, 114514.0);
    inputs.Candle5VOutVoltage = MathUtil.clamp(5, 4, 6);
  }

  @Override
  public void updateLEDs() {
    render();
  }
}

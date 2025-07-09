package frc.robot.util;

import frc.robot.constants.LedConstants;

public class LedUtil {
  public static boolean isInSpecificPositionInTheRow(int locationToBe, int currentIndex) {
    var line =
        (currentIndex - (currentIndex % LedConstants.LedWidth))
            / LedConstants.LedWidth; // avoid rounded result cause magic result
    var column = currentIndex - line * LedConstants.LedWidth;
    return isInPositiveSequenceRow(currentIndex)
        ? column == locationToBe - (line != 0 ? 1 : 0)
        : column == LedConstants.LedWidth - locationToBe + 1;
  }

  public static boolean isInPositiveSequenceRow(int index) {
    var line =
        (index - (index % LedConstants.LedWidth))
            / LedConstants.LedWidth; // avoid rounded result cause magic result
    return line % 2 == 0;
  }

  public static boolean isInNegativeSequenceRow(int index) {
    return !isInPositiveSequenceRow(index);
  }

  public static boolean isInStartOfRaw(int index) {
    if (isInPositiveSequenceRow(index)) {
      return isInSpecificPositionInTheRow(0, index);
    } else {
      return isInSpecificPositionInTheRow(LedConstants.LedWidth - 1, index);
    }
  }

  public static boolean isInEndOfRaw(int index) {
    if (isInPositiveSequenceRow(index)) {
      return isInSpecificPositionInTheRow(LedConstants.LedWidth - 1, index);
    } else {
      return isInSpecificPositionInTheRow(0, index);
    }
  }
}

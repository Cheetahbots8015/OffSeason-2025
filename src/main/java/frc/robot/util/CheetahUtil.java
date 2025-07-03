package frc.robot.util;

public class CheetahUtil {
  /*
   * This class is a placeholder for utility methods related to the Cheetah robot.
   */
  public static boolean isNear(double value, double target, double tolerance) {
    return Math.abs(value - target) <= tolerance;
  }

  // default tolerance of 0.05
  public static boolean isNear(double value, double target) {
    return isNear(value, target, 0.05); // Default tolerance of 0.05
  }

  // compare with percentage tolerance
  public static boolean isNearPercentage(double value, double target, double percentage) {
    double tolerance = Math.abs(target * percentage);
    return isNear(value, target, tolerance);
  }

  // compare with percentage tolerance with default percentage of 0.01
  public static boolean isNearPercentage(double value, double target) {
    return isNearPercentage(value, target, 0.01); // Default percentage of 1%
  }
}

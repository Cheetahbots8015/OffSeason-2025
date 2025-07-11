package frc.robot.constants;

import edu.wpi.first.math.util.Units;

public class AutoConstants {
  public static final boolean autoPreventFlipping = false;
  public static final double maxVelocityMPS = 4;
  public static final double maxAccelerationMPSSq = 4;
  public static final double maxAngularVelocityRadPerS = Units.degreesToRadians(360);
  public static final double maxAngularAccelerationRadPerSSq = Units.degreesToRadians(540);

  public static final double RStartIntakePointX = 15.1;
  public static final double RStartIntakePointY = 2.36;
  public static final double RStartIntakePointRotationRad = -50.0 / 180.0 * Math.PI;

  public static final double RStationX = 16.3;
  public static final double RStationY = 1.05;
  public static final double RStationRotationRad = -50.0 / 180.0 * Math.PI;

  public static final double TestX = 0.0;
  public static final double TestY = 0.0;
  public static final double TestRotationRad = 0.0;

  public static final double ReefX = 14.0;
  public static final double ReefY = 2.3;
  public static final double ReefRotationRad = 0.666 * Math.PI;
}

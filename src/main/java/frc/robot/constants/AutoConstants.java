package frc.robot.constants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public class AutoConstants {
  public static final Pose2d rightPrepareToIntakePoint =
      new Pose2d(2.5, 1.9, new Rotation2d(Math.toRadians(-126)));
  public static final Pose2d rightStation =
      new Pose2d(1.5, 0.7, new Rotation2d(Math.toRadians(-126)));
  //   public static final Pose2d leftPrepareToIntakePoint = new Pose2d(null, null, null);
  //   public static final Pose2d leftStation = new Pose2d(null, null, null);
}

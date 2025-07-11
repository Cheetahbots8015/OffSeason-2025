package frc.robot.util;

import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.constants.AutoConstants;
import java.util.List;

public class AutoUtil {
  public static final PathConstraints constraints =
      new PathConstraints(
          AutoConstants.maxVelocityMPS,
          AutoConstants.maxAccelerationMPSSq,
          AutoConstants.maxAngularVelocityRadPerS,
          AutoConstants.maxAngularAccelerationRadPerSSq);

  // The constraints for this path.

  public static PathPlannerPath generatePath(GoalEndState endState, Pose2d... pose2ds) {
    // Create a list of waypoints from poses. Each pose represents one waypoint.
    // The rotation component of the pose should be the direction of travel. Do not use holonomic
    // rotation.
    List<Waypoint> waypoints = PathPlannerPath.waypointsFromPoses(pose2ds);

    // Create the path using the waypoints created above
    PathPlannerPath path =
        new PathPlannerPath(
            waypoints,
            constraints,
            null, // The ideal starting state, this is only relevant for pre-planned paths, so can
            // be null for on-the-fly paths.
            endState // Goal end state. You can set a holonomic rotation here. If using a
            // differential drivetrain, the rotation will have no effect.
            );

    // Prevent the path from being flipped if the coordinates are already correct
    path.preventFlipping = AutoConstants.autoPreventFlipping;

    return path;
  }
}

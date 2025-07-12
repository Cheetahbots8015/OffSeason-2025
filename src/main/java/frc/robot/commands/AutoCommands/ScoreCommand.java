// package frc.robot.commands.AutoCommands;

// import edu.wpi.first.math.geometry.Pose2d;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.RobotContainer;
// import frc.robot.constants.FieldConstants;

// public class ScoreCommand extends Command {
//   RobotContainer m_RobotContainer;
//   Pose2d m_Pose2d;
//   Pose2d m_PushPose2d;

//   public ScoreCommand(
//       RobotContainer _RobotContainer, Pose2d _Pose2d, int _Level, boolean _isinverted) {
//     m_RobotContainer = _RobotContainer;
//     m_Pose2d = _Pose2d;
//     m_PushPose2d = _Pose2d;
//     addRequirements(m_RobotContainer.drive);
//     m_RotationController.enableContinuousInput(-Math.PI, Math.PI);

//     m_PoseXController.setSetpoint(0);
//     m_PoseYController.setSetpoint(0);
//     if (_isinverted) {
//       m_RotationController.setSetpoint(m_Pose2d.getRotation().getRadians() + Math.PI);
//     } else {
//       m_RotationController.setSetpoint(m_Pose2d.getRotation().getRadians());
//     }
//     m_Level = _Level;
//     m_Trajectory2d = new Trajectory2d("ScoreL" + _Level, 1.);
//     m_RisingTrajectory2d = new Trajectory2d("Rest2L" + _Level, 1.);
//     m_isInverted = _isinverted;
//   }

// }

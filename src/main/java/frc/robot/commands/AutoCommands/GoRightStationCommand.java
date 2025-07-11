package frc.robot.commands.AutoCommands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.Drive;
import frc.robot.util.AutoUtil;

public class GoRightStationCommand extends Command {
  private final Drive m_drive;
  private Pose2d[] targets;
  private PathPlannerPath m_path;
  private Command m_followCommand;

  public GoRightStationCommand(Drive drive, Pose2d... targets) {
    this.m_drive = drive;
    this.targets = targets;
    addRequirements(drive);
  }

  @Override
  public void initialize() {
    m_path =
        AutoUtil.generatePath(
            new GoalEndState(0, targets[targets.length - 1].getRotation()), targets);
    m_followCommand = AutoBuilder.followPath(m_path);

    m_followCommand.initialize();
  }

  @Override
  public void execute() {
    m_followCommand.execute();
  }

  @Override
  public boolean isFinished() {
    return m_followCommand.isFinished();
  }

  @Override
  public void end(boolean interrupted) {
    m_followCommand.end(interrupted);
    m_drive.stop();
  }
}

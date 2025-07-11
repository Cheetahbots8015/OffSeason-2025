package frc.robot.commands.AutoCommands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.AutoConstants;
import frc.robot.constants.IntakeConstants;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.util.AutoUtil;

public class TestAuto extends Command {
  private final Drive m_drive;
  private final IntakeSubsystem m_intake;
  private PathPlannerPath m_path;
  private Command m_followCommand;

  public TestAuto(Drive drive, IntakeSubsystem intake) {
    this.m_drive = drive;
    this.m_intake = intake;

    addRequirements(drive, intake);
  }

  @Override
  public void initialize() {
    // 读取Dashboard上的最新值
    double x = SmartDashboard.getNumber("TestX", AutoConstants.TestX);
    double y = SmartDashboard.getNumber("TestY", AutoConstants.TestY);
    double rotationDeg =
        SmartDashboard.getNumber("TestRotationDeg", Math.toDegrees(AutoConstants.TestRotationRad));
    Rotation2d rotation = Rotation2d.fromDegrees(rotationDeg);

    m_path =
        AutoUtil.generatePath(
            new GoalEndState(0, rotation), m_drive.getPose(), new Pose2d(x, y, rotation));

    m_followCommand = AutoBuilder.followPath(m_path);

    m_followCommand.initialize();

    // intake动作也可以放这里，如果你愿意的话
    m_intake.setArmVoltage(IntakeConstants.armVoltage);
    m_intake.setIntakeVoltage(3);
    m_intake.setIndexerVoltage(3);
  }

  @Override
  public void execute() {
    m_intake.setArmVoltage(IntakeConstants.armVoltage);
    m_intake.setIntakeVoltage(3);
    m_intake.setIndexerVoltage(3);

    m_followCommand.execute();
  }

  @Override
  public boolean isFinished() {
    return m_intake.getInput().Canrange || m_followCommand.isFinished();
  }

  @Override
  public void end(boolean interrupted) {
    m_intake.setArmVoltage(0.0);
    m_intake.setIntakeVoltage(0.0);
    m_intake.setIndexerVoltage(0.0);

    m_followCommand.end(interrupted);
    m_drive.stop();
  }
}

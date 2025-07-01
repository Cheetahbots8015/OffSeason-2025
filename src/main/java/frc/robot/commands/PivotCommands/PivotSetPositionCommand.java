package frc.robot.commands.PivotCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pivot.PivotSubsystem;

public class PivotSetPositionCommand extends Command {
  private final PivotSubsystem m_subsystem;

  public PivotSetPositionCommand(PivotSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setPosition(SmartDashboard.getNumber("PivotMotionMagicPositionRads", 0.0));
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}

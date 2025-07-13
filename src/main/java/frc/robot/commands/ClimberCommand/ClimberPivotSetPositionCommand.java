package frc.robot.commands.ClimberCommand;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.ClimberConstants;
import frc.robot.subsystems.climber.ClimberSubsystem;

public class ClimberPivotSetPositionCommand extends Command {
  private final ClimberSubsystem m_subsystem;
  private double m_position;

  public ClimberPivotSetPositionCommand(ClimberSubsystem subsystem, double position) {
    m_subsystem = subsystem;
    m_position = position;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setClimberPivotPosition(m_position);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return Math.abs(m_subsystem.getInput().PivotPositionDeg - m_position)
        < ClimberConstants.PositionDeadband;
  }
}

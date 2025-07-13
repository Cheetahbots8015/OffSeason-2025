package frc.robot.commands.ClimberCommand;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.ClimberConstants;
import frc.robot.subsystems.climber.ClimberSubsystem;

public class ClimberPivotDefaultCommand extends Command {
  private final ClimberSubsystem m_subsystem;
  private double m_position;

  public ClimberPivotDefaultCommand(ClimberSubsystem subsystem, double position) {
    m_subsystem = subsystem;
    m_position = position;
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_subsystem.setClimberPivotPosition(m_position);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(m_subsystem.getInput().PivotPositionDeg - m_position)
        < ClimberConstants.PositionDeadband;
  }
}

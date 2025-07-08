package frc.robot.commands.ElevatorCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.ElevatorConstants;
import frc.robot.subsystems.elevator.ElevatorSubsystem;

public class ElevatorLittleCommand extends Command {
  private final ElevatorSubsystem m_subsystem;
  private final double m_position;

  public ElevatorLittleCommand(ElevatorSubsystem subsystem, double position) {
    m_subsystem = subsystem;
    m_position = position;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setElevatorVoltage(
        (m_position - m_subsystem.getInput().ElevatorHeightMeters) > 0 ? 2.5 : -1.5);
    ;
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.VelocityVoltage(0.0);
  }

  @Override
  public boolean isFinished() {
    return Math.abs(m_subsystem.getInput().ElevatorHeightMeters - m_position)
        < ElevatorConstants.PositionDeadband;
  }
}

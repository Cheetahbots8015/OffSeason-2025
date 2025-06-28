package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.ElevatorConstants;
import frc.robot.subsystems.elevator.ElevatorSubsystem;

public class ElevatorUpCommand extends Command {
  private final ElevatorSubsystem m_subsystem;

  public ElevatorUpCommand(ElevatorSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.runPercentOutput(ElevatorConstants.UpValue);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.shutdown();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;

public class ElevatorDownCommand extends Command {
  private final ElevatorSubsystem m_subsystem;

  public ElevatorDownCommand(ElevatorSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.defaultIdleVelocity();
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

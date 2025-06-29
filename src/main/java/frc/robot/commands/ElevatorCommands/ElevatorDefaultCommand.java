package frc.robot.commands.ElevatorCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;

public class ElevatorDefaultCommand extends Command {
  private final ElevatorSubsystem m_subsystem;

  public ElevatorDefaultCommand(ElevatorSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.VelocityVoltage();
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setElevatorVoltage(0.0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

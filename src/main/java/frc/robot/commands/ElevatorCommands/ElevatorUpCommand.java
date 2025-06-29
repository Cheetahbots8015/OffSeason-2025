package frc.robot.commands.ElevatorCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
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
    m_subsystem.setElevatorVoltage(SmartDashboard.getNumber("ElevatorVolts", 0));
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

package frc.robot.commands.ElevatorCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.elevator.ElevatorSubsystem;

public class ElevatorSetPositionCommand extends Command {
  private final ElevatorSubsystem m_subsystem;

  public ElevatorSetPositionCommand(ElevatorSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setPosition(SmartDashboard.getNumber("SetMotionMagicPositionRads", 100.0));
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.VelocityVoltage(0.0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

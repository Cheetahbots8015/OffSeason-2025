package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class IntakeRollerInCommand extends Command {
  private final IntakeSubsystem m_subsystem;

  public IntakeRollerInCommand(IntakeSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setIntakeVoltage(SmartDashboard.getNumber("IntakeRollerVolts", 0.0));
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setIntakeVoltage(0.0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

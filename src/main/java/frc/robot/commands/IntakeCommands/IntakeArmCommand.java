package frc.robot.commands.IntakeCommands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class IntakeArmCommand extends Command {
  private final IntakeSubsystem m_subsystem;
  private double m_volts;

  public IntakeArmCommand(IntakeSubsystem subsystem, double volts) {
    m_subsystem = subsystem;
    m_volts = volts;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setArmVoltage(1);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.VelocityVoltage(0);
  }

  @Override
  public boolean isFinished() {
    return m_subsystem.getInput().ArmPositionRad > 15;
  }
}

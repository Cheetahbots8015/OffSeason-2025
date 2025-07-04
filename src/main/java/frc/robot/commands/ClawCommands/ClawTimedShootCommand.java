package frc.robot.commands.ClawCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.claw.ClawSubsystem;

public class ClawTimedShootCommand extends Command {
  private final ClawSubsystem m_subsystem;
  private Timer m_timer = new Timer();

  public ClawTimedShootCommand(ClawSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {
    m_timer.reset();
    m_timer.start();
  }

  @Override
  public void execute() {
    m_subsystem.setIntakeVoltage(2);
    m_subsystem.setShooterVoltage(12);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setIntakeVoltage(0);
    m_subsystem.setShooterVoltage(0.0);
  }

  @Override
  public boolean isFinished() {
    return m_timer.get() > 2.0;
  }
}

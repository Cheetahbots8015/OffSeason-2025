package frc.robot.commands.ClawCommands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.claw.ClawSubsystem;

public class ClawTimedIntakeCommand extends Command {
  private final ClawSubsystem m_subsystem;
  private Timer m_timer = new Timer();

  public ClawTimedIntakeCommand(ClawSubsystem subsystem) {
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
    m_subsystem.setIntakeVoltage(1);
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setIntakeVoltage(0.0);
  }

  @Override
  public boolean isFinished() {
    return m_timer.get() > 3.0;
  }
}

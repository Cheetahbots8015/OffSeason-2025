package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.roller.RollerSubsystem;
import frc.robot.subsystems.roller.RollerSubsystem.rollerIdleState;

public class RollerOutCommand extends Command {
  private final RollerSubsystem m_subsystem;

  public RollerOutCommand(RollerSubsystem subsystem) {
    m_subsystem = subsystem;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_subsystem.setSystemIdleState(rollerIdleState.out);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_subsystem.setSystemIdleState(rollerIdleState.out);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_subsystem.shutdown();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_subsystem.getSystemIdleState() == rollerIdleState.out;
  }
}

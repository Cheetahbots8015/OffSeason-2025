package frc.robot.commands.PivotCommands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.pivot.PivotSubsystem;

public class PivotAntiClockWiseCommand extends Command {
  private final PivotSubsystem m_subsystem;

  public PivotAntiClockWiseCommand(PivotSubsystem subsystem) {
    m_subsystem = subsystem;
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    m_subsystem.setPivotVoltage(SmartDashboard.getNumber("PivotVolts", 0));
    ;
  }

  @Override
  public void end(boolean interrupted) {
    m_subsystem.setPivotVoltage(0);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}

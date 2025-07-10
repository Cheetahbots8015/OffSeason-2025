package frc.robot.commands.LedCommand;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.led.LedSubsystem;

public class LedSetAnimationCommand extends Command {
  private final LedSubsystem m_subsystem;
  private int m_animationIndex, m_modeToSwitch;

  public LedSetAnimationCommand(
      LedSubsystem subsystem, int AnimationIndex, boolean modeToSwitch, boolean isToggleMode) {
    m_subsystem = subsystem;
    m_animationIndex = AnimationIndex;
    m_modeToSwitch = isToggleMode ? -1 : (modeToSwitch ? 1 : 0);
    addRequirements(subsystem);
  }

  @Override
  public void initialize() {}

  @Override
  public void execute() {
    switch (m_modeToSwitch) {
      case -1:
        m_subsystem.setAnimation(m_animationIndex);
        break;
      case 0:
        m_subsystem.setAnimation(m_animationIndex, false);
        break;
      case 1:
        m_subsystem.setAnimation(m_animationIndex, true);
        break;
      default:
        break;
    }
  }

  public void EnableAnimation() {
    m_subsystem.setAnimation(m_animationIndex, true);
  }

  public void DisableAnimation() {
    m_subsystem.setAnimation(m_animationIndex, false);
  }

  @Override
  public void end(boolean interrupted) {}

  @Override
  public boolean isFinished() {
    return false;
  }
}

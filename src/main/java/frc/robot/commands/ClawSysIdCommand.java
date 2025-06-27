package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.claw.ClawSubsystem;

public class ClawSysIdCommand extends Command {
  public enum TestType {
    QUASISTATIC,
    DYNAMIC
  }

  private final ClawSubsystem clawSubsystem;
  private final TestType testType;
  private final boolean isForward;

  private double startTime;
  private double appliedVoltage = 0;

  public ClawSysIdCommand(ClawSubsystem clawSubsystem, TestType testType, boolean isForward) {
    this.clawSubsystem = clawSubsystem;
    this.testType = testType;
    this.isForward = isForward;

    addRequirements(clawSubsystem);
  }

  @Override
  public void initialize() {
    startTime = System.currentTimeMillis() / 1000.0;
    appliedVoltage = 0;
  }

  @Override
  public void execute() {
    double elapsedTime = (System.currentTimeMillis() / 1000.0) - startTime;

    switch (testType) {
      case QUASISTATIC:
        // Ramp voltage from 0 to 12V over 3 seconds
        appliedVoltage = isForward ? 1 : -1 * Math.min(12, 4 * elapsedTime);
        break;
      case DYNAMIC:
        // Apply step voltage of 7V for 2 seconds
        appliedVoltage = isForward ? 1 : -1 * 7;
        break;
    }

    clawSubsystem.setIntakeVoltage(appliedVoltage);
  }

  @Override
  public void end(boolean interrupted) {
    clawSubsystem.setIntakeVoltage(0);
  }

  @Override
  public boolean isFinished() {
    double elapsedTime = (System.currentTimeMillis() / 1000.0) - startTime;

    return (testType == TestType.QUASISTATIC && elapsedTime > 3)
        || (testType == TestType.DYNAMIC && elapsedTime > 2);
  }

  @Override
  public String getName() {
    return String.format("ClawSysId-%s-%s", testType.toString(), isForward ? "Forward" : "Reverse");
  }
}

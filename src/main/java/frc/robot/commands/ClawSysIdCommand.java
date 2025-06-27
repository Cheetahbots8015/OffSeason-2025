package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.claw.ClawIOTalonFX;
import frc.robot.subsystems.claw.ClawSubsystem;

public class ClawSysIdCommand extends Command {
  public enum TestType {
    QUASISTATIC,
    DYNAMIC
  }

  public enum Direction {
    FORWARD(1),
    REVERSE(-1);

    public final int value;

    Direction(int value) {
      this.value = value;
    }
  }

  private final ClawSubsystem clawSubsystem;
  private final ClawIOTalonFX clawIO;
  private final TestType testType;
  private final Direction direction;

  private double startTime;
  private double appliedVoltage = 0;

  public ClawSysIdCommand(ClawSubsystem clawSubsystem, TestType testType, Direction direction) {
    this.clawSubsystem = clawSubsystem;
    this.clawIO = (ClawIOTalonFX) clawSubsystem.getIO();
    this.testType = testType;
    this.direction = direction;

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
        appliedVoltage = direction.value * Math.min(12, 4 * elapsedTime);
        break;
      case DYNAMIC:
        // Apply step voltage of 7V for 2 seconds
        appliedVoltage = direction.value * 7;
        break;
    }

    clawIO.setIntakeVoltage(appliedVoltage);
  }

  @Override
  public void end(boolean interrupted) {
    clawIO.setIntakeVoltage(0);
  }

  @Override
  public boolean isFinished() {
    double elapsedTime = (System.currentTimeMillis() / 1000.0) - startTime;

    return (testType == TestType.QUASISTATIC && elapsedTime > 3)
        || (testType == TestType.DYNAMIC && elapsedTime > 2);
  }

  @Override
  public String getName() {
    return String.format(
        "ClawSysId-%s-%s",
        testType.toString(), direction == Direction.FORWARD ? "Forward" : "Reverse");
  }
}

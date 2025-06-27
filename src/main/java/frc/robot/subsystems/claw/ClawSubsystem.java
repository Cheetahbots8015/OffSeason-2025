// ClawSubsystem - Subsystem to control a single TalonFX motor for a claw

package frc.robot.subsystems.claw;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ClawSubsystem extends SubsystemBase {
  public enum clawIdleState {
    in,
    out,
    stop
  }

  public enum climberIdleState {
    in,
    out,
    stop
  }

  private final ClawIO io;
  private final ClawIOInputsAutoLogged inputs = new ClawIOInputsAutoLogged();

  public ClawSubsystem(ClawIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Claw", inputs);
  }

  public void runVelocity(double clawOutput, double climberOutput) {
    io.setOpenLoop(clawOutput, climberOutput);
  }

  public void shutdown() {
    io.setOpenLoop(0.0, 0.0);
  }

  public ClawIO getIO() {
    return io;
  }

  public void setIntakeVoltage(double volts) {
    io.setIntakeVoltage(volts);
  }

  public void setShooterVoltage(double volts) {
    io.setShooterVoltage(volts);
  }
}

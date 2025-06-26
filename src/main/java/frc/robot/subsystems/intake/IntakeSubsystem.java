// IntakeSubsystem - Subsystem to control a single TalonFX motor for a Intake

package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.IntakeConstants;

import org.littletonrobotics.junction.Logger;

public class IntakeSubsystem extends SubsystemBase {
  public enum IntakeIdleState {
    down,
    homed,
    stop
  }

  private final IntakeIO io;
  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();
  private IntakeIdleState systemIdleState = IntakeIdleState.stop;

  public IntakeSubsystem(IntakeIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Intake", inputs);
  }

  public void defaultIdelVelocity() {
    if (systemIdleState == IntakeIdleState.down) {
      io.setPivotPosition(IntakeConstants.downPosition);
      io.setRollerVoltage(IntakeConstants.rollerDefaultVoltage);
      io.setIndexerVoltage(IntakeConstants.indexerDefaultVoltage);
    } else if (systemIdleState == IntakeIdleState.home) {
      runVelocity(-0.1);
    } else {
      shutdown();
    }
  }

  public void setSystemIdleState(IntakeIdleState state) {
    systemIdleState = state;
  }

  public IntakeIdleState getSystemIdleState() {
    return systemIdleState;
  }

  public void shutdown() {
    io.setRollerOpenLoop(0.0);
    io.setPivotOpenLoop(0.0);
  }
}

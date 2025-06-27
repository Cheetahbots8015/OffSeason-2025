// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class IntakeSubsystem extends SubsystemBase {
  private IntakeIO io;
  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

  public IntakeSubsystem(IntakeIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Intake", inputs);
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.setOpenLoop(0.0, 0.0, 0.0);
  }

  public void runVelocity(double indexerOutput, double intakeOutput, double armOutput) {
    io.setOpenLoop(indexerOutput, intakeOutput, armOutput);
  }
}

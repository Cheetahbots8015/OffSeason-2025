// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.generated.intake;

import com.ctre.phoenix6.configs.CANrangeConfiguration;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.IntakeIOInputsAutoLogged;

import org.littletonrobotics.junction.Logger;

public class IntakeSubsystem extends SubsystemBase {
  private IntakeIO io;
  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();

  public IntakeSubsystem(IntakeIO io) {
    this.io = io;
  }


  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Intake", inputs);
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.setOpenLoop(0.0,0.0);
  }

  public void runVelocity(double indexerOutput, double intakeOutput) {
    io.setOpenLoop(indexerOutput,intakeOutput);
  }
}

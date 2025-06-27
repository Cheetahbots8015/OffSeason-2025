// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.PivotConstants;
import org.littletonrobotics.junction.Logger;

public class PivotSubsystem extends SubsystemBase {

  // TalonFX motor controller for the roller
  private final TalonFX pivot = new TalonFX(PivotConstants.pivotID, "rio");

  // Configuration object to set up motor parameters
  private TalonFXConfiguration pivotConfigs = new TalonFXConfiguration();

  // Voltage control with FOC enabled
  private VoltageOut voltageOut = new VoltageOut(0.0).withEnableFOC(true);

  // Velocity control using FOC
  private VelocityTorqueCurrentFOC velocityFOC = new VelocityTorqueCurrentFOC(0.0);

  // Neutral mode (motor off)
  private NeutralOut neutralOut = new NeutralOut();

  private DutyCycleOut dutyCycleOut = new DutyCycleOut(0.0);

  private PivotIO io;
  private final PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();

  public PivotSubsystem(PivotIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Pivot", inputs);
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.setOpenLoop(0.0);
  }

  // Basic voltage control method; can be reused by higher-level logic
  public void setVolts(double volts) {
    pivot.setControl(voltageOut.withOutput(volts));
  }

  // Basic velocity control method using FOC; can be reused by higher-level logic
  public void setVelocity(double velocity) {
    pivot.setControl(velocityFOC.withVelocity(velocity));
  }

  // Set motor duty cycle output
  public void setDutyCycle(double duty) {
    if (Math.abs(duty) > PivotConstants.dutyCycleDeadband) {
      pivot.setControl(dutyCycleOut.withOutput(duty));
    } else {
      shutDown();
    }
  }

  public void runVelocity(double velocity) {
    io.setOpenLoop(velocity);
  }

  public void defaultIdleVelocity() {
    runVelocity(0.2);
  }
}

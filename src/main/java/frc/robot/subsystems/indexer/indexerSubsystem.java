// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.indexer;

// Import necessary CTRE Phoenix 6 classes for motor control and configuration
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.IndexerConstants;

public class indexerSubsystem extends SubsystemBase {

  // TalonFX motor controller for the roller
  private final TalonFX indexer = new TalonFX(IndexerConstants.indexerID, "rio");

  // Configuration object to set up motor parameters
  private TalonFXConfiguration indexerConfigs = new TalonFXConfiguration();

  // Voltage control with FOC enabled
  private VoltageOut voltageOut = new VoltageOut(0.0).withEnableFOC(true);

  // Velocity control using FOC
  private VelocityTorqueCurrentFOC velocityFOC = new VelocityTorqueCurrentFOC(0.0);

  // Neutral mode (motor off)
  private NeutralOut neutralOut = new NeutralOut();

  private DutyCycleOut dutyCycleOut = new DutyCycleOut(0.0);

  private indexerIO io;

  // Constructor: Configure motor settings upon subsystem creation
  public indexerSubsystem() {
    // Set the neutral mode (Coast or Brake) based on constants
    indexerConfigs.MotorOutput.withNeutralMode(
        IndexerConstants.neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    indexerConfigs.MotorOutput.withInverted(
        IndexerConstants.inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    indexerConfigs.Slot0.kP = IndexerConstants.kP;
    indexerConfigs.Slot0.kI = IndexerConstants.kI;
    indexerConfigs.Slot0.kD = IndexerConstants.kD;
    indexerConfigs.Slot0.kA = IndexerConstants.kA;
    indexerConfigs.Slot0.kS = IndexerConstants.kS;
    indexerConfigs.Slot0.kV = IndexerConstants.kV;

    // Apply the configuration to the motor
    indexer.getConfigurator().apply(indexerConfigs);
  }

  public indexerSubsystem(indexerIO io) {
    this.io = io;
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.setOpenLoop(0.0);
  }

  // Basic voltage control method; can be reused by higher-level logic
  public void setVolts(double volts) {
    indexer.setControl(voltageOut.withOutput(volts));
  }

  // Basic velocity control method using FOC; can be reused by higher-level logic
  public void setVelocity(double velocity) {
    indexer.setControl(velocityFOC.withVelocity(velocity));
  }

  // Set motor duty cycle output
  public void setDutyCycle(double duty) {
    if (Math.abs(duty) > IndexerConstants.dutyCycleDeadband) {
      indexer.setControl(dutyCycleOut.withOutput(duty));
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

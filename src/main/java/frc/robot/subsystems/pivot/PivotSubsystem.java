// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
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

  // Constructor: Configure motor settings upon subsystem creation
  public PivotSubsystem() {
    // Set the neutral mode (Coast or Brake) based on constants
    pivotConfigs.MotorOutput.withNeutralMode(
        PivotConstants.neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    pivotConfigs.MotorOutput.withInverted(
        PivotConstants.inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    pivotConfigs.Slot0.kP = PivotConstants.kP;
    pivotConfigs.Slot0.kI = PivotConstants.kI;
    pivotConfigs.Slot0.kD = PivotConstants.kD;
    pivotConfigs.Slot0.kA = PivotConstants.kA;
    pivotConfigs.Slot0.kS = PivotConstants.kS;
    pivotConfigs.Slot0.kV = PivotConstants.kV;

    // Apply the configuration to the motor
    pivot.getConfigurator().apply(pivotConfigs);
  }

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

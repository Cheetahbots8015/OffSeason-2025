// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ElevatorConstants;
import org.littletonrobotics.junction.Logger;

public class ElevatorSubsystem extends SubsystemBase {

  // TalonFX motor controller for the roller
  private final TalonFX elevator = new TalonFX(ElevatorConstants.elevatorID, "rio");

  // Configuration object to set up motor parameters
  private TalonFXConfiguration elevatorConfigs = new TalonFXConfiguration();

  // Voltage control with FOC enabled
  private VoltageOut voltageOut = new VoltageOut(0.0).withEnableFOC(true);

  // Velocity control using FOC
  private VelocityTorqueCurrentFOC velocityFOC = new VelocityTorqueCurrentFOC(0.0);

  // Neutral mode (motor off)
  private NeutralOut neutralOut = new NeutralOut();

  private DutyCycleOut dutyCycleOut = new DutyCycleOut(0.0);

  private ElevatorIO io;
  private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();

  // Constructor: Configure motor settings upon subsystem creation
  public ElevatorSubsystem() {
    // Set the neutral mode (Coast or Brake) based on constants
    elevatorConfigs.MotorOutput.withNeutralMode(
        ElevatorConstants.neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    elevatorConfigs.MotorOutput.withInverted(
        ElevatorConstants.inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Apply the configuration to the motor
    elevator.getConfigurator().apply(elevatorConfigs);
  }

  public ElevatorSubsystem(ElevatorIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Indexer", inputs);
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.setOpenLoop(0.0);
  }

  // Basic voltage control method; can be reused by higher-level logic
  public void setVolts(double volts) {
    elevator.setControl(voltageOut.withOutput(volts));
  }

  // Basic velocity control method using FOC; can be reused by higher-level logic
  public void setVelocity(double velocity) {
    elevator.setControl(velocityFOC.withVelocity(velocity));
  }

  // // Set motor duty cycle output
  // public void setDutyCycle(double duty) {
  //   if (Math.abs(duty) > elevatorConfigs.dutyCycleDeadband) {
  //     elevator.setControl(dutyCycleOut.withOutput(duty));
  //   } else {
  //     shutDown();
  //   }
  // }

  public void runVelocity(double velocity) {
    io.setOpenLoop(velocity);
  }

  public void defaultIdleVelocity() {
    runVelocity(0.2);
  }
}

// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.roller;

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
import frc.robot.RollerConstants;

public class RollerSubsystem extends SubsystemBase {

  // TalonFX motor controller for the roller
  private final TalonFX roller = new TalonFX(RollerConstants.rollerID, "rio");

  // Configuration object to set up motor parameters
  private TalonFXConfiguration rollerConfigs = new TalonFXConfiguration();

  // Voltage control with FOC enabled
  private VoltageOut voltageOut = new VoltageOut(0.0).withEnableFOC(true);

  // Velocity control using FOC
  private VelocityTorqueCurrentFOC velocityFOC = new VelocityTorqueCurrentFOC(0.0);

  // Neutral mode (motor off)
  private NeutralOut neutralOut = new NeutralOut();

  private DutyCycleOut dutyCycleOut = new DutyCycleOut(0.0);

  // Constructor: Configure motor settings upon subsystem creation
  public RollerSubsystem() {
    // Set the neutral mode (Coast or Brake) based on constants
    rollerConfigs.MotorOutput.withNeutralMode(
        RollerConstants.neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    rollerConfigs.MotorOutput.withInverted(
        RollerConstants.inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    rollerConfigs.Slot0.kP = RollerConstants.kP;
    rollerConfigs.Slot0.kI = RollerConstants.kI;
    rollerConfigs.Slot0.kD = RollerConstants.kD;
    rollerConfigs.Slot0.kA = RollerConstants.kA;
    rollerConfigs.Slot0.kS = RollerConstants.kS;
    rollerConfigs.Slot0.kV = RollerConstants.kV;

    // Apply the configuration to the motor
    roller.getConfigurator().apply(rollerConfigs);
  }

  // Stop the roller motor by setting it to neutral
  public void shutDown() {
    roller.setControl(neutralOut);
  }

  // Basic voltage control method; can be reused by higher-level logic
  public void setVolts(double volts) {
    roller.setControl(voltageOut.withOutput(volts));
  }

  // Basic velocity control method using FOC; can be reused by higher-level logic
  public void setVelocity(double velocity) {
    roller.setControl(velocityFOC.withVelocity(velocity));
  }

  // Set motor duty cycle output
  public void setDutyCycle(double duty) {
    if (Math.abs(duty) > RollerConstants.dutyCycleDeadband) {
      roller.setControl(dutyCycleOut.withOutput(duty));
    } else {
      shutDown();
    }
  }
}

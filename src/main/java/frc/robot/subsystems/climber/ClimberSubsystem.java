// ClimberSubsystem - Subsystem to control a single TalonFX motor for a climber

package frc.robot.subsystems.climber;

// Import necessary CTRE Phoenix 6 classes for motor control and configuration
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.filter.MedianFilter;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.ClimberConstants;

public class ClimberSubsystem extends SubsystemBase {
  // TalonFX motor contclimber for the climber
  private final TalonFX climber = new TalonFX(ClimberConstants.climberID, "rio");
  private double climberEncoderOffset = 0.0;
  private final MedianFilter filter = new MedianFilter(5);

  // Configuration object to set up motor parameters
  private TalonFXConfiguration climberConfigs = new TalonFXConfiguration();

  // Control modes
  private VoltageOut voltageOut =
      new VoltageOut(0.0).withEnableFOC(true); // Voltage control with FOC enabled
  private VelocityTorqueCurrentFOC velocityFOC =
      new VelocityTorqueCurrentFOC(0.0); // Velocity control using FOC
  private NeutralOut neutralOut = new NeutralOut(); // Neutral mode (motor off)
  private DutyCycleOut dutyCycleOut = new DutyCycleOut(0.0);

  // Constructor: Configure motor settings upon subsystem creation
  public ClimberSubsystem() {
    // Set the neutral mode (Coast or Brake) based on constants
    climberConfigs.MotorOutput.withNeutralMode(
        ClimberConstants.neutalmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    climberConfigs.MotorOutput.withInverted(
        ClimberConstants.inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    climberConfigs.Slot0.kP = ClimberConstants.kP;
    climberConfigs.Slot0.kI = ClimberConstants.kI;
    climberConfigs.Slot0.kD = ClimberConstants.kD;
    climberConfigs.Slot0.kA = ClimberConstants.kA;
    climberConfigs.Slot0.kS = ClimberConstants.kS;
    climberConfigs.Slot0.kV = ClimberConstants.kV;

    // Apply the configuration to the motor
    climber.getConfigurator().apply(climberConfigs);

    climberEncoderOffset = climber.getPosition().getValueAsDouble();
  }

  public double getPositionWithoutOffset() {
    return climber.getPosition().getValueAsDouble() - climberEncoderOffset;
  }

  // Stop the climber motor by setting it to neutral
  public void shutDown() {
    climber.setControl(neutralOut);
  }

  // Basic voltage control method; can be reused by higher-level logic
  public void setVolts(double volts) {
    climber.setControl(voltageOut.withOutput(volts));
  }

  // Basic velocity control method using FOC; can be reused by higher-level logic
  public void setVelocity(double velocity) {
    climber.setControl(velocityFOC.withVelocity(velocity));
  }

  public void setDutyCycle(double velocity) {
    climber.setControl(dutyCycleOut.withOutput(velocity / 5));
  }

  public void setPosition(double position) {
    if (Math.abs(getPositionWithoutOffset() - position) < ClimberConstants.positionDeadband) {
      setVolts(ClimberConstants.holdVoltage);
    } else if (this.getPositionWithoutOffset() > position + ClimberConstants.positionDeadband) {
      setVolts(filter.calculate(ClimberConstants.setPositionDownvoltage));
    } else if (this.getPositionWithoutOffset() < position - ClimberConstants.positionDeadband) {
      setVolts(filter.calculate(ClimberConstants.setPositionUpVoltage));
    }
  }
}

package frc.robot.subsystems.climber;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.ClimberConstants;

public class ClimberIOTalonFX implements ClimberIO {
  // Hardware objects
  private final TalonFX climber;
  private TalonFXConfiguration climberConfigs = new TalonFXConfiguration();
  // Voltage control requests
  private final VoltageOut voltageRequest = new VoltageOut(0);
  // Inputs from climber
  private final StatusSignal<Angle> Position;
  private final StatusSignal<AngularVelocity> Velocity;
  private final StatusSignal<Voltage> AppliedVolts;
  private final StatusSignal<Current> Current;

  public ClimberIOTalonFX() {
    climber = new TalonFX(ClimberConstants.climberID, "rio");
    climberConfigs.MotorOutput.withNeutralMode(
        ClimberConstants.neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

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

    // Create drive status signals
    Position = climber.getPosition();
    Velocity = climber.getVelocity();
    AppliedVolts = climber.getMotorVoltage();
    Current = climber.getStatorCurrent();

    BaseStatusSignal.setUpdateFrequencyForAll(50.0, Velocity, AppliedVolts, Current, Position);
    ParentDevice.optimizeBusUtilizationForAll(climber);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    BaseStatusSignal.refreshAll(Position, Velocity, AppliedVolts, Current);
    // Update climber inputs
    inputs.PositionRad = Units.rotationsToRadians(Position.getValueAsDouble());
    inputs.VelocityRadPerSec = Units.rotationsToRadians(Velocity.getValueAsDouble());
    inputs.AppliedVolts = AppliedVolts.getValueAsDouble();
    inputs.CurrentAmps = Current.getValueAsDouble();
  }

  @Override
  public void setOpenLoop(double output) {
    climber.setControl(voltageRequest.withOutput(output));
  }
}

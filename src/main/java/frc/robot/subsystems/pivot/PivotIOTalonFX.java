package frc.robot.subsystems.pivot;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
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
import frc.robot.constants.PivotConstants;
import frc.robot.subsystems.pivot.PivotIO.PivotIOInputs;

public class PivotIOTalonFX implements PivotIO {
  // Hardware objects
  private final TalonFX pivot;
  private TalonFXConfiguration pivotConfigs = new TalonFXConfiguration();
  // Voltage control requests
  private final VoltageOut voltageRequest = new VoltageOut(0);
  // Inputs from pivot
  private final StatusSignal<Angle> Position;
  private final StatusSignal<AngularVelocity> Velocity;
  private final StatusSignal<Voltage> AppliedVolts;
  private final StatusSignal<Current> Current;

  public PivotIOTalonFX() {
    pivot = new TalonFX(PivotConstants.pivotID, "rio");
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

    // Create drive status signals
    Position = pivot.getPosition();
    Velocity = pivot.getVelocity();
    AppliedVolts = pivot.getMotorVoltage();
    Current = pivot.getStatorCurrent();

    BaseStatusSignal.setUpdateFrequencyForAll(
        PivotConstants.statusUpdateFrequency, Velocity, AppliedVolts, Current, Position);
    ParentDevice.optimizeBusUtilizationForAll(pivot);
  }

  @Override
  public void updateInputs(PivotIOInputs inputs) {
    BaseStatusSignal.refreshAll(Position, Velocity, AppliedVolts, Current);
    // Update indexer inputs
    inputs.PositionRad = Units.rotationsToRadians(Position.getValueAsDouble());
    inputs.VelocityRadPerSec = Units.rotationsToRadians(Velocity.getValueAsDouble());
    inputs.AppliedVolts = AppliedVolts.getValueAsDouble();
    inputs.CurrentAmps = Current.getValueAsDouble();
  }

  @Override
  public void pivotDutyCycleOut(double output) {
    pivot.setControl(new DutyCycleOut(output));
  }
}

package frc.robot.subsystems.indexer;

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
import frc.robot.IndexerConstants;
import frc.robot.subsystems.indexer.indexerIO.indexerIOInputs;

public class indexerIOTalonFX implements indexerIO {
  // Hardware objects
  private final TalonFX indexer;
  private TalonFXConfiguration indexerConfigs = new TalonFXConfiguration();
  // Voltage control requests
  private final VoltageOut voltageRequest = new VoltageOut(0);
  // Inputs from roller
  private final StatusSignal<Angle> Position;
  private final StatusSignal<AngularVelocity> Velocity;
  private final StatusSignal<Voltage> AppliedVolts;
  private final StatusSignal<Current> Current;

  public indexerIOTalonFX() {
    indexer = new TalonFX(IndexerConstants.indexerID, "rio");
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

    // Create drive status signals
    Position = indexer.getPosition();
    Velocity = indexer.getVelocity();
    AppliedVolts = indexer.getMotorVoltage();
    Current = indexer.getStatorCurrent();

    BaseStatusSignal.setUpdateFrequencyForAll(50.0, Velocity, AppliedVolts, Current, Position);
    ParentDevice.optimizeBusUtilizationForAll(indexer);
  }

  @Override
  public void updateInputs(indexerIOInputs inputs) {
    BaseStatusSignal.refreshAll(Position, Velocity, AppliedVolts, Current);
    // Update indexer inputs
    inputs.PositionRad = Units.rotationsToRadians(Position.getValueAsDouble());
    inputs.VelocityRadPerSec = Units.rotationsToRadians(Velocity.getValueAsDouble());
    inputs.AppliedVolts = AppliedVolts.getValueAsDouble();
    inputs.CurrentAmps = Current.getValueAsDouble();
  }

  @Override
  public void setOpenLoop(double output) {
    indexer.setControl(new DutyCycleOut(output));
  }
}

package frc.robot.subsystems.intake;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.reduxrobotics.sensors.canandcolor.DigoutChannel.Index;

import edu.wpi.first.units.measure.Current;
import frc.robot.constants.IntakeConstants;

public class IntakeIOTalonFX implements IntakeIO {
  // Hardware objects
  private final TalonFX roller;
  private final TalonFX pivot;
  private final TalonFX indexer;

  private TalonFXConfiguration rollerConfigs = new TalonFXConfiguration();
  private TalonFXConfiguration pivotConfigs = new TalonFXConfiguration();
  private TalonFXConfiguration indexerConfigs = new TalonFXConfiguration();

  // Voltage control requests
  private final VoltageOut voltageRequest = new VoltageOut(0);

  // Inputs from roller
  //   private final StatusSignal<Angle> rollerPosition;
  //   private final StatusSignal<AngularVelocity> rollerVelocity;
  //   private final StatusSignal<Voltage> rollerAppliedVolts;
  //   private final StatusSignal<Current> rollerCurrent;

  //   private final StatusSignal<Angle> pivotPosition;
  //   private final StatusSignal<AngularVelocity> pivotVelocity;
  //   private final StatusSignal<Voltage> pivotAppliedVolts;
  //   private final StatusSignal<Current> pivotCurrent;

  public IntakeIOTalonFX() {
    roller = new TalonFX(IntakeConstants.rollerID, IntakeConstants.rollerCanName);
    pivot = new TalonFX(IntakeConstants.pivotID, IntakeConstants.pivotCanName);
    indexer = new TalonFX(IntakeConstants.indexerID, IntakeConstants.indexerCanName);

    rollerConfigs.MotorOutput.withNeutralMode(
        IntakeConstants.rollerNeutralmode_Coast 
          ? NeutralModeValue.Coast 
          : NeutralModeValue.Brake
    );

    rollerConfigs.MotorOutput.withInverted(
        IntakeConstants.rollerCounterClockwisePositive
          ? InvertedValue.CounterClockwise_Positive
          : InvertedValue.Clockwise_Positive
    );


    pivotConfigs.MotorOutput.withNeutralMode(
        IntakeConstants.pivotNeutralmode_Coast 
          ? NeutralModeValue.Coast 
          : NeutralModeValue.Brake
    );
    
    pivotConfigs.MotorOutput.withInverted(
        IntakeConstants.pivotCounterClockwisePositive
          ? InvertedValue.CounterClockwise_Positive
          : InvertedValue.Clockwise_Positive
    );


    indexerConfigs.MotorOutput.withNeutralMode(
        IntakeConstants.pivotNeutralmode_Coast 
          ? NeutralModeValue.Coast 
          : NeutralModeValue.Brake
    );
    
    indexerConfigs.MotorOutput.withInverted(
        IntakeConstants.pivotCounterClockwisePositive
          ? InvertedValue.CounterClockwise_Positive
          : InvertedValue.Clockwise_Positive
    );


    // Set PID and feedforward constants from constants file
    rollerConfigs.Slot0.kP = IntakeConstants.kP;
    rollerConfigs.Slot0.kI = IntakeConstants.kI;
    rollerConfigs.Slot0.kD = IntakeConstants.kD;
    rollerConfigs.Slot0.kA = IntakeConstants.kA;
    rollerConfigs.Slot0.kS = IntakeConstants.kS;
    rollerConfigs.Slot0.kV = IntakeConstants.kV;

    // Apply the configuration to the motor
    roller.getConfigurator().apply(rollerConfigs);

    pivot.getConfigurator().apply(pivotConfigs);

    indexer.getConfigurator().apply(indexerConfigs);

    // Create drive status signals
    // rollerPosition = roller.getPosition();
    // rollerVelocity = roller.getVelocity();
    // rollerAppliedVolts = roller.getMotorVoltage();
    // rollerCurrent = roller.getStatorCurrent();

    // pivotPosition = pivot.getPosition();
    // pivotVelocity = pivot.getVelocity();
    // pivotAppliedVolts = pivot.getMotorVoltage();
    // pivotCurrent = pivot.getStatorCurrent();

    // BaseStatusSignal.setUpdateFrequencyForAll(50.0,
    //                                 rollerVelocity, rollerAppliedVolts, rollerCurrent,
    // rollerPosition,
    //                                 pivotVelocity, pivotAppliedVolts, pivotCurrent,
    // pivotPosition);
    // //To be cooked
    // ParentDevice.optimizeBusUtilizationForAll(roller, pivot);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    // BaseStatusSignal.refreshAll(rollerVelocity, rollerAppliedVolts, rollerCurrent,
    // rollerPosition,
    //                             pivotVelocity, pivotAppliedVolts, pivotCurrent, pivotPosition);
    // Update roller inputs
    inputs.RollerPositionRotation = roller.getPosition().getValueAsDouble();
    inputs.RollerVelocityRPS = roller.getVelocity().getValueAsDouble();
    inputs.RollerAppliedVolts = roller.getMotorVoltage().getValueAsDouble();
    inputs.RollerCurrentAmps = roller.getSupplyCurrent().getValueAsDouble();

    inputs.PivotPositionRotation = pivot.getPosition().getValueAsDouble();
    inputs.PivotVelocityRPS = pivot.getVelocity().getValueAsDouble();
    inputs.PivotAppliedVolts = pivot.getMotorVoltage().getValueAsDouble();
    inputs.PivotCurrentAmps = pivot.getSupplyCurrent().getValueAsDouble();

    inputs.IndexerPositionRotation = pivot.getPosition().getValueAsDouble();
    inputs.IndexerVelocityRPS = pivot.getVelocity().getValueAsDouble();
    inputs.IndexerAppliedVolts = pivot.getMotorVoltage().getValueAsDouble();
    inputs.IndexerCurrentAmps = pivot.getSupplyCurrent().getValueAsDouble();
  }

  @Override
  public void setRollerVoltage(double output) {
    roller.setVoltage(output);
  }

  @Override
  public void setPivotVoltage(double output) {
    pivot.setVoltage(output);
  }

  @Override
  public void setIndexerVoltage(double output) {
    indexer.setVoltage(output);
  }

  @Override
  public void setPivotPosition(double output) {
    pivot.setPosition(output);
  }

}

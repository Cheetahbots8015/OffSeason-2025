package frc.robot.subsystems.intake;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.CANrange;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.constants.IntakeConstants;
import frc.robot.subsystems.intake.IntakeIO.IntakeIOInputs;

public class IntakeIOTalonFX implements IntakeIO {
  // Hardware objects
  private final TalonFX indexer;
  private final TalonFX intake;
  private final TalonFX arm;
  private final CANrange canrange;
  private TalonFXConfiguration indexerConfigs = new TalonFXConfiguration();
  private TalonFXConfiguration intakeConfigs = new TalonFXConfiguration();
  private TalonFXConfiguration armConfigs = new TalonFXConfiguration();

  // Inputs from indexer
  private final StatusSignal<Angle> IndexerPosition;
  private final StatusSignal<AngularVelocity> IndexerVelocity;
  private final StatusSignal<Voltage> IndexerAppliedVolts;
  private final StatusSignal<Current> IndexerCurrent;

  // Inputs from intake
  private final StatusSignal<Angle> IntakePosition;
  private final StatusSignal<AngularVelocity> IntakeVelocity;
  private final StatusSignal<Voltage> IntakeAppliedVolts;
  private final StatusSignal<Current> IntakeCurrent;

  // Inputs from arm
  private final StatusSignal<Angle> ArmPosition;
  private final StatusSignal<AngularVelocity> ArmVelocity;
  private final StatusSignal<Voltage> ArmAppliedVolts;
  private final StatusSignal<Current> ArmCurrent;

  // Inputs from canrange
  private final StatusSignal<Boolean> Canrange;

  public IntakeIOTalonFX() {
    indexer = new TalonFX(IntakeConstants.indexerID, IntakeConstants.canName);
    intake = new TalonFX(IntakeConstants.intakeID, IntakeConstants.canName);
    arm = new TalonFX(IntakeConstants.armID, IntakeConstants.canName);
    canrange = new CANrange(IntakeConstants.canRangeID, IntakeConstants.canName);
    indexerConfigs.MotorOutput.withNeutralMode(
        IntakeConstants.indexer_neutralmode_Coast
            ? NeutralModeValue.Coast
            : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    indexerConfigs.MotorOutput.withInverted(
        IntakeConstants.indexer_inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    indexerConfigs.Slot0.kP = IntakeConstants.indexer_kP;
    indexerConfigs.Slot0.kI = IntakeConstants.indexer_kI;
    indexerConfigs.Slot0.kD = IntakeConstants.indexer_kD;
    indexerConfigs.Slot0.kA = IntakeConstants.indexer_kA;
    indexerConfigs.Slot0.kS = IntakeConstants.indexer_kS;
    indexerConfigs.Slot0.kV = IntakeConstants.indexer_kV;

    intakeConfigs.MotorOutput.withNeutralMode(
        IntakeConstants.intake_neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    intakeConfigs.MotorOutput.withInverted(
        IntakeConstants.intake_inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    intakeConfigs.Slot0.kP = IntakeConstants.intake_kP;
    intakeConfigs.Slot0.kI = IntakeConstants.intake_kI;
    intakeConfigs.Slot0.kD = IntakeConstants.intake_kD;
    intakeConfigs.Slot0.kA = IntakeConstants.intake_kA;
    intakeConfigs.Slot0.kS = IntakeConstants.intake_kS;
    intakeConfigs.Slot0.kV = IntakeConstants.intake_kV;

    intakeConfigs.MotorOutput.withNeutralMode(
        IntakeConstants.intake_neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    armConfigs.MotorOutput.withInverted(
        IntakeConstants.arm_inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    armConfigs.Slot0.kP = IntakeConstants.arm_kP;
    armConfigs.Slot0.kI = IntakeConstants.arm_kI;
    armConfigs.Slot0.kD = IntakeConstants.arm_kD;
    armConfigs.Slot0.kA = IntakeConstants.arm_kA;
    armConfigs.Slot0.kS = IntakeConstants.arm_kS;
    armConfigs.Slot0.kV = IntakeConstants.arm_kV;

    // Apply the configuration to the motor
    indexer.getConfigurator().apply(indexerConfigs);
    intake.getConfigurator().apply(intakeConfigs);
    arm.getConfigurator().apply(armConfigs);

    // Create Indexer status signals
    IndexerPosition = indexer.getPosition();
    IndexerVelocity = indexer.getVelocity();
    IndexerAppliedVolts = indexer.getMotorVoltage();
    IndexerCurrent = indexer.getStatorCurrent();

    // Create Intake status signals
    IntakePosition = intake.getPosition();
    IntakeVelocity = intake.getVelocity();
    IntakeAppliedVolts = intake.getMotorVoltage();
    IntakeCurrent = intake.getStatorCurrent();

    // Create Arm status signals
    ArmPosition = arm.getPosition();
    ArmVelocity = arm.getVelocity();
    ArmAppliedVolts = arm.getMotorVoltage();
    ArmCurrent = arm.getStatorCurrent();

    // Create canrange status signals
    Canrange = canrange.getIsDetected();

    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0,
        IndexerVelocity,
        IndexerAppliedVolts,
        IndexerCurrent,
        IndexerPosition,
        IntakePosition,
        IntakeVelocity,
        IntakeAppliedVolts,
        IntakeCurrent,
        ArmPosition,
        ArmVelocity,
        ArmAppliedVolts,
        ArmCurrent,
        Canrange);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    BaseStatusSignal.refreshAll(
        IndexerVelocity,
        IndexerAppliedVolts,
        IndexerCurrent,
        IndexerPosition,
        IntakePosition,
        IntakeVelocity,
        IntakeAppliedVolts,
        IntakeCurrent,
        ArmPosition,
        ArmVelocity,
        ArmAppliedVolts,
        ArmCurrent,
        Canrange);
    // Update indexer inputs
    inputs.IndexerPositionRad = Units.rotationsToRadians(IndexerPosition.getValueAsDouble());
    inputs.IndexerVelocityRadPerSec = Units.rotationsToRadians(IndexerVelocity.getValueAsDouble());
    inputs.IndexerAppliedVolts = IndexerAppliedVolts.getValueAsDouble();
    inputs.IndexerCurrentAmps = IndexerCurrent.getValueAsDouble();
    // Update intake inputs
    inputs.IntakePositionRad = Units.rotationsToRadians(IntakePosition.getValueAsDouble());
    inputs.IntakeVelocityRadPerSec = Units.rotationsToRadians(IntakeVelocity.getValueAsDouble());
    inputs.IntakeAppliedVolts = IntakeAppliedVolts.getValueAsDouble();
    inputs.IntakeCurrentAmps = IntakeCurrent.getValueAsDouble();
    // Update arm inputs
    inputs.ArmPositionRad = Units.rotationsToRadians(ArmPosition.getValueAsDouble());
    inputs.ArmVelocityRadPerSec = Units.rotationsToRadians(ArmVelocity.getValueAsDouble());
    inputs.ArmAppliedVolts = ArmAppliedVolts.getValueAsDouble();
    inputs.ArmCurrentAmps = ArmCurrent.getValueAsDouble();
    // Update canrange inputs
    inputs.Canrange = Canrange.getValue();
  }

  @Override
  public void setOpenLoop(double indexerOutput, double intakeOutput, double armOutput) {
    indexer.setControl(new DutyCycleOut(indexerOutput));
    intake.setControl(new DutyCycleOut(intakeOutput));
    arm.setControl(new DutyCycleOut(armOutput));
  }

  @Override
  public void setArmVoltage(double volts) {
    arm.setVoltage(volts);
  }

  @Override
  public void setIntakeVoltage(double volts) {
    intake.setVoltage(volts);
  }

  @Override
  public void setIndexerVoltage(double volts) {
    indexer.setVoltage(volts);
  }

  @Override
  public boolean getCanRange() {
    return Canrange.getValue();
  }
}

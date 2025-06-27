package frc.robot.subsystems.climber;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
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
  private final TalonFX claw;
  private final TalonFX pivot;
  private TalonFXConfiguration clawConfigs = new TalonFXConfiguration();
  private TalonFXConfiguration pivotConfigs = new TalonFXConfiguration();
  // Voltage control requests

  // Inputs from claw
  private final StatusSignal<Angle> ClawPosition;
  private final StatusSignal<AngularVelocity> ClawVelocity;
  private final StatusSignal<Voltage> ClawAppliedVolts;
  private final StatusSignal<Current> ClawCurrent;
  private final StatusSignal<Angle> PivotPosition;
  private final StatusSignal<AngularVelocity> PivotVelocity;
  private final StatusSignal<Voltage> PivotAppliedVolts;
  private final StatusSignal<Current> PivotCurrent;

  public ClimberIOTalonFX() {
    claw = new TalonFX(ClimberConstants.clawID, "rio");
    pivot = new TalonFX(ClimberConstants.pivotID, "rio");
    clawConfigs.MotorOutput.withNeutralMode(
        ClimberConstants.claw_neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    clawConfigs.MotorOutput.withInverted(
        ClimberConstants.pivot_inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    pivotConfigs.MotorOutput.withNeutralMode(
        ClimberConstants.pivot_neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    pivotConfigs.MotorOutput.withInverted(
        ClimberConstants.pivot_inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);
    // Set PID and feedforward constants from constants file
    clawConfigs.Slot0.kP = ClimberConstants.clawkP;
    clawConfigs.Slot0.kI = ClimberConstants.clawkI;
    clawConfigs.Slot0.kD = ClimberConstants.clawkD;
    clawConfigs.Slot0.kA = ClimberConstants.clawkA;
    clawConfigs.Slot0.kS = ClimberConstants.clawkS;
    clawConfigs.Slot0.kV = ClimberConstants.clawkV;

    pivotConfigs.Slot0.kP = ClimberConstants.pivotkP;
    pivotConfigs.Slot0.kI = ClimberConstants.pivotkI;
    pivotConfigs.Slot0.kD = ClimberConstants.pivotkD;
    pivotConfigs.Slot0.kA = ClimberConstants.pivotkA;
    pivotConfigs.Slot0.kS = ClimberConstants.pivotkS;
    pivotConfigs.Slot0.kV = ClimberConstants.pivotkV;

    // Apply the configuration to the motor
    claw.getConfigurator().apply(clawConfigs);
    pivot.getConfigurator().apply(pivotConfigs);

    // Create drive status signals
    ClawPosition = claw.getPosition();
    ClawVelocity = claw.getVelocity();
    ClawAppliedVolts = claw.getMotorVoltage();
    ClawCurrent = claw.getStatorCurrent();
    PivotPosition = pivot.getPosition();
    PivotVelocity = pivot.getVelocity();
    PivotAppliedVolts = pivot.getMotorVoltage();
    PivotCurrent = pivot.getStatorCurrent();

    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0,
        ClawPosition,
        ClawVelocity,
        ClawAppliedVolts,
        ClawCurrent,
        PivotPosition,
        PivotVelocity,
        PivotAppliedVolts,
        PivotCurrent);
    ParentDevice.optimizeBusUtilizationForAll(claw, pivot);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {
    BaseStatusSignal.refreshAll(
        ClawPosition,
        ClawVelocity,
        ClawAppliedVolts,
        ClawCurrent,
        PivotPosition,
        PivotVelocity,
        PivotAppliedVolts,
        PivotCurrent);
    // Update claw inputs
    inputs.ClawPositionRad = Units.rotationsToRadians(ClawPosition.getValueAsDouble());
    inputs.ClawVelocityRadPerSec = Units.rotationsToRadians(ClawVelocity.getValueAsDouble());
    inputs.ClawAppliedVolts = ClawAppliedVolts.getValueAsDouble();
    inputs.ClawCurrentAmps = ClawCurrent.getValueAsDouble();

    inputs.PivotPositionRad = Units.rotationsToRadians(PivotPosition.getValueAsDouble());
    inputs.PivotVelocityRadPerSec = Units.rotationsToRadians(PivotVelocity.getValueAsDouble());
    inputs.PivotAppliedVolts = PivotAppliedVolts.getValueAsDouble();
    inputs.PivotCurrentAmps = PivotCurrent.getValueAsDouble();
  }

  @Override
  public void setOpenLoop(double clawOutput, double pivotOutput) {
    claw.setControl(new DutyCycleOut(clawOutput));
    pivot.setControl(new DutyCycleOut(pivotOutput));
  }

  @Override
  public void setClawVoltage(double volts){
    claw.setVoltage(volts);
  }

  @Override
  public void setPivotVoltage(double volts){
    pivot.setVoltage(volts);
  }
}

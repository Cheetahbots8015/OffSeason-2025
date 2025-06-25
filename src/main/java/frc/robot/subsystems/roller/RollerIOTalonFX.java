package frc.robot.subsystems.roller;

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
import frc.robot.RollerConstants;

public class RollerIOTalonFX implements RollerIO {
  // Hardware objects
  private final TalonFX roller;
  private final TalonFX climber;
  private TalonFXConfiguration rollerConfigs = new TalonFXConfiguration();
  private TalonFXConfiguration climberConfigs = new TalonFXConfiguration();
  // Voltage control requests
  private final VoltageOut voltageRequest = new VoltageOut(0);
  // Inputs from roller
  private final StatusSignal<Angle> RollerPosition;
  private final StatusSignal<AngularVelocity> RollerVelocity;
  private final StatusSignal<Voltage> RollerAppliedVolts;
  private final StatusSignal<Current> RollerCurrent;
  private final StatusSignal<Angle> ClimberPosition;
  private final StatusSignal<AngularVelocity> ClimberVelocity;
  private final StatusSignal<Voltage> ClimberAppliedVolts;
  private final StatusSignal<Current> ClimberCurrent;

  public RollerIOTalonFX() {
    roller = new TalonFX(RollerConstants.rollerID, "rio");
    climber = new TalonFX(RollerConstants.climberID, "rio");
    rollerConfigs.MotorOutput.withNeutralMode(
        RollerConstants.neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    rollerConfigs.MotorOutput.withInverted(
        RollerConstants.inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    rollerConfigs.Slot0.kP = RollerConstants.rollerkP;
    rollerConfigs.Slot0.kI = RollerConstants.rollerkI;
    rollerConfigs.Slot0.kD = RollerConstants.rollerkD;
    rollerConfigs.Slot0.kA = RollerConstants.rollerkA;
    rollerConfigs.Slot0.kS = RollerConstants.rollerkS;
    rollerConfigs.Slot0.kV = RollerConstants.rollerkV;

    climberConfigs.Slot0.kP = RollerConstants.climberkP;
    climberConfigs.Slot0.kI = RollerConstants.climberkI;
    climberConfigs.Slot0.kD = RollerConstants.climberkD;
    climberConfigs.Slot0.kA = RollerConstants.climberkA;
    climberConfigs.Slot0.kS = RollerConstants.climberkS;
    climberConfigs.Slot0.kV = RollerConstants.climberkV;

    // Apply the configuration to the motor
    roller.getConfigurator().apply(rollerConfigs);
    climber.getConfigurator().apply(climberConfigs);

    // Create drive status signals
    RollerPosition = roller.getPosition();
    RollerVelocity = roller.getVelocity();
    RollerAppliedVolts = roller.getMotorVoltage();
    RollerCurrent = roller.getStatorCurrent();
    ClimberPosition = climber.getPosition();
    ClimberVelocity = climber.getVelocity();
    ClimberAppliedVolts = climber.getMotorVoltage();
    ClimberCurrent = climber.getStatorCurrent();

    BaseStatusSignal.setUpdateFrequencyForAll(
        50.0,
        RollerPosition,
        RollerVelocity,
        RollerAppliedVolts,
        RollerCurrent,
        ClimberPosition,
        ClimberVelocity,
        ClimberAppliedVolts,
        ClimberCurrent);
    ParentDevice.optimizeBusUtilizationForAll(roller, climber);
  }

  @Override
  public void updateInputs(RollerIOInputs inputs) {
    BaseStatusSignal.refreshAll(
        RollerPosition,
        RollerVelocity,
        RollerAppliedVolts,
        RollerCurrent,
        ClimberPosition,
        ClimberVelocity,
        ClimberAppliedVolts,
        ClimberCurrent);
    // Update roller inputs
    inputs.RollerPositionRad = Units.rotationsToRadians(RollerPosition.getValueAsDouble());
    inputs.RollerVelocityRadPerSec = Units.rotationsToRadians(RollerVelocity.getValueAsDouble());
    inputs.RollerAppliedVolts = RollerAppliedVolts.getValueAsDouble();
    inputs.RollerCurrentAmps = RollerCurrent.getValueAsDouble();

    inputs.ClimberPositionRad = Units.rotationsToRadians(ClimberPosition.getValueAsDouble());
    inputs.ClimberVelocityRadPerSec = Units.rotationsToRadians(ClimberVelocity.getValueAsDouble());
    inputs.ClimberAppliedVolts = ClimberAppliedVolts.getValueAsDouble();
    inputs.ClimberCurrentAmps = ClimberCurrent.getValueAsDouble();
  }

  @Override
  public void setOpenLoop(double output, double climberOutput) {

    roller.setControl(new DutyCycleOut(output));
  }
}

package frc.robot.subsystems.elevator;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.units.measure.Acceleration;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularAcceleration;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.constants.ElevatorConstants;
import frc.robot.subsystems.elevator.ElevatorIO.ElevatorIOInputs;

public class ElevatorIOTalonFX implements ElevatorIO {
  // Hardware objects
  private final TalonFX elevator;
  private TalonFXConfiguration elevatorConfigs = new TalonFXConfiguration();

  // class member variable
  final VelocityVoltage m_velocity = new VelocityVoltage(0);

  // Inputs from roller
  private final StatusSignal<Angle> Position;
  private final StatusSignal<AngularVelocity> Velocity;
  private final StatusSignal<Voltage> AppliedVolts;
  private final StatusSignal<Current> Current;
  private final StatusSignal<AngularAcceleration> Acceleration;

  public ElevatorIOTalonFX() {
    elevator = new TalonFX(ElevatorConstants.elevatorID, "canivore");
    elevatorConfigs.MotorOutput.withNeutralMode(
        ElevatorConstants.neutralmode_Coast ? NeutralModeValue.Coast : NeutralModeValue.Brake);

    // Set motor inversion based on desired rotation direction
    elevatorConfigs.MotorOutput.withInverted(
        ElevatorConstants.inverted_CounterClockwisePositive
            ? InvertedValue.CounterClockwise_Positive
            : InvertedValue.Clockwise_Positive);

    // Set PID and feedforward constants from constants file
    elevatorConfigs.Slot0.kP = ElevatorConstants.kP;
    elevatorConfigs.Slot0.kI = ElevatorConstants.kI;
    elevatorConfigs.Slot0.kD = ElevatorConstants.kD;
    elevatorConfigs.Slot0.kG = ElevatorConstants.kG;
    elevatorConfigs.Slot0.GravityType = GravityTypeValue.Elevator_Static;

    // Apply the configuration to the motor
    elevator.getConfigurator().apply(elevatorConfigs);

    // Create drive status signals
    Position = elevator.getPosition();
    Velocity = elevator.getVelocity();
    AppliedVolts = elevator.getMotorVoltage();
    Current = elevator.getStatorCurrent();
    Acceleration = elevator.getAcceleration();

    BaseStatusSignal.setUpdateFrequencyForAll(
        ElevatorConstants.statusUpdateFrequency,
        Velocity,
        AppliedVolts,
        Current,
        Position,
        Acceleration);
    ParentDevice.optimizeBusUtilizationForAll(elevator);
  }

  @Override
  public void updateInputs(ElevatorIOInputs inputs) {
    BaseStatusSignal.refreshAll(Position, Velocity, AppliedVolts, Current, Acceleration);
    // Update elevator inputs
    inputs.PositionRad = Units.rotationsToRadians(Position.getValueAsDouble());
    inputs.VelocityRadPerSec = Units.rotationsToRadians(Velocity.getValueAsDouble());
    inputs.AppliedVolts = AppliedVolts.getValueAsDouble();
    inputs.CurrentAmps = Current.getValueAsDouble();
    inputs.AccelerationRad = Units.rotationsToRadians(Acceleration.getValueAsDouble());
  }

  @Override
  public void elevatorDutyCycleOut(double output) {
    elevator.setControl(new DutyCycleOut(output));
  }

  @Override
  public void setElevatorVoltage(double volts) {
    elevator.setVoltage(volts);
  }

  @Override
  public double getElevatorVelocity() {
    BaseStatusSignal.refreshAll(Position, Velocity, AppliedVolts, Current, Acceleration);
    return Units.rotationsToRadians(Velocity.getValueAsDouble());
  }

  @Override
  public void VelocityVoltage() {
    elevator.setControl(m_velocity.withVelocity(0));
  }
}

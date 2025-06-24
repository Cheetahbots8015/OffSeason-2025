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
  private TalonFXConfiguration rollerConfigs = new TalonFXConfiguration();
  // Voltage control requests
  private final VoltageOut voltageRequest = new VoltageOut(0);
  // Inputs from roller
  private final StatusSignal<Angle> Position;
  private final StatusSignal<AngularVelocity> Velocity;
  private final StatusSignal<Voltage> AppliedVolts;
  private final StatusSignal<Current> Current;

  public RollerIOTalonFX() {
    roller = new TalonFX(RollerConstants.rollerID, "rio");
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

    // Create drive status signals
    Position = roller.getPosition();
    Velocity = roller.getVelocity();
    AppliedVolts = roller.getMotorVoltage();
    Current = roller.getStatorCurrent();

    BaseStatusSignal.setUpdateFrequencyForAll(50.0, Velocity, AppliedVolts, Current, Position);
    ParentDevice.optimizeBusUtilizationForAll(roller);
  }

  @Override
  public void updateInputs(RollerIOInputs inputs) {
    BaseStatusSignal.refreshAll(Position, Velocity, AppliedVolts, Current);
    // Update roller inputs
    inputs.PositionRad = Units.rotationsToRadians(Position.getValueAsDouble());
    inputs.VelocityRadPerSec = Units.rotationsToRadians(Velocity.getValueAsDouble());
    inputs.AppliedVolts = AppliedVolts.getValueAsDouble();
    inputs.CurrentAmps = Current.getValueAsDouble();
  }

  @Override
  public void setOpenLoop(double output) {

    roller.setControl(new DutyCycleOut(output));
  }
}

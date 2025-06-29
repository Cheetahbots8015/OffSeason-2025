package frc.robot.subsystems.elevator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class ElevatorIOSim implements ElevatorIO {
  private static final DCMotor GEARBOX = DCMotor.getKrakenX60Foc(1);
  private final DCMotorSim elevatorIOSim;
  private double AppliedVolts = 0.0;

  public ElevatorIOSim() {
    elevatorIOSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
  }

  @Override
  public void updateInputs(ElevatorIOInputs inputs) {

    // Update simulation state
    elevatorIOSim.setInputVoltage(MathUtil.clamp(AppliedVolts, -12.0, 12.0));
    elevatorIOSim.update(0.02);

    // Update roller inputs
    inputs.PositionRad = elevatorIOSim.getAngularPositionRad();
    inputs.VelocityRadPerSec = elevatorIOSim.getAngularVelocityRadPerSec();
    inputs.AppliedVolts = AppliedVolts;
    inputs.CurrentAmps = Math.abs(elevatorIOSim.getCurrentDrawAmps());
    inputs.AccelerationRad = elevatorIOSim.getAngularAccelerationRadPerSecSq();
  }

  @Override
  public void elevatorDutyCycleOut(double output) {
    AppliedVolts = output * 12.0;
  }

  @Override
  public void setElevatorVoltage(double volts) {
    AppliedVolts = volts;
  }

  @Override
  public double getElevatorVelocity() {
    return elevatorIOSim.getAngularVelocityRadPerSec();
  }
}

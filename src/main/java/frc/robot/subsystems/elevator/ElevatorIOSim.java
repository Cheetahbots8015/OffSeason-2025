package frc.robot.subsystems.elevator;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class ElevatorIOSim implements ElevatorIO {
  private static final DCMotor GEARBOX = DCMotor.getKrakenX60Foc(1);
  private final DCMotorSim rollerSim;
  private double AppliedVolts = 0.0;

  public ElevatorIOSim() {
    rollerSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
  }

  @Override
  public void updateInputs(ElevatorIOInputs inputs) {

    // Update simulation state
    rollerSim.setInputVoltage(MathUtil.clamp(AppliedVolts, -12.0, 12.0));
    rollerSim.update(0.02);

    // Update roller inputs
    inputs.PositionRad = rollerSim.getAngularPositionRad();
    inputs.VelocityRadPerSec = rollerSim.getAngularVelocityRadPerSec();
    inputs.AppliedVolts = AppliedVolts;
    inputs.CurrentAmps = Math.abs(rollerSim.getCurrentDrawAmps());
  }

  @Override
  public void setOpenLoop(double output) {
    AppliedVolts = output * 12.0;
  }
}

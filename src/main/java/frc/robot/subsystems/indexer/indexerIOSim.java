package frc.robot.subsystems.indexer;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class indexerIOSim implements indexerIO {
  private static final DCMotor GEARBOX = DCMotor.getKrakenX60Foc(1);
  private final DCMotorSim indexerSim;
  private double AppliedVolts = 0.0;

  public indexerIOSim() {
    indexerSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
  }

  @Override
  public void updateInputs(indexerIOInputs inputs) {

    // Update simulation state
    indexerSim.setInputVoltage(MathUtil.clamp(AppliedVolts, -12.0, 12.0));
    indexerSim.update(0.02);

    // Update roller inputs
    inputs.PositionRad = indexerSim.getAngularPositionRad();
    inputs.VelocityRadPerSec = indexerSim.getAngularVelocityRadPerSec();
    inputs.AppliedVolts = AppliedVolts;
    inputs.CurrentAmps = Math.abs(indexerSim.getCurrentDrawAmps());
  }

  @Override
  public void setOpenLoop(double output) {
    AppliedVolts = output * 12.0;
  }
}

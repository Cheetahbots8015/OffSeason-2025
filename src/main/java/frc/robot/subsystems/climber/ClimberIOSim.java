package frc.robot.subsystems.climber;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class ClimberIOSim implements ClimberIO {
  private static final DCMotor GEARBOX = DCMotor.getKrakenX60Foc(1);
  private final DCMotorSim climberSim;
  private double AppliedVolts = 0.0;

  public ClimberIOSim() {
    climberSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
  }

  @Override
  public void updateInputs(ClimberIOInputs inputs) {

    // Update simulation state
    climberSim.setInputVoltage(MathUtil.clamp(AppliedVolts, -12.0, 12.0));
    climberSim.update(0.02);

    // Update climber inputs
    inputs.PositionRad = climberSim.getAngularPositionRad();
    inputs.VelocityRadPerSec = climberSim.getAngularVelocityRadPerSec();
    inputs.AppliedVolts = AppliedVolts;
    inputs.CurrentAmps = Math.abs(climberSim.getCurrentDrawAmps());
  }

  @Override
  public void setOpenLoop(double output) {
    AppliedVolts = output * 12.0;
  }
}

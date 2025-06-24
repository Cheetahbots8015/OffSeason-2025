package frc.robot.subsystems.roller;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class RollerIOSim implements RollerIO {
  private static final DCMotor GEARBOX = DCMotor.getKrakenX60Foc(1);
  private final DCMotorSim rollerSim;
  private final DCMotorSim climberSim;
  private double RollerAppliedVolts = 0.0;
  private double ClimberAppliedVolts = 0.0;

  public RollerIOSim() {
    rollerSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
    climberSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
  }

  @Override
  public void updateInputs(RollerIOInputs inputs) {

    // Update simulation state
    rollerSim.setInputVoltage(MathUtil.clamp(RollerAppliedVolts, -12.0, 12.0));
    rollerSim.update(0.02);
    climberSim.setInputVoltage(MathUtil.clamp(ClimberAppliedVolts, -12.0, 12.0));
    climberSim.update(0.02);

    // Update roller inputs
    inputs.RollerPositionRad = rollerSim.getAngularPositionRad();
    inputs.RollerVelocityRadPerSec = rollerSim.getAngularVelocityRadPerSec();
    inputs.RollerAppliedVolts = RollerAppliedVolts;
    inputs.RollerCurrentAmps = Math.abs(rollerSim.getCurrentDrawAmps());
    inputs.ClimberPositionRad = climberSim.getAngularPositionRad();
    inputs.ClimberVelocityRadPerSec = climberSim.getAngularVelocityRadPerSec();
    inputs.ClimberAppliedVolts = ClimberAppliedVolts;
    inputs.ClimberCurrentAmps = Math.abs(climberSim.getCurrentDrawAmps());
  }

  @Override
  public void setOpenLoop(double rollerOutput, double climberOutput) {
    RollerAppliedVolts = rollerOutput * 12.0;
    ClimberAppliedVolts = climberOutput * 12.0;
  }
}

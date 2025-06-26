package frc.robot.subsystems.intake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class IntakeIOSim implements IntakeIO {
  private static final DCMotor GEARBOX = DCMotor.getKrakenX60Foc(3);
  private final DCMotorSim rollerSim;
  private final DCMotorSim pivotSim;
  private final DCMotorSim indexSim;
  private double RollerVolts = 0.0;
  private double PivotVolts = 0.0;
  private double IndexerVolts = 0.0;

  public IntakeIOSim() {
    rollerSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
    pivotSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
    indexSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {

    // Update simulation state
    rollerSim.setInputVoltage(MathUtil.clamp(RollerVolts, -12.0, 12.0));
    rollerSim.update(0.02);

    pivotSim.setInputVoltage(MathUtil.clamp(PivotVolts, -12.0, 12.0));
    pivotSim.update(0.02);

    // Update roller inputs
    inputs.RollerPositionRotation = rollerSim.getAngularPositionRotations();
    inputs.RollerVelocityRPS = rollerSim.getAngularVelocityRPM() / 60.0;
    inputs.RollerAppliedVolts = RollerVolts;
    inputs.RollerCurrentAmps = Math.abs(rollerSim.getCurrentDrawAmps());

    inputs.PivotPositionRotation = pivotSim.getAngularPositionRotations();
    inputs.PivotVelocityRPS = pivotSim.getAngularVelocityRPM() / 60.0;
    inputs.PivotAppliedVolts = PivotVolts;
    inputs.PivotCurrentAmps = Math.abs(pivotSim.getCurrentDrawAmps());
  }

  @Override
  public void setRollerVoltage(double output) {
    RollerVolts = output;
  }

  @Override
  public void setPivotVoltage(double output) {
    PivotVolts = output;
  }

  @Override
  public void setIndexerVoltage(double output) {
    IndexerVolts = output;
  }
}

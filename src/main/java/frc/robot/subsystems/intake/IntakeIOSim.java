package frc.robot.subsystems.intake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.DCMotorSim;

public class IntakeIOSim implements IntakeIO {
  private static final DCMotor GEARBOX = DCMotor.getKrakenX60Foc(1);
  private final DCMotorSim indexerIOSim;
  private final DCMotorSim intakeIOSim;
  private double IndexerAppliedVolts = 0.0;
  private double IntakeAppliedVolts = 0.0;

  public IntakeIOSim() {
    indexerIOSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
    intakeIOSim = new DCMotorSim(LinearSystemId.createDCMotorSystem(GEARBOX, 0.001, 1), GEARBOX);
  }

  @Override
  public void updateInputs(IntakeIOInputs inputs) {

    // Update simulation state
    indexerIOSim.setInputVoltage(MathUtil.clamp(IndexerAppliedVolts, -12.0, 12.0));
    indexerIOSim.update(0.02);
    intakeIOSim.setInputVoltage(MathUtil.clamp(IntakeAppliedVolts, -12, 12));
    intakeIOSim.update(0.02);

    // Update indexer inputs
    inputs.IndexerPositionRad = indexerIOSim.getAngularPositionRad();
    inputs.IndexerVelocityRadPerSec = indexerIOSim.getAngularVelocityRadPerSec();
    inputs.IndexerAppliedVolts = IndexerAppliedVolts;
    inputs.IndexerCurrentAmps = Math.abs(indexerIOSim.getCurrentDrawAmps());

    // Update intake inputs
    inputs.IntakePositionRad = intakeIOSim.getAngularPositionRad();
    inputs.IntakeVelocityRadPerSec = intakeIOSim.getAngularVelocityRadPerSec();
    inputs.IntakeAppliedVolts = IntakeAppliedVolts;
    inputs.IntakeCurrentAmps = Math.abs(intakeIOSim.getCurrentDrawAmps());
  }

  @Override
  public void setOpenLoop(double indexerOutput, double intakeOutput) {
    IndexerAppliedVolts = indexerOutput * 12.0;
    IntakeAppliedVolts = intakeOutput * 12.0;
  }
}

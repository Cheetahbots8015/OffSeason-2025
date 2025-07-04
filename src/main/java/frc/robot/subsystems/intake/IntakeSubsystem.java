// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.intake;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.constants.IntakeConstants;
import frc.robot.subsystems.intake.IntakeIO.IntakeIOInputs;
import org.littletonrobotics.junction.Logger;

public class IntakeSubsystem extends SubsystemBase {
  private IntakeIO io;
  private final IntakeIOInputsAutoLogged inputs = new IntakeIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  public IntakeSubsystem(IntakeIO io) {
    this.io = io;
    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Intake/Arm/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> setArmVoltage(voltage.in(Volt)), null, this));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Intake", inputs);
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.setOpenLoop(0.0, 0.0, 0.0);
  }

  public void runVelocity(double indexerOutput, double intakeOutput, double armOutput) {
    io.setOpenLoop(indexerOutput, intakeOutput, armOutput);
  }

  public void setArmVoltage(double volts) {
    io.setArmVoltage(volts);
  }

  public void setIntakeVoltage(double volts) {
    io.setIntakeVoltage(volts);
  }

  public void setIndexerVoltage(double volts) {
    io.setIndexerVoltage(volts);
  }

  public boolean getCanRange() {
    return io.getCanRange();
  }

  public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return sysId.quasistatic(direction);
  }

  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return sysId.dynamic(direction);
  }

  public IntakeIOInputs getInput() {
    return inputs;
  }

  public void armDown() {
    io.setArmPosition(IntakeConstants.armDownPosition);
  }

  public void armHome() {
    io.setArmPosition(IntakeConstants.armHomePosition);
  }

  public void rollerIntake() {
    io.setIntakeVoltage(IntakeConstants.intakingVoltage);
    io.setIndexerVoltage(IntakeConstants.indexerVoltage);
  }

  public void rollerVomit() {
    io.setIntakeVoltage(-IntakeConstants.intakingVoltage);
    io.setIndexerVoltage(-IntakeConstants.indexerVoltage);
  }

  public void rollerStop() {
    io.setIntakeVoltage(0.0);
    io.setIndexerVoltage(0.0);
  }
}

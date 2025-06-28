// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.pivot;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.Logger;

public class PivotSubsystem extends SubsystemBase {

  private PivotIO io;
  private final PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  public PivotSubsystem(PivotIO io) {
    this.io = io;
    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Pivot/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> setPivotVoltage(voltage.in(Volt)), null, this));
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Pivot", inputs);
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.pivotDutyCycleOut(0.0);
  }

  public void runPercentOuput(double percentOutput) {
    io.pivotDutyCycleOut(percentOutput);
  }

  public void defaultIdleVelocity() {
    runPercentOuput(0.2);
  }

  public PivotIO getIO() {
    return io;
  }

  public void setPivotVoltage(double volts) {
    io.setPivotVoltage(volts);
  }

  public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return sysId.quasistatic(direction);
  }

  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return sysId.dynamic(direction);
  }
}

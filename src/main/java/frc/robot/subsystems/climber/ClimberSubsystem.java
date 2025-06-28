// ClimberSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.climber;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();
  private final SysIdRoutine clawsysId;
  private final SysIdRoutine pivotsysId;

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
    clawsysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Climber/Claw/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> setClawVoltage(voltage.in(Volt)), null, this));

    pivotsysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Climber/Pivot/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> setPivotVoltage(voltage.in(Volt)), null, this));
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Climber", inputs);
  }

  public void runVelocity(double rollerOutput, double climberOutput) {
    io.setOpenLoop(rollerOutput, climberOutput);
  }

  public void shutdown() {
    io.setOpenLoop(0.0, 0.0);
  }

  public void setClawVoltage(double volts) {
    io.setClawVoltage(volts);
  }

  public void setPivotVoltage(double volts) {
    io.setPivotVoltage(volts);
  }
}

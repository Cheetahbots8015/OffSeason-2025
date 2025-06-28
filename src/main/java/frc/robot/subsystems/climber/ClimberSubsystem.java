// ClimberSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.climber;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.ClimberConstants;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();
  private final SysIdRoutine clawsysId;
  private final SysIdRoutine pivotsysId;
  public double pivotUpDutyCycleOutValue = ClimberConstants.pivotUpDutyCycleOutValue;
  public double pivotDownDutyCycleOutValue = ClimberConstants.pivotDownDutyCycleOutValue;
  public double clawDutyCycleOutValue = ClimberConstants.clawDutyCycleOutValue;

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
    SmartDashboard.putNumber("Climber's roller Up Duty Cycle Out Value", pivotUpDutyCycleOutValue);
    SmartDashboard.putNumber(
        "Climber's roller Down Duty Cycle Out Value", pivotDownDutyCycleOutValue);
    SmartDashboard.putNumber("Climber Duty Cycle Out Value", clawDutyCycleOutValue);

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
    pivotUpDutyCycleOutValue =
        SmartDashboard.getNumber(
            "Climber's roller Up Duty Cycle Out Value", pivotUpDutyCycleOutValue);
    pivotDownDutyCycleOutValue =
        SmartDashboard.getNumber(
            "Climber's roller Down Duty Cycle Out Value", pivotDownDutyCycleOutValue);
    clawDutyCycleOutValue =
        SmartDashboard.getNumber("Climber Duty Cycle Out Value", clawDutyCycleOutValue);
  }

  public void runDutyCyleOutValue(double rollerOutput, double climberOutput) {
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

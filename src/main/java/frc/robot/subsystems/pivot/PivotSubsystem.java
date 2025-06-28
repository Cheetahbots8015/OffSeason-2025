package frc.robot.subsystems.pivot;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.constants.PivotConstants;
import org.littletonrobotics.junction.Logger;

public class PivotSubsystem extends SubsystemBase {

  private final PivotIO io;
  private final PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  private double ClockWisedutyCycleOutValue = PivotConstants.ClockWiseValue;
  private double AntiClockWisedutyCycleOutValue = PivotConstants.AntiClockWiseValue;

  public PivotSubsystem(PivotIO io) {
    this.io = io;
    SmartDashboard.putNumber("ClockWise dutyCycleOut Value", ClockWisedutyCycleOutValue);
    SmartDashboard.putNumber("AntiClockWise dutyCycleOut Value", AntiClockWisedutyCycleOutValue);
    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Pivot/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism((voltage) -> setPivotVoltage(voltage.in(Volt)), null, this));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Pivot", inputs);
    ClockWisedutyCycleOutValue =
        SmartDashboard.getNumber("ClockWise dutyCycleOut Value", ClockWisedutyCycleOutValue);
    AntiClockWisedutyCycleOutValue =
        SmartDashboard.getNumber(
            "AntiClockWise dutyCycleOut Value", AntiClockWisedutyCycleOutValue);
  }

  // Stop the indexer motor by setting it to neutral
  public void shutDown() {
    io.pivotDutyCycleOut(0.0);
  }

  public void runDutyCycleOuput(double percentOutput) {
    io.pivotDutyCycleOut(percentOutput);
  }

  public double getClockWiseDutyCycleOutValue() {
    return ClockWisedutyCycleOutValue;
  }

  public double getAntiClockWiseDutyCycleOutValue() {
    return AntiClockWisedutyCycleOutValue;
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

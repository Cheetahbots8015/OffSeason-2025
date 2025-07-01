package frc.robot.subsystems.pivot;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.constants.PivotConstants;
import frc.robot.subsystems.pivot.PivotIO.PivotIOInputs;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs;
import org.littletonrobotics.junction.Logger;

public class PivotSubsystem extends SubsystemBase {

  private final PivotIO io;
  private final PivotIOInputsAutoLogged inputs = new PivotIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  private StateManagerIOInputs stateIO;

  private double ClockWisedutyCycleOutValue = PivotConstants.ClockWiseValue;
  private double AntiClockWisedutyCycleOutValue = PivotConstants.AntiClockWiseValue;

  public PivotSubsystem(PivotIO io, StateManagerIOInputs stateIO) {
    this.stateIO = stateIO;
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
    if (stateIO.isUpdating) {
      switch (stateIO.targetCoralState) {
        case IDLE:
          io.setPosition(PivotConstants.homedPosition);
          break;

        case ElevatorUp:
          io.setPosition(PivotConstants.homedPosition);
          break;

        case ReadyToIntake:
          io.setPosition(PivotConstants.intakePosition);
          break;

        case ClawIntaking:
          io.setPosition(PivotConstants.intakePosition);
          break;

        case FinishedIntaking:
          io.setPosition(PivotConstants.intakePosition);
          break;

        case L1:
          io.setPosition(PivotConstants.L1Position);
          break;

        case L2:
          io.setPosition(PivotConstants.L2Position);
          break;

        case L3:
          io.setPosition(PivotConstants.L3Position);
          break;

        case L4:
          io.setPosition(PivotConstants.L4Position);
          break;

        case Shooting:
          io.setPosition(inputs.PositionRad);
          break;

        default:
          break;
      }
    } else {
      switch (stateIO.currentCoralState) {
        case IDLE:
          io.setPosition(PivotConstants.homedPosition);
          break;

        case ElevatorUp:
          io.setPosition(PivotConstants.homedPosition);
          break;

        case ReadyToIntake:
          io.setPosition(PivotConstants.intakePosition);
          break;

        case ClawIntaking:
          io.setPosition(PivotConstants.intakePosition);
          break;

        case FinishedIntaking:
          io.setPosition(PivotConstants.intakePosition);
          break;

        case L1:
          io.setPosition(PivotConstants.L1Position);
          break;

        case L2:
          io.setPosition(PivotConstants.L2Position);
          break;

        case L3:
          io.setPosition(PivotConstants.L3Position);
          break;

        case L4:
          io.setPosition(PivotConstants.L4Position);
          break;

        case Shooting:
          io.setPosition(inputs.PositionRad);
          break;

        default:
          break;
      }
    }
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

  public PivotIOInputs getInput() {
    return inputs;
  }
}

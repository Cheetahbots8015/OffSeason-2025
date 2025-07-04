package frc.robot.subsystems.pivot;

import static edu.wpi.first.units.Units.Volt;

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

  public PivotSubsystem(PivotIO io, StateManagerIOInputs stateIO) {
    this.stateIO = stateIO;
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

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Pivot", inputs);
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
    io.setPivotVoltage(0.0);
  }

  public PivotIO getIO() {
    return io;
  }

  public void setPivotVoltage(double volts) {
    io.setPivotVoltage(volts);
  }

  public void setPosition(double position) {
    io.setPosition(position);
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

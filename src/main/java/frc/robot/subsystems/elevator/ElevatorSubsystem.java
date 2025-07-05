package frc.robot.subsystems.elevator;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.constants.ElevatorConstants;
import frc.robot.subsystems.SuperStructureIO.SuperStructureIOInputs;
import frc.robot.subsystems.elevator.ElevatorIO.ElevatorIOInputs;
import org.littletonrobotics.junction.Logger;

public class ElevatorSubsystem extends SubsystemBase {

  private final ElevatorIO io;

  private final SuperStructureIOInputs stateIO;

  private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  public ElevatorSubsystem(ElevatorIO io, SuperStructureIOInputs stateIO) {
    this.io = io;
    this.stateIO = stateIO;
    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Elevator/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism(
                (voltage) -> setElevatorVoltage(voltage.in(Volt)), null, this));
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Elevator", inputs);
    if (stateIO.isUpdating) {
      switch (stateIO.targetCoralState) {
        case IDLE:
          io.setPosition(ElevatorConstants.homedPosition);
          break;

        case ElevatorUp:
          io.setPosition(ElevatorConstants.finishedIntakePosition);
          break;

        case ReadyToIntake:
          io.setPosition(ElevatorConstants.finishedIntakePosition);
          break;

        case ClawIntaking:
          io.setPosition(ElevatorConstants.intakePosition);
          break;

        case FinishedIntaking:
          io.setPosition(ElevatorConstants.finishedIntakePosition);
          break;

        case PivotAtReef:
          io.setPosition(inputs.PositionRad);
          break;

        case ElevatorAtReef:
          setToReef();
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
          io.setPosition(ElevatorConstants.homedPosition);
          break;

        case ElevatorUp:
          io.setPosition(ElevatorConstants.finishedIntakePosition);
          break;

        case ReadyToIntake:
          io.setPosition(ElevatorConstants.finishedIntakePosition);
          break;

        case ClawIntaking:
          io.setPosition(ElevatorConstants.intakePosition);
          break;

        case FinishedIntaking:
          io.setPosition(ElevatorConstants.finishedIntakePosition);
          break;

        case PivotAtReef:
          io.setPosition(inputs.PositionRad);
          break;

        case ElevatorAtReef:
          setToReef();
          break;

        case Shooting:
          io.setPosition(inputs.PositionRad);
          break;

        default:
          break;
      }
    }
  }

  public void shutdown() {
    io.setElevatorVoltage(0.0);
  }

  public ElevatorIO getIO() {
    return io;
  }

  public void setElevatorVoltage(double volts) {
    io.setElevatorVoltage(volts);
  }

  public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return sysId.quasistatic(direction);
  }

  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return sysId.dynamic(direction);
  }

  public double getElevatorVelocity() {
    return io.getElevatorVelocity();
  }

  public void VelocityVoltage(double velocity) {
    io.VelocityVoltage(velocity);
  }

  public void setPosition(double position) {
    io.setPosition(position);
  }

  public ElevatorIOInputs getInput() {
    return inputs;
  }

  public boolean isAtPosition(double position) {
    return Math.abs(inputs.PositionRad - position) < ElevatorConstants.PositionDeadband;
  }

  private void setToReef() {
    switch (stateIO.currentReef) {
      case "L1":
        setPosition(ElevatorConstants.L1Position);

      case "L2":
        setPosition(ElevatorConstants.L2Position);

      case "L3":
        setPosition(ElevatorConstants.L3Position);

      case "L4":
        setPosition(ElevatorConstants.L4Position);
    }
  }
}

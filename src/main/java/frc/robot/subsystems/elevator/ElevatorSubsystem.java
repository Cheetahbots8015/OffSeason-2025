package frc.robot.subsystems.elevator;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.constants.ElevatorConstants;
import frc.robot.subsystems.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs;
import org.littletonrobotics.junction.Logger;

public class ElevatorSubsystem extends SubsystemBase {

  private final ElevatorIO io;

  private final StateManagerIOInputs stateIO;

  private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  private double UpdutyCycleValue = ElevatorConstants.UpValue;
  private double DowndutyCycleValue = ElevatorConstants.DownValue;

  public ElevatorSubsystem(ElevatorIO io, StateManagerIOInputs stateIO) {
    this.io = io;
    this.stateIO = stateIO;

    SmartDashboard.putNumber("Elevator Up dutyCycle Value", UpdutyCycleValue);
    SmartDashboard.putNumber("Elevator Down dutyCycle Value", DowndutyCycleValue);
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
    UpdutyCycleValue = SmartDashboard.getNumber("Elevator Up dutyCycle Value", UpdutyCycleValue);
    DowndutyCycleValue =
        SmartDashboard.getNumber("Elevator Down dutyCycle Value", DowndutyCycleValue);
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

        case L1:
          io.setPosition(ElevatorConstants.L1Position);
          break;

        case L2:
          io.setPosition(ElevatorConstants.L2Position);
          break;

        case L3:
          io.setPosition(ElevatorConstants.L3Position);
          break;

        case L4:
          io.setPosition(ElevatorConstants.L4Position);
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

        case L1:
          io.setPosition(ElevatorConstants.L1Position);
          break;

        case L2:
          io.setPosition(ElevatorConstants.L2Position);
          break;

        case L3:
          io.setPosition(ElevatorConstants.L3Position);
          break;

        case L4:
          io.setPosition(ElevatorConstants.L4Position);
          break;

        case Shooting:
          io.setPosition(inputs.PositionRad);
          break;

        default:
          break;
      }
    }
  }

  /**
   * @param percentOutput Output percentage, from -1.0 to 1.0
   */
  public void runPercentOutput(double percentOutput) {
    io.elevatorDutyCycleOut(percentOutput);
  }

  public void shutdown() {
    io.elevatorDutyCycleOut(0.0);
  }

  public void defaultIdleVelocity() {
    runPercentOutput(ElevatorConstants.IdleDutyCycle);
  }

  public double getUpDutyCycleValue() {
    return UpdutyCycleValue;
  }

  public double getDownDutyCycleValue() {
    return DowndutyCycleValue;
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
}

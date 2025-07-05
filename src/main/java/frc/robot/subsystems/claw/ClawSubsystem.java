// ClawSubsystem - Subsystem to control a single TalonFX motor for a claw

package frc.robot.subsystems.claw;

import static edu.wpi.first.units.Units.Volt;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.constants.ClawConstants;
import frc.robot.subsystems.SuperStructureIO.SuperStructureIOInputs;
import frc.robot.subsystems.claw.ClawIO.ClawIOInputs;
import org.littletonrobotics.junction.Logger;

public class ClawSubsystem extends SubsystemBase {
  private final ClawIO io;
  private final SuperStructureIOInputs stateIO;

  private final ClawIOInputsAutoLogged inputs = new ClawIOInputsAutoLogged();
  private final SysIdRoutine sysId;

  public ClawSubsystem(ClawIO io, SuperStructureIOInputs stateIO) {
    this.io = io;
    this.stateIO = stateIO;

    sysId =
        new SysIdRoutine(
            new SysIdRoutine.Config(
                null,
                null,
                null,
                (state) -> Logger.recordOutput("Claw/Intake/SysIdState", state.toString())),
            new SysIdRoutine.Mechanism(
                (voltage) -> setIntakeVoltage(voltage.in(Volt)), null, this));
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Claw", inputs);

    if (stateIO.isUpdating) {
      switch (stateIO.targetCoralState) {
        case IDLE:
          io.setOpenLoop(0, 0);
          break;

        case ElevatorUp:
          io.setOpenLoop(0, 0);
          break;

        case ReadyToIntake:
          io.setOpenLoop(0, 0);
          break;

        case ClawIntaking:
          io.setIntakeVoltage(ClawConstants.intakeVolts);
          break;

        case FinishedIntaking:
          io.setOpenLoop(0, 0);
          break;

        case PivotAtReef:
          break;

        case ElevatorAtReef:
          break;

        case Shooting:
          if (stateIO.currentReef.equals("L4")) {
            io.L4Shoot();
          } else {
            io.normalShoot();
          }
          break;

        default:
          break;
      }
    } else {
      switch (stateIO.currentCoralState) {
        case IDLE:
          io.setOpenLoop(0, 0);
          break;

        case ElevatorUp:
          io.setOpenLoop(0, 0);
          break;

        case ReadyToIntake:
          io.setOpenLoop(0, 0);
          break;

        case ClawIntaking:
          io.setIntakeVoltage(ClawConstants.intakeVolts);
          break;

        case FinishedIntaking:
          io.setOpenLoop(0, 0);
          break;

        case PivotAtReef:
          break;

        case ElevatorAtReef:
          break;

        case Shooting:
          if (stateIO.currentReef.equals("L4")) {
            io.L4Shoot();
          } else {
            io.normalShoot();
          }
          break;

        default:
          break;
      }
    }
  }

  public void runVelocity(double intakeOutput, double shooterOutput) {
    io.setOpenLoop(intakeOutput, shooterOutput);
  }

  public void shutdown() {
    io.setOpenLoop(0.0, 0.0);
  }

  public ClawIO getIO() {
    return io;
  }

  public void setIntakeVoltage(double volts) {
    io.setIntakeVoltage(volts);
  }

  public void setShooterVoltage(double volts) {
    io.setShooterVoltage(volts);
  }

  public Command sysIdQuasistatic(SysIdRoutine.Direction direction) {
    return sysId.quasistatic(direction);
  }

  public Command sysIdDynamic(SysIdRoutine.Direction direction) {
    return sysId.dynamic(direction);
  }

  public ClawIOInputs getInput() {
    return inputs;
  }

  public void IntakeVelocityVoltage(double velocity) {
    io.IntakeVelocityVoltage(velocity);
  }
}

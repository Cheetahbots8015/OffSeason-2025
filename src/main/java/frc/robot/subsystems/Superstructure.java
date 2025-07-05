package frc.robot.subsystems;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANrangeConfiguration;
import com.ctre.phoenix6.hardware.CANrange;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.ClawConstants;
import frc.robot.constants.ElevatorConstants;
import frc.robot.constants.IntakeConstants;
import frc.robot.constants.PivotConstants;
import frc.robot.subsystems.SuperStructureIO.SuperStructureIOInputs;
import frc.robot.subsystems.SuperStructureIO.SuperStructureIOInputs.CoralState;
import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.mechanism.LoggedMechanism2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;

public class Superstructure extends SubsystemBase {
  private SuperStructureIOInputs inputs;

  private final ClawSubsystem claw;
  private final ElevatorSubsystem elevator;
  private final PivotSubsystem pivot;
  private final LoggedMechanism2d mechanism = new LoggedMechanism2d(3, 3);
  private final LoggedMechanismLigament2d m_elevatorSIM;
  private final LoggedMechanismLigament2d m_pivotSIM;

  private final CANrange canrange;
  private CANrangeConfiguration canrangeConfig = new CANrangeConfiguration();

  private final StatusSignal<Boolean> canrangeDetected;

  public Superstructure(
      ClawSubsystem claw,
      ElevatorSubsystem elevator,
      PivotSubsystem pivot,
      SuperStructureIOInputs inputs) {
    this.inputs = inputs;

    this.claw = claw;
    this.elevator = elevator;
    this.pivot = pivot;
    m_elevatorSIM =
        mechanism
            .getRoot("superstructure", 2, 0)
            .append(
                new LoggedMechanismLigament2d("elevator", 0, 90, 20, new Color8Bit(Color.kOrange)));
    m_pivotSIM =
        m_elevatorSIM.append(
            new LoggedMechanismLigament2d("pivot", 0.5, 0, 10, new Color8Bit(Color.kPurple)));

    canrange = new CANrange(IntakeConstants.canRangeID, IntakeConstants.canName);

    canrangeConfig.ProximityParams.ProximityThreshold = IntakeConstants.canRangeThreshold;
    canrangeConfig.ProximityParams.MinSignalStrengthForValidMeasurement =
        IntakeConstants.minSignalStrength;
    canrangeConfig.ProximityParams.ProximityHysteresis = IntakeConstants.canRangeHysteresis;

    canrange.getConfigurator().apply(canrangeConfig);

    canrangeDetected = canrange.getIsDetected();
  }

  @Override
  public void periodic() {
    this.checkStates();

    Logger.recordOutput("Superstructure/Mechanism", mechanism);
    m_elevatorSIM.setLength(elevator.getInput().PositionRad / 320 * 2);
    m_pivotSIM.setAngle(pivot.getInput().PositionRad / 335 * Math.PI);
  }

  private boolean readyToCook = true;

  private boolean clawIntakeRequest = false;

  // request to set to each of the positions from IDLE
  private boolean L1Request = false;
  private boolean L2Request = false;
  private boolean L3Request = false;
  private boolean L4Request = false;

  private boolean shootingRequest = false;

  private boolean idleRequest = false;

  private double startShootingTime = 0.0;

  public void checkStates() {
    if (L4Request) inputs.currentReef = "L4";
    else if (L3Request) inputs.currentReef = "L3";
    else if (L2Request) inputs.currentReef = "L2";
    else if (L1Request) inputs.currentReef = "L1";

    SmartDashboard.putString("CurrentState", inputs.currentCoralState.name());

    if (inputs.targetCoralState == null) SmartDashboard.putString("TargetState", "null");
    else SmartDashboard.putString("TargetState", inputs.targetCoralState.name());

    if (inputs.previousCoralState == null) SmartDashboard.putString("PreviousState", "null");
    else SmartDashboard.putString("PreviousState", inputs.previousCoralState.name());

    if (!readyToCook) {
      if (canrangeDetected.getValue()) this.setReadyToCook(true);
    } else {
      switch (inputs.currentCoralState) {
        case IDLE: // Elevator down, Pivot Up
          updateStateWithRequest(
              clawIntakeRequest,
              CoralState.ElevatorUp, // Release the button it will go back
              pivot.isAtPosition(PivotConstants.intakePosition),
              elevator.isAtPosition(ElevatorConstants.finishedIntakePosition));
          updateStateWithRequest(
              L1Request || L2Request || L3Request || L4Request,
              CoralState.PivotAtReef,
              this.pivotAtReef());
          break;

        case ElevatorUp:
          updateStateWithRequest(
              idleRequest, CoralState.IDLE, pivot.isAtPosition(PivotConstants.homedPosition));
          updateStateWithRequest(
              clawIntakeRequest,
              CoralState.ReadyToIntake,
              pivot.isAtPosition(PivotConstants.intakePosition));
          updateStateWithRequest(
              L1Request || L2Request || L3Request || L4Request,
              CoralState.PivotAtReef,
              this.pivotAtReef());
          break;

        case ReadyToIntake: // Elevator Up to top, Pivot Down
          updateStateWithRequest(
              clawIntakeRequest,
              CoralState.ClawIntaking,
              pivot.isAtPosition(PivotConstants.intakePosition),
              elevator.isAtPosition(ElevatorConstants.intakePosition));
          updateStateWithRequest(
              idleRequest,
              CoralState.ElevatorUp, // Force to home
              pivot.isAtPosition(PivotConstants.homedPosition),
              elevator.isAtPosition(ElevatorConstants.finishedIntakePosition));
          break;

        case ClawIntaking: // Elevator down a little with Pivot down touching coral
          updateStateWithRequest(
              !clawIntakeRequest,
              CoralState.FinishedIntaking, // Finish intaking once released button
              pivot.isAtPosition(PivotConstants.intakePosition),
              elevator.isAtPosition(ElevatorConstants.finishedIntakePosition));
          break;

        case FinishedIntaking:
          updateStateWithRequest(
              !canrangeDetected.getValue(),
              CoralState.ElevatorUp,
              pivot.isAtPosition(PivotConstants.homedPosition),
              elevator.isAtPosition(ElevatorConstants.finishedIntakePosition));
          // Improper Transitions
          updateStateWithRequest(
              clawIntakeRequest,
              CoralState.ClawIntaking, // Go and try to pick again
              pivot.isAtPosition(PivotConstants.intakePosition),
              elevator.isAtPosition(ElevatorConstants.intakePosition));
          updateStateWithRequest(
              idleRequest,
              CoralState.ElevatorUp, // Force to home
              pivot.isAtPosition(PivotConstants.homedPosition),
              elevator.isAtPosition(ElevatorConstants.finishedIntakePosition));
          break;

        case PivotAtReef:
          updateStateWithRequest(shootingRequest, CoralState.ElevatorAtReef, this.elevatorAtReef());
          updateStateWithRequest(
              idleRequest,
              CoralState.IDLE,
              pivot.isAtPosition(PivotConstants.homedPosition),
              elevator.isAtPosition(ElevatorConstants.homedPosition));
          break;

        case ElevatorAtReef:
          updateStateWithRequest(shootingRequest, CoralState.Shooting, true);

        case Shooting:
          updateStateWithRequest(
              !shootingRequest && detectShootingTime(),
              CoralState.IDLE,
              pivot.isAtPosition(PivotConstants.homedPosition),
              elevator.isAtPosition(ElevatorConstants.homedPosition));
          break;

        default:
          break;
      }
    }
  }

  private void updateStateWithRequest(
      boolean triggeredCondition, CoralState target, boolean... successfulCondition) {
    if (triggeredCondition) {
      if (target == CoralState.Shooting && inputs.targetCoralState != CoralState.Shooting) {
        startShootingTime = Timer.getFPGATimestamp();
      }
      if (inputs.previousCoralState == CoralState.Shooting) {
        this.setReadyToCook(false);
      }

      inputs.isUpdating = true;
      inputs.targetCoralState = target;

      boolean allTrue = true;
      for (boolean b : successfulCondition) {
        if (!b) {
          allTrue = false;
          break;
        }
      }

      if (allTrue) {
        inputs.previousCoralState = inputs.currentCoralState;
        inputs.currentCoralState = inputs.targetCoralState;
        inputs.targetCoralState = null;
        inputs.isUpdating = false;
      }
    }
  }

  private boolean detectShootingTime() {
    return (Timer.getFPGATimestamp() - startShootingTime) > ClawConstants.shootingSeconds;
  }

  public void setL1Request(boolean request) {
    L1Request = request;
  }

  public void setL2Request(boolean request) {
    L2Request = request;
  }

  public void setL3Request(boolean request) {
    L3Request = request;
  }

  public void setL4Request(boolean request) {
    L4Request = request;
  }

  public void setIdleRequest(boolean request) {
    idleRequest = request;
  }

  public void setClawIntakeRequest(boolean request) {
    clawIntakeRequest = request;
  }

  public void setShootingRequest(boolean request) {
    shootingRequest = request;
  }

  public void setReadyToCook(boolean request) {
    readyToCook = request;
  }

  public boolean pivotAtReef() {
    switch (inputs.currentReef) {
      case "L1":
        return pivot.isAtPosition(PivotConstants.L1Position);

      case "L2":
        return pivot.isAtPosition(PivotConstants.L2Position);

      case "L3":
        return pivot.isAtPosition(PivotConstants.L3Position);

      case "L4":
        return pivot.isAtPosition(PivotConstants.L4Position);

      default:
        return false;
    }
  }

  public boolean elevatorAtReef() {
    switch (inputs.currentReef) {
      case "L1":
        return elevator.isAtPosition(ElevatorConstants.L1Position);

      case "L2":
        return elevator.isAtPosition(ElevatorConstants.L2Position);

      case "L3":
        return elevator.isAtPosition(ElevatorConstants.L3Position);

      case "L4":
        return elevator.isAtPosition(ElevatorConstants.L4Position);

      default:
        return false;
    }
  }
}

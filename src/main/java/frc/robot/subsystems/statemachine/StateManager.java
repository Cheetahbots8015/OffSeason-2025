package frc.robot.subsystems.statemachine;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.*;
import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs.CoralState;

public class StateManager {
  private StateManagerIO io;
  private StateManagerIOInputs inputs;

  private PivotSubsystem pivot;
  private ClawSubsystem claw;
  private ElevatorSubsystem elevator;
  private ClimberSubsystem climber;
  private IntakeSubsystem intake;

  private boolean readyToCook = false;

  private boolean clawIntakeRequest = false;

  private boolean L1Request = false;
  private boolean L2Request = false;
  private boolean L3Request = false;
  private boolean L4Request = false;

  private boolean shootingRequest = false;

  private boolean idleRequest = false;

  private double startShootingTime = 0.0;

  public StateManager(
      StateManagerIO io,
      PivotSubsystem pivot,
      ClawSubsystem claw,
      ElevatorSubsystem elevator,
      ClimberSubsystem climber,
      IntakeSubsystem intake) {
    this.io = io;
    this.pivot = pivot;
    this.claw = claw;
    this.elevator = elevator;
    this.climber = climber;
    this.intake = intake;
  }

  public void checkStates() {
    // Command Scheduler will automatically set the requests to false when needed.
    if (readyToCook) {
      switch (inputs.currentCoralState) {
        case IDLE: // Elevator down, Pivot Up
          updateStateWithRequest(
              clawIntakeRequest,
              CoralState.ElevatorUp, // Release the button it will go back
              pivot.getInput().PositionRad == PivotConstants.intakePosition,
              elevator.getInput().PositionRad == ElevatorConstants.finishedIntakePosition);
          updateStateWithRequest(
              L1Request,
              CoralState.L1,
              pivot.getInput().PositionRad == PivotConstants.L1Position,
              elevator.getInput().PositionRad == ElevatorConstants.L1Position);
          updateStateWithRequest(
              L2Request,
              CoralState.L2,
              pivot.getInput().PositionRad == PivotConstants.L2Position,
              elevator.getInput().PositionRad == ElevatorConstants.L2Position);
          updateStateWithRequest(
              L3Request,
              CoralState.L3,
              pivot.getInput().PositionRad == PivotConstants.L3Position,
              elevator.getInput().PositionRad == ElevatorConstants.L3Position);
          updateStateWithRequest(
              L4Request,
              CoralState.L4,
              pivot.getInput().PositionRad == PivotConstants.L4Position,
              elevator.getInput().PositionRad == ElevatorConstants.L4Position);
          break;

        case ElevatorUp:
          updateStateWithRequest(
            inputs.previousCoralState == CoralState.ReadyToIntake, 
            CoralState.IDLE, 
            pivot.getInput().PositionRad == PivotConstants.homedPosition
          );
          updateStateWithRequest(
            inputs.previousCoralState == CoralState.FinishedIntaking, 
            CoralState.IDLE, 
            pivot.getInput().PositionRad == PivotConstants.homedPosition
          );
          updateStateWithRequest(
            inputs.previousCoralState == CoralState.IDLE, 
            CoralState.ReadyToIntake, 
            pivot.getInput().PositionRad == PivotConstants.intakePosition
          );
          break;

        case ReadyToIntake: // Elevator Up to top, Pivot Down
          updateStateWithRequest(
              clawIntakeRequest,
              CoralState.ClawIntaking,
              pivot.getInput().PositionRad == PivotConstants.intakePosition,
              elevator.getInput().PositionRad == ElevatorConstants.intakePosition);
          updateStateWithRequest(
              idleRequest,
              CoralState.ElevatorUp, // Force to home
              pivot.getInput().PositionRad == PivotConstants.homedPosition,
              elevator.getInput().PositionRad == ElevatorConstants.homedPosition);
          break;

        case ClawIntaking: // Elevator down a little with Pivot down touching coral
          updateStateWithRequest(
              !clawIntakeRequest,
              CoralState.FinishedIntaking, // Finish intaking once released button
              pivot.getInput().PositionRad == PivotConstants.intakePosition,
              elevator.getInput().PositionRad == ElevatorConstants.finishedIntakePosition);
          break;

        case FinishedIntaking:
          updateStateWithRequest(
              !intake.getCanRange(),
              CoralState.ElevatorUp,
              pivot.getInput().PositionRad == PivotConstants.homedPosition,
              elevator.getInput().PositionRad == ElevatorConstants.homedPosition);
          // Improper Transitions
          updateStateWithRequest(
              clawIntakeRequest,
              CoralState.ClawIntaking, // Go and try to pick again
              pivot.getInput().PositionRad == PivotConstants.intakePosition,
              elevator.getInput().PositionRad == ElevatorConstants.intakePosition);
          updateStateWithRequest(
              idleRequest,
              CoralState.ElevatorUp, // Force to home
              pivot.getInput().PositionRad == PivotConstants.homedPosition,
              elevator.getInput().PositionRad == ElevatorConstants.homedPosition);
          break;

        case L1:
          updateStateWithRequest(shootingRequest, CoralState.Shooting, true);
          break;

        case L2:
          updateStateWithRequest(shootingRequest, CoralState.Shooting, true);
          break;

        case L3:
          updateStateWithRequest(shootingRequest, CoralState.Shooting, true);
          break;

        case L4:
          updateStateWithRequest(shootingRequest, CoralState.Shooting, true);
          break;

        case Shooting:
          updateStateWithRequest(!shootingRequest, CoralState.IDLE, detectShootingTime());
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
      if (inputs.currentCoralState == CoralState.L4) {
        inputs.isShootingL4 = true;
      } else if (inputs.currentCoralState == CoralState.L1
          || inputs.currentCoralState == CoralState.L2
          || inputs.currentCoralState == CoralState.L3) {
        inputs.isShootingL4 = false;
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
}

package frc.robot.subsystems.statemachine;

import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.elevator.ElevatorIO;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs.CoralState;

public class StateManager {
  private StateManagerIO io;
  private StateManagerIOInputs inputs;

  private boolean isUpdating;

  private PivotSubsystem pivot;
  private ClawSubsystem claw;
  private ElevatorSubsystem elevator;
  private ClimberSubsystem climber;
  private IntakeSubsystem intake;

  private boolean readyToCook = false;
  private boolean clawRequest = false;

  private boolean L1Request = false;
  private boolean L2Request = false;
  private boolean L3Request = false;
  private boolean L4Request = false;

  private boolean shootingRequest = false;

  private boolean idleRequest = false;

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
        case FinishedIntaking:
          break;

        case InClaw:
          updateStateWithRequest(L1Request, CoralState.L1, pivot.getIO().);
          updateStateWithRequest(L2Request, CoralState.L2, );
          updateStateWithRequest(L3Request, CoralState.L3, );
          updateStateWithRequest(L4Request, CoralState.L4, );
          break;

        case L1:
          break;

        case L2:
          break;

        case L3:
          break;

        case L4:
          break;

        case Shooting:
          break;

        case IDLE:
          updateStateWithRequest(clawRequest, CoralState.InClaw, !intake.getCanRange());
          

          // TODO: to be cooked
          // updateStateWithRequest(reefRequest, CoralState.ReefPosition, );
          // updateStateWithRequest(shootingRequest, CoralState.Shooting, );

          break;

        default:
          break;
      }
    }
  }

  private void updateStateWithRequest(
      boolean triggeredCondition, CoralState target, boolean... successfulCondition) {
    if (triggeredCondition) {
      isUpdating = true;
      inputs.targetCoralState = target;

      boolean allTrue = true;
      for (boolean b : successfulCondition) {
        if (!b) {
          allTrue = false;
          break;
        }
      }

      if (allTrue) {
        inputs.currentCoralState = inputs.targetCoralState;
        inputs.targetCoralState = null;
        isUpdating = false;
      }
    }
  }

  private void setCurrentCoralState(CoralState state) {
    inputs.currentCoralState = state;
  }
}

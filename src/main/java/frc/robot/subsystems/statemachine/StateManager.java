package frc.robot.subsystems.statemachine;

import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs;
import frc.robot.subsystems.statemachine.StateManagerIO.StateManagerIOInputs.MachineState;

public class StateManager {
  private StateManagerIO io;
  private StateManagerIOInputs inputs;

  private boolean isUpdating;

  private PivotSubsystem pivot;
  private ClawSubsystem claw;
  private ElevatorSubsystem elevator;
  private ClimberSubsystem climber;
  private IntakeSubsystem intake;

  private boolean intakingRequest = false;
  private boolean clawRequest = false;
  private boolean reefRequest = false;
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
    switch (inputs.currentMachineState) {
      case FinishedIntaking:
        break;

      case InClaw:
        break;

      case ReefPosition:
        break;

      case Shooting:
        break;

      case IDLE:
        updateStateWithRequest(
            intakingRequest, MachineState.FinishedIntaking, intake.getCanRange());
        updateStateWithRequest(clawRequest, MachineState.InClaw, !intake.getCanRange());

        // TODO: to be cooked
        // updateStateWithRequest(reefRequest, MachineState.ReefPosition, );
        // updateStateWithRequest(shootingRequest, MachineState.Shooting, );

        break;

      default:
        break;
    }
  }

  private void updateStateWithRequest(
      boolean triggeredCondition, MachineState target, boolean... successfulCondition) {
    if (triggeredCondition) {
      isUpdating = true;
      inputs.targetMachineState = target;

      boolean allTrue = true;
      for (boolean b : successfulCondition) {
        if (!b) {
          allTrue = false;
          break;
        }
      }

      if (allTrue) {
        inputs.currentMachineState = inputs.targetMachineState;
        inputs.targetMachineState = null;
        isUpdating = false;
      }
    }
  }

  private void setCurrentMachineState(MachineState state) {
    inputs.currentMachineState = state;
  }
}

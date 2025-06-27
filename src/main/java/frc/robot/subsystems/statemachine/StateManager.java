package frc.robot.subsystems.statemachine;

import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.climber.ClimberSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;

public class StateManager {
  public enum MachineState {
    FinishedIntaking,
    InClaw,
    Shooting,
    IDLE
  }

  private MachineState currentMachineState = MachineState.IDLE;
  private MachineState targetMachineState = null;

  private boolean isUpdating;

  private PivotSubsystem pivot;
  private ClawSubsystem claw;
  private ElevatorSubsystem elevator;
  private ClimberSubsystem climber;
  private IntakeSubsystem intake;

  private boolean intakingRequest = false;
  private boolean clawRequest = false;
  private boolean shootingRequest = false;
  private boolean idleRequest = false;

  public StateManager(
      PivotSubsystem pivot,
      ClawSubsystem claw,
      ElevatorSubsystem elevator,
      ClimberSubsystem climber,
      IntakeSubsystem intake) {
    this.pivot = pivot;
    this.claw = claw;
    this.elevator = elevator;
    this.climber = climber;
    this.intake = intake;
  }

  public void checkStates(){
    switch (currentMachineState) {
        case FinishedIntaking:
            if (clawRequest){
                isUpdating = true;
                targetMachineState = MachineState.InClaw;
            }
            if (shootingRequest){
                isUpdating = true;
                targetMachineState = MachineState.Shooting;
            }
            if (idleRequest){
                isUpdating = true;
                targetMachineState = MachineState.IDLE;
            }
            break;

        case InClaw:
            if (intakingRequest){
                isUpdating = true;
                targetMachineState = MachineState.FinishedIntaking;
            }
            if (shootingRequest){
                isUpdating = true;
                targetMachineState = MachineState.Shooting;
            }
            if (idleRequest){
                isUpdating = true;
                targetMachineState = MachineState.IDLE;
            }
            break;

        case Shooting:
            if (intakingRequest){
                isUpdating = true;
                targetMachineState = MachineState.FinishedIntaking;
            }
            if (clawRequest){
                isUpdating = true;
                targetMachineState = MachineState.InClaw;
            }
            if (idleRequest){
                isUpdating = true;
                targetMachineState = MachineState.IDLE;
            }
            break;

        case IDLE:
            if (intakingRequest){
                isUpdating = true;
                targetMachineState = MachineState.FinishedIntaking;
            }
            if (clawRequest){
                isUpdating = true;
                targetMachineState = MachineState.InClaw;
            }
            if (shootingRequest){
                isUpdating = true;
                targetMachineState = MachineState.Shooting;
            }
            break;

        default:

            break;
    }
  }

  private void setCurrentMachineState(MachineState state){
    currentMachineState = state;
  }
}

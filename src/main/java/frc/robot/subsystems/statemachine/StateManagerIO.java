package frc.robot.subsystems.statemachine;

import org.littletonrobotics.junction.AutoLog;

public class StateManagerIO {
  @AutoLog
  public static class StateManagerIOInputs {
    public enum CoralState {
      ClawIntaking,
      ReadyToIntake,
      FinishedIntaking,
      ElevatorUp,
      L1,
      L2,
      L3,
      L4,
      Shooting,
      IDLE
    }

    public enum AlgaeState {}

    public CoralState previousCoralState = null;
    public CoralState currentCoralState = CoralState.IDLE;
    public CoralState targetCoralState = null;

    public boolean isShootingL4 = false;

    public boolean isUpdating;
  }
}

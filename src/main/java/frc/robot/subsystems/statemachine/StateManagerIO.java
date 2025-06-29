package frc.robot.subsystems.statemachine;

import org.littletonrobotics.junction.AutoLog;

public class StateManagerIO {
  @AutoLog
  public static class StateManagerIOInputs {
    public enum CoralState {
      FinishedIntaking,
      InClaw,
      L1,
      L2,
      L3,
      L4,
      Shooting,
      IDLE
    }

    public enum AlgaeState {
      
    }

    public CoralState currentCoralState = CoralState.IDLE;
    public CoralState targetCoralState = null;
  }
}

package frc.robot.subsystems.statemachine;

import org.littletonrobotics.junction.AutoLog;

public class StateManagerIO {
  @AutoLog
  public static class StateManagerIOInputs {
    public enum MachineState {
      FinishedIntaking,
      InClaw,
      ReefPosition,
      Shooting,
      IDLE
    }

    public MachineState currentMachineState = MachineState.IDLE;
    public MachineState targetMachineState = null;
  }
}

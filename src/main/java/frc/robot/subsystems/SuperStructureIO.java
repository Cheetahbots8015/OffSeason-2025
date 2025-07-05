package frc.robot.subsystems;

import org.littletonrobotics.junction.AutoLog;

public class SuperStructureIO {
  @AutoLog
  public static class SuperStructureIOInputs {
    public enum CoralState {
      ClawIntaking,
      ReadyToIntake,
      FinishedIntaking,
      ElevatorUp,
      PivotAtReef,
      ElevatorAtReef,
      Shooting,
      IDLE
    }

    public enum AlgaeState {}

    public CoralState previousCoralState = null;
    public CoralState currentCoralState = CoralState.IDLE;
    public CoralState targetCoralState = null;

    public boolean isUpdating;

    public String currentReef = "";
  }
}

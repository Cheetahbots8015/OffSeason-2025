package frc.robot.subsystems.roller;

import org.littletonrobotics.junction.AutoLog;

public interface RollerIO {
  @AutoLog
  public static class RollerIOInputs {
    public double PositionRad = 0.0;
    public double VelocityRadPerSec = 0.0;
    public double AppliedVolts = 0.0;
    public double CurrentAmps = 0.0;
  }

  /** Updates the set of loggable inputs. */
  public default void updateInputs(RollerIOInputs inputs) {}

  /** Run the roller at the specified open loop value. */
  public default void setOpenLoop(double output) {}
}

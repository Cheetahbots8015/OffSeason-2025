package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.AutoLog;

public interface ClimberIO {
  @AutoLog
  public static class ClimberIOInputs {
    public double RollerPositionRad = 0.0;
    public double RollerVelocityRadPerSec = 0.0;
    public double RollerAppliedVolts = 0.0;
    public double RollerCurrentAmps = 0.0;
    public double ClimberPositionRad = 0.0;
    public double ClimberVelocityRadPerSec = 0.0;
    public double ClimberAppliedVolts = 0.0;
    public double ClimberCurrentAmps = 0.0;
  }

  /** Updates the set of loggable inputs. */
  public default void updateInputs(ClimberIOInputs inputs) {}

  /** Run the climber at the specified open loop value. */
  public default void setOpenLoop(double rollerOutput, double climberOutput) {}
}

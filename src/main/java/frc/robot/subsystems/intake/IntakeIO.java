package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {
  @AutoLog
  public static class IntakeIOInputs {
    public double PivotPositionRotation = 0.0;
    public double PivotVelocityRPS = 0.0;
    public double PivotAppliedVolts = 0.0;
    public double PivotCurrentAmps = 0.0;

    public double RollerPositionRotation = 0.0;
    public double RollerVelocityRPS = 0.0;
    public double RollerAppliedVolts = 0.0;
    public double RollerCurrentAmps = 0.0;

    public double IndexerPositionRotation = 0.0;
    public double IndexerVelocityRPS = 0.0;
    public double IndexerAppliedVolts = 0.0;
    public double IndexerCurrentAmps = 0.0;
  }

  /** Updates the set of loggable inputs. */
  public default void updateInputs(IntakeIOInputs inputs) {}

  /** Run the roller & Pivot at the specified open loop value. */
  public default void setRollerVoltage(double output) {}

  public default void setPivotVoltage(double output) {}

  public default void setIndexerVoltage(double output) {}

  public default void setPivotPosition(double output) {}
}

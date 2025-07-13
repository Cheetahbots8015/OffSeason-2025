package frc.robot.constants;

public class ClimberConstants {
  public static final int clawID = 59;
  public static final int pivotID = 58;
  public static final int canrangID = 6;
  public static final boolean claw_neutralmode_Coast = false;
  public static final boolean claw_inverted_CounterClockwisePositive = true;
  public static final boolean pivot_neutralmode_Coast = false;
  public static final boolean pivot_inverted_CounterClockwisePositive = true;
  public static final double clawkP = 1.0;
  public static final double clawkI = 0.0;
  public static final double clawkD = 0.0;
  public static final double clawkA = 0.0;
  public static final double clawkS = 0.0;
  public static final double clawkV = 0.0;

  public static final double pivotkP = 2;
  public static final double pivotkI = 0.0;
  public static final double pivotkD = 0.0;
  public static final double pivotkA = 0.0;
  public static final double pivotkS = 0.0;
  public static final double pivotkV = 0.0;
  public static final double holdVoltage = 0.2;

  public static final double canrangeDistance = 0.1;

  public static final double setPositionUpVoltage = 2;
  public static final double setPositionDownvoltage = -2;
  public static final double statusUpdateFrequency = 50.0;

  public static final double pivotUpDutyCycleOutValue = -0.2;
  public static final double pivotDownDutyCycleOutValue = 0.2;
  public static final double clawDutyCycleOutValue = 0.2;

  // Close Loop Constants
  public static final double climberDefaultPosition = 0.0;
  public static final double climberFinalPosition = 0.0;
  public static final double ReductionRatio = 1.0 / 32;
  public static final double PositionDeadband = 3.0;
}

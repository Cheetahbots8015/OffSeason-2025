package frc.robot;

public class IntakeConstants {
  public static final int indexerID = 2;
  public static final int intakeID = 3;
  public static final int armID = 4;

  public static final double armDownPosition = 0.0;
  public static final double armHomePosition = 0.0;

  public static final double intakingDutyCycleOut = 0.2;
  public static final double indexerDutyCycleOut = 0.2;

  public static final boolean indexer_neutralmode_Coast = false;
  public static final boolean indexer_inverted_CounterClockwisePositive = false;
  public static final boolean intake_neutralmode_Coast = true;
  public static final boolean intake_inverted_CounterClockwisePositive = false;
  public static final boolean arm_neutralmode_Coast = false;
  public static final boolean arm_inverted_CounterClockwisePositive = false;

  public static final double armForwardDutyCycleLimit = 0.55;
  public static final double armReverseDutyCycleLimit = -0.55;
  public static final boolean armForwardSoftLimitEnable = true;
  public static final boolean armReverseSoftLimitEnable = true;
  public static final double armForwardSoftLimitThreshold = 0.0;
  public static final double armReverseSoftLimitThreshold = -0.0;

  public static final double armCruiseVelocity = 1.07;
  public static final double armCruiseAcceleration = 1.07;

  public static final double indexer_kP = 0.0;
  public static final double indexer_kI = 0.0;
  public static final double indexer_kD = 0.0;
  public static final double indexer_kA = 0.0;
  public static final double indexer_kS = 0.0;
  public static final double indexer_kV = 0.0;
  public static final double intake_kP = 0.0;
  public static final double intake_kI = 0.0;
  public static final double intake_kD = 0.0;
  public static final double intake_kA = 0.0;
  public static final double intake_kS = 0.0;
  public static final double intake_kV = 0.0;
  public static final double arm_kP = 0.0;
  public static final double arm_kI = 0.0;
  public static final double arm_kD = 0.0;
  public static final double arm_kA = 0.0;
  public static final double arm_kS = 0.0;
  public static final double arm_kV = 0.0;
  // canRange can ID, name and configs
  public static final int canRangeID = 60;
  public static final String canName = "canivore";
  public static final double canRangeThreshold = 0.06;
  public static final double canRangeHysteresis = 0.005;
  public static final int minSignalStrength = 2500;
}

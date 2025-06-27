// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.elevator;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ElevatorSubsystem extends SubsystemBase {
  public enum elevatorIdleState {
    in,
    out,
    stop
  }

  private final ElevatorIO io;
  private final ElevatorIOInputsAutoLogged inputs = new ElevatorIOInputsAutoLogged();
  private elevatorIdleState systemIdleState = elevatorIdleState.stop;

  private double idleSpeed = 0.1;

  public ElevatorSubsystem(ElevatorIO io) {
    this.io = io;
    SmartDashboard.putNumber("Elevator Idle Speed", idleSpeed);
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Elevator", inputs);

    idleSpeed = SmartDashboard.getNumber("Elevator Idle Speed", idleSpeed);
  }

  public void runVelocity(double velocity) {
    io.setDutyCycleOut(velocity);
  }

  public void openLoopDefaultIdelVelocity() {
    if (systemIdleState == elevatorIdleState.in) {
      runVelocity(idleSpeed);
    } else if (systemIdleState == elevatorIdleState.out) {
      runVelocity(-idleSpeed);
    } else {
      shutdown();
    }
  }

  public void setSystemIdleState(elevatorIdleState state) {
    systemIdleState = state;
  }

  public elevatorIdleState getSystemIdleState() {
    return systemIdleState;
  }

  public void shutdown() {
    io.setDutyCycleOut(0.0);
  }
}

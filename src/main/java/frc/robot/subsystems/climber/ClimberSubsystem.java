// ClimberSubsystem - Subsystem to control a single TalonFX motor for a climber

package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {
  public enum climberIdleState {
    in,
    out,
    stop
  }

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();
  private climberIdleState systemIdleState = climberIdleState.stop;

  private double idleSpeed = 0.1;

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;

    SmartDashboard.putNumber("Intaking Idle Speed", idleSpeed);
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Climber", inputs);

    idleSpeed = SmartDashboard.getNumber("Intaking Idle Speed", idleSpeed);
  }

  public void runVelocity(double velocity) {
    io.setOpenLoop(velocity);
  }

  public void defaultIdelVelocity() {
    if (systemIdleState == climberIdleState.in) {
      runVelocity(idleSpeed);
    } else if (systemIdleState == climberIdleState.out) {
      runVelocity(-idleSpeed);
    } else {
      shutdown();
    }
  }

  public void setSystemIdleState(climberIdleState state) {
    systemIdleState = state;
  }

  public climberIdleState getSystemIdleState() {
    return systemIdleState;
  }

  public void shutdown() {
    io.setOpenLoop(0.0);
  }
}

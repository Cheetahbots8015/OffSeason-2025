// ClimberSubsystem - Subsystem to control a single TalonFX motor for a climber

package frc.robot.subsystems.climber;

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

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Climber", inputs);
  }

  public void runVelocity(double velocity) {
    io.setOpenLoop(velocity);
  }

  public void defaultIdelVelocity() {
    if (systemIdleState == climberIdleState.in) {
      runVelocity(0.2);
    } else if (systemIdleState == climberIdleState.out) {
      runVelocity(-0.2);
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

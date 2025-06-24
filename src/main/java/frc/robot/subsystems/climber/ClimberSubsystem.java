// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {
  public enum rollerIdleState {
    in,
    out,
    stop
  }

  public enum climberIdleState {
    in,
    out,
    stop
  }

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();
  private rollerIdleState rollersystemIdleState = rollerIdleState.stop;
  private climberIdleState climbersystemIdleState = climberIdleState.stop;
  private double rol = 0.0, cli = 0.0;

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Climber", inputs);
  }

  public void runVelocity(double rollerOutput, double climberOutput) {
    io.setOpenLoop(rollerOutput, climberOutput);
  }

  public void defaultIdelVelocity() {
    if (rollersystemIdleState == rollerIdleState.in) {
      rol = 0.1;
    } else if (rollersystemIdleState == rollerIdleState.out) {
      rol = -0.1;
    } else {
      rol = 0;
    }
    if (climbersystemIdleState == climberIdleState.in) {
      cli = 0.1;
    } else if (climbersystemIdleState == climberIdleState.out) {
      cli = -0.1;
    } else {
      cli = 0;
    }
    io.setOpenLoop(rol, cli);
  }

  public void setSystemIdleState(rollerIdleState state, climberIdleState state2) {
    rollersystemIdleState = state;
    climbersystemIdleState = state2;
  }

  public rollerIdleState getrollerSystemIdleState() {
    return rollersystemIdleState;
  }

  public climberIdleState getclimberSystemIdleState() {
    return climbersystemIdleState;
  }

  public void shutdown() {
    io.setOpenLoop(0.0, 0.0);
  }
}

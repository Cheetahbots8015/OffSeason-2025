// RollerSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.roller;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class RollerSubsystem extends SubsystemBase {
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

  private final RollerIO io;
  private final RollerIOInputsAutoLogged inputs = new RollerIOInputsAutoLogged();
  private rollerIdleState rollersystemIdleState = rollerIdleState.stop;
  private climberIdleState climbersystemIdleState = climberIdleState.stop;
  private double rol = 0.0, cli = 0.0;

  public RollerSubsystem(RollerIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Roller", inputs);
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
    if (climbersystemIdleState == climbersystemIdleState.in) {
      cli = 0.1;
    } else if (rollersystemIdleState == rollerIdleState.out) {
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

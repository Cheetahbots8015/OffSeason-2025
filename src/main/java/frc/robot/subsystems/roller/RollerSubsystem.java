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

  private final RollerIO io;
  private final RollerIOInputsAutoLogged inputs = new RollerIOInputsAutoLogged();
  private rollerIdleState systemIdleState = rollerIdleState.stop;

  public RollerSubsystem(RollerIO io) {
    this.io = io;
  }

  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Roller", inputs);
  }

  public void runVelocity(double velocity) {
    io.setOpenLoop(velocity);
  }

  public void defaultIdelVelocity() {
    if (systemIdleState == rollerIdleState.in) {
      runVelocity(0.2);
    } else if (systemIdleState == rollerIdleState.out) {
      runVelocity(-0.2);
    } else {
      shutdown();
    }
  }

  public void setSystemIdleState(rollerIdleState state) {
    systemIdleState = state;
  }

  public rollerIdleState getSystemIdleState() {
    return systemIdleState;
  }

  public void shutdown() {
    io.setOpenLoop(0.0);
  }
}

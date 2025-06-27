// ClimberSubsystem - Subsystem to control a single TalonFX motor for a roller

package frc.robot.subsystems.climber;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import org.littletonrobotics.junction.Logger;

public class ClimberSubsystem extends SubsystemBase {

  private final ClimberIO io;
  private final ClimberIOInputsAutoLogged inputs = new ClimberIOInputsAutoLogged();

  public ClimberSubsystem(ClimberIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Climber", inputs);
  }

  public void runVelocity(double rollerOutput, double climberOutput) {
    io.setOpenLoop(rollerOutput, climberOutput);
  }

  public void shutdown() {
    io.setOpenLoop(0.0, 0.0);
  }
}

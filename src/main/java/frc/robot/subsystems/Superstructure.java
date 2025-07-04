package frc.robot.subsystems;

import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.pivot.PivotSubsystem;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.mechanism.LoggedMechanism2d;
import org.littletonrobotics.junction.mechanism.LoggedMechanismLigament2d;

public class Superstructure extends SubsystemBase {
  private final ClawSubsystem claw;
  private final ElevatorSubsystem elevator;
  private final PivotSubsystem pivot;
  private final LoggedMechanism2d mechanism = new LoggedMechanism2d(3, 3);
  private final LoggedMechanismLigament2d m_elevatorSIM;
  private final LoggedMechanismLigament2d m_pivotSIM;

  public Superstructure(ClawSubsystem claw, ElevatorSubsystem elevator, PivotSubsystem pivot) {
    this.claw = claw;
    this.elevator = elevator;
    this.pivot = pivot;
    m_elevatorSIM =
        mechanism
            .getRoot("superstructure", 2, 0)
            .append(
                new LoggedMechanismLigament2d("elevator", 0, 90, 20, new Color8Bit(Color.kOrange)));
    m_pivotSIM =
        m_elevatorSIM.append(
            new LoggedMechanismLigament2d("pivot", 0.5, 0, 10, new Color8Bit(Color.kPurple)));
  }

  @Override
  public void periodic() {
    Logger.recordOutput("Superstructure/Mechanism", mechanism);
    m_elevatorSIM.setLength(elevator.getInput().PositionRad / 320 * 2);
    m_pivotSIM.setAngle(pivot.getInput().PositionRad / 335 * Math.PI);
  }
}

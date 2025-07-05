// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.ClawCommands.ClawAlageInCommand;
import frc.robot.commands.ClawCommands.ClawAlageShootCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.ElevatorCommands.*;
import frc.robot.commands.IntakeCommands.*;
import frc.robot.commands.PivotCommands.*;
import frc.robot.constants.ContainerConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.claw.ClawIOSim;
import frc.robot.subsystems.claw.ClawIOTalonFX;
import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.elevator.ElevatorIOSim;
import frc.robot.subsystems.elevator.ElevatorIOTalonFX;
import frc.robot.subsystems.elevator.ElevatorSubsystem;
import frc.robot.subsystems.intake.IntakeIOSim;
import frc.robot.subsystems.intake.IntakeIOTalonFX;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.pivot.PivotIOSim;
import frc.robot.subsystems.pivot.PivotIOTalonFX;
import frc.robot.subsystems.pivot.PivotSubsystem;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final Drive drive;
  private final ClawSubsystem clawSubsystem;
  private final ElevatorSubsystem elevatorSubsystem;
  private final PivotSubsystem pivotSubsystem;
  private final IntakeSubsystem intakeSubsystem;

  // Controller
  private final CommandXboxController controller = new CommandXboxController(0);

  // Dashboard inputs
  private final LoggedDashboardChooser<Command> autoChooser;

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    switch (ContainerConstants.currentMode) {
      case REAL:
        // Real robot, instantiate hardware IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOTalonFX(TunerConstants.FrontLeft),
                new ModuleIOTalonFX(TunerConstants.FrontRight),
                new ModuleIOTalonFX(TunerConstants.BackLeft),
                new ModuleIOTalonFX(TunerConstants.BackRight));
        clawSubsystem = new ClawSubsystem(new ClawIOTalonFX());
        elevatorSubsystem = new ElevatorSubsystem(new ElevatorIOTalonFX());
        pivotSubsystem = new PivotSubsystem(new PivotIOTalonFX());
        intakeSubsystem = new IntakeSubsystem(new IntakeIOTalonFX());
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIOSim(TunerConstants.FrontLeft),
                new ModuleIOSim(TunerConstants.FrontRight),
                new ModuleIOSim(TunerConstants.BackLeft),
                new ModuleIOSim(TunerConstants.BackRight));
        clawSubsystem = new ClawSubsystem(new ClawIOSim());
        elevatorSubsystem = new ElevatorSubsystem(new ElevatorIOSim());
        pivotSubsystem = new PivotSubsystem(new PivotIOSim());
        intakeSubsystem = new IntakeSubsystem(new IntakeIOSim());
        break;

      default:
        // Replayed robot, disable IO implementations
        drive =
            new Drive(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        clawSubsystem = new ClawSubsystem(new ClawIOTalonFX());
        elevatorSubsystem = new ElevatorSubsystem(new ElevatorIOTalonFX());
        pivotSubsystem = new PivotSubsystem(new PivotIOTalonFX());
        intakeSubsystem = new IntakeSubsystem(new IntakeIOTalonFX());
        break;
    }

    // Set up auto routines
    autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

    // Set up SysId routines
    autoChooser.addOption(
        "Drive Wheel Radius Characterization", DriveCommands.wheelRadiusCharacterization(drive));
    autoChooser.addOption(
        "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Forward)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Quasistatic Reverse)",
        drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
    autoChooser.addOption(
        "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
    autoChooser.addOption(
        "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));

    SmartDashboard.putNumber("ClawIntakeVolts", 0.0);
    SmartDashboard.putNumber("ClawShooterVolts", 0.0);
    SmartDashboard.putNumber("ClimberPivotVolts", 0.0);
    SmartDashboard.putNumber("ClimberClawVolts", 0.0);
    SmartDashboard.putNumber("ElevatorVolts", 0.0);
    SmartDashboard.putNumber("PivotVolts", 0.5);
    SmartDashboard.putNumber("IntakeIndexerVolts", 3);
    SmartDashboard.putNumber("IntakeRollerVolts", 3);
    SmartDashboard.putNumber("SetMotionMagicPositionRads", 160.0);
    SmartDashboard.putNumber("PivotMotionMagicPositionRads", 100.0);

    // Configure the button bindings
    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    drive.setDefaultCommand(
        DriveCommands.joystickDrive(
            drive,
            () -> -controller.getLeftY(),
            () -> -controller.getLeftX(),
            () -> -controller.getRightX()));

    controller
        .x()
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), new Rotation2d())),
                    drive)
                .ignoringDisable(true));

    /* Claw Subsystem test
    controller.leftTrigger().whileTrue(new
    ClawIntakeInCommand(clawSubsystem)); controller.leftBumper().whileTrue(new
    ClawIntakeOutCommand(clawSubsystem)); controller.rightTrigger().whileTrue(new
    ClawShooterInCommand(clawSubsystem)); controller.rightBumper().whileTrue(new
    ClawShooterOutCommand(clawSubsystem));
    */

    // Intake Subsystem test

    controller.leftTrigger().whileTrue(new IntakeRollerIndexerInCommand(intakeSubsystem));
    controller.leftBumper().whileTrue(new IntakeIndexerOutCommand(intakeSubsystem));
    controller.rightBumper().whileTrue(new IntakeRollerOutCommand(intakeSubsystem));
    controller.rightTrigger().whileTrue(new IntakeArmSetPositionCommand(intakeSubsystem, -35));

    controller.povUp().whileTrue(new ElevatorSetPositionCommand(elevatorSubsystem, 40));
    controller.povDown().whileTrue(new ElevatorReleaseCommand(elevatorSubsystem));
    controller.povLeft().whileTrue(new PivotSetPositionCommand(pivotSubsystem, 0));

    /* Claw Intake Command
    controller
        .y()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 140)
                .andThen(
                    new PivotSetPositionCommand(pivotSubsystem, 330)
                        .andThen(
                            new ElevatorLittleCommand(elevatorSubsystem, 120)
                                .alongWith(new ClawTimedIntakeCommand(clawSubsystem)))
                        .andThen(new ElevatorLittleCommand(elevatorSubsystem, 140))
                        .andThen(new PivotSetPositionCommand(pivotSubsystem, 0))
                        .andThen(new ElevatorSetPositionCommand(elevatorSubsystem, 5))
                        .andThen(new ElevatorReleaseCommand(elevatorSubsystem))));

    // L3 Command
    controller
        .x()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 130)
                .alongWith(new PivotSetPositionCommand(pivotSubsystem, 80))
                .andThen(new ClawTimedShootCommand(clawSubsystem)));

    // L4 Command
    controller
        .b()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 315)
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 95))
                .andThen(new ClawTimedShootCommand(clawSubsystem)));

    // L2 Command
    controller
        .a()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 5)
                .alongWith(new PivotSetPositionCommand(pivotSubsystem, 50))
                .andThen(new ClawTimedShootCommand(clawSubsystem)));
    */

    // Alage Level1 Intake
    controller
        .a()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 210)
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 210))
                .andThen(new ClawAlageInCommand(clawSubsystem)));

    // Alage Shoot
    controller
        .b()
        .whileTrue(
            
            new ElevatorSetPositionCommand(elevatorSubsystem, 315)
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 40))
                .andThen(new ClawAlageShootCommand(clawSubsystem)));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.get();
  }
}

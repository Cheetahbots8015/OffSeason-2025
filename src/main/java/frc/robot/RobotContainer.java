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
import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.commands.ClawCommands.*;
import frc.robot.commands.ClimberCommand.ClimberClawCommand;
import frc.robot.commands.ClimberCommand.ClimberPivotDefaultCommand;
import frc.robot.commands.ClimberCommand.ClimberPivotUpCommand;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.ElevatorCommands.*;
import frc.robot.commands.IntakeCommands.*;
import frc.robot.commands.PivotCommands.*;
import frc.robot.constants.ContainerConstants;
import frc.robot.constants.FieldConstants;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.claw.ClawIOSim;
import frc.robot.subsystems.claw.ClawIOTalonFX;
import frc.robot.subsystems.claw.ClawSubsystem;
import frc.robot.subsystems.climber.ClimberIOTalonFX;
import frc.robot.subsystems.climber.ClimberSubsystem;
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
  public final Drive drive;
  private final ClawSubsystem clawSubsystem;
  private final ElevatorSubsystem elevatorSubsystem;
  private final PivotSubsystem pivotSubsystem;
  private final IntakeSubsystem intakeSubsystem;
  private final ClimberSubsystem climberSubsystem;

  private final RobotContainer robotContainer = this;

  // Controller
  private CommandXboxController controller = new CommandXboxController(0);
  private final CommandXboxController controller2 = new CommandXboxController(1);
  private final CommandXboxController climberController = new CommandXboxController(2);

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
        climberSubsystem = new ClimberSubsystem(new ClimberIOTalonFX());
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
        climberSubsystem = new ClimberSubsystem(new ClimberIOTalonFX());
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
        climberSubsystem = new ClimberSubsystem(new ClimberIOTalonFX());
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

    // Configure the button bindings
    Commands.runOnce(() -> new PivotStartCommand(pivotSubsystem), pivotSubsystem)
        .ignoringDisable(true);

    // Default Command to set intake arm position
    intakeSubsystem.setDefaultCommand(new IntakeArmSetPositionCommand(intakeSubsystem, 0));
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
        .povUp()
        .onTrue(
            Commands.runOnce(
                    () ->
                        drive.setPose(
                            new Pose2d(drive.getPose().getTranslation(), new Rotation2d())),
                    drive)
                .ignoringDisable(true));

    // Intake Subsystem test
    controller.leftTrigger().whileTrue(new IntakeArmForwardCommand(intakeSubsystem, 1));
    // controller.rightTrigger().whileTrue(new IntakeArmReverseCommand(intakeSubsystem, 1));
    controller2.x().whileTrue(new IntakeRollerIndexerOutCommand(intakeSubsystem));
    controller2.a().whileTrue(new IntakeArmSetPositionCommand(intakeSubsystem, 0));

    controller2.b().whileTrue(new ElevatorSetPositionCommand(elevatorSubsystem, 0.382));
    controller2.povLeft().whileTrue(new PivotSetPositionCommand(pivotSubsystem, 0));

    // Claw Intake Command
    controller
        .y()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 0.90)
                .alongWith(new PivotSetPositionCommand(pivotSubsystem, 177))
                .andThen(
                    new ElevatorSetPositionCommand(elevatorSubsystem, 0.80)
                        .alongWith(new ClawIntakeCommand(clawSubsystem, intakeSubsystem)))
                .andThen(new ElevatorSetPositionCommand(elevatorSubsystem, 0.9))
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 0, 130))
                .andThen(new ElevatorSetPositionCommand(elevatorSubsystem, 0.382))
                .andThen(new ClawShootTimedBackCommand(clawSubsystem)));

    // L3 Command
    controller
        .a()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 0.879)
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 43))
                .andThen(new ClawShootCommand(clawSubsystem))
                .andThen(
                    new PivotSetPositionCommand(pivotSubsystem, 0)
                        .alongWith(new ElevatorSetPositionCommand(elevatorSubsystem, 0.382))));

    // L4 Command
    controller
        .b()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 1.50)
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 35))
                .andThen(new ClawShootCommand(clawSubsystem))
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 0))
                .andThen(new ElevatorSetPositionCommand(elevatorSubsystem, 0.382)));

    /* L2 Command
    controller
        .a()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 0.382)
                .alongWith(new PivotSetPositionCommand(pivotSubsystem, 27))
                .andThen(new ClawTimedShootCommand(clawSubsystem)));
    */

    //  Alage Level1 Intake
    controller
        .x()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 1.198)
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 112.8))
                .andThen(new ClawAlageInCommand(clawSubsystem)));

    // Alage Shoot
    controller
        .leftBumper()
        .whileTrue(
            new ElevatorSetPositionCommand(elevatorSubsystem, 1.615)
                .andThen(new PivotSetPositionCommand(pivotSubsystem, 21.5))
                .andThen(new ClawAlageShootCommand(clawSubsystem)));

    // // Auto Allignment
    // controller2.leftBumper().whileTrue(new alignreef(false, drive));

    // controller2.rightBumper().whileTrue(new alignreef(true, drive));

    // controller2.y().whileTrue(new alignalage(drive));

    // Climber Test
    controller2.povUp().whileTrue((new ClimberClawCommand(climberSubsystem)));
    controller2
        .leftTrigger()
        .whileTrue(
            new ClimberPivotDefaultCommand(climberSubsystem)
                .alongWith(new PivotSetPositionCommand(pivotSubsystem, 100)));
    controller2.rightTrigger().whileTrue(new ClimberPivotUpCommand(climberSubsystem));

    // Auto Test
    final DriverStation.Alliance alliance = DriverStation.getAlliance().orElse(Alliance.Red);

    // Pose2d startPose = new Pose2d(new Translation2d(12, 1.5), Rotation2d.fromDegrees(14));

    // // if (alliance == Alliance.Red) {
    // //   startPose =
    // //       FieldConstants.rotateAroundCenter(
    // //           startPose, FieldConstants.FieldCenter, Rotation2d.k180deg);
    // // }

    // drive.setPose(startPose);
    SmartDashboard.putData(
        "Pathfind to Closest Reef",
        Commands.runOnce(
            () -> {
              Pose2d currentPose = drive.getPose();

              Pose2d closestPose =
                  FieldConstants.getClosestReefPose(currentPose.getTranslation(), 1, alliance);

              AutoBuilder.pathfindToPose(
                      closestPose,
                      new PathConstraints(
                          1, 1.0, Units.degreesToRadians(540), Units.degreesToRadians(360)),
                      0.0)
                  .schedule();
            }));
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

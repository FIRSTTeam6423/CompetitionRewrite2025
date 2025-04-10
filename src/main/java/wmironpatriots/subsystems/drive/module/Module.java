// Copyright (c) 2025 FRC 6423 - Ward Melville Iron Patriots
// https://github.com/FIRSTTeam6423
// 
// Open Source Software; you can modify and/or share it under the terms of
// MIT license file in the root directory of this project

package wmironpatriots.subsystems.drive.module;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import monologue.Annotations.Log;
import wmironpatriots.Robot;
import wmironpatriots.subsystems.drive.module.ModuleHardware.ModuleHardwareInputs;

public class Module {
  // CONSTANTS
  public static final double PIVOT_REDUCTION = 21.428571428571427;
  public static final double DRIVE_REDUCTION = 6.122448979591837;
  public static final double WHEEL_RADIUS_METERS = 0.049784;

  public static final record ModuleCfg(
      int index,
      int pivotId,
      int driveId,
      int cancoderId,
      double cancoderOffsetRevs,
      boolean pivotInverted,
      boolean driveInverted,
      Translation2d moduleOffset) {}

  @Log
  private ModuleHardwareInputs loggedInputs =
      new ModuleHardwareInputs(false, false, 0, 0, 0, 0, false, 0, 0, 0, 0, 0);

  private final ModuleHardware hardware;

  public Module(ModuleCfg cfg) {
    hardware = Robot.isReal()
      ? new ModuleHardwareReal(cfg)
      : new ModuleHardwareReal(cfg); // TODO Simulated module
  }

  /** Periodic module update */
  public void periodic() {
    hardware.updateModuleInputs(loggedInputs);
  }

  /**
   * @return Module angle as {@link Rotation2d}
   */
  public Rotation2d getRotation2d() {
    return Rotation2d.fromRotations(loggedInputs.cancoderPoseRevs());
  }

  /**
   * @return {@link SwerveModuleState} of current module speed and angle
   */
  public SwerveModuleState getModuleState() {
    return new SwerveModuleState(loggedInputs.driveVelMps(), getRotation2d());
  }

  /**
   * @return {@link SwerveModulePosition} of current drive distance and angle
   */
  public SwerveModulePosition getModulePose() {
    return new SwerveModulePosition(loggedInputs.drivePoseRevs(), getRotation2d());
  }

  /**
   * Run {@link SwerveModuleState} setpoint
   *
   * @param setpoint Setpoint to run
   * @return optimized {@link SwerveModuleState} setpoint
   */
  public SwerveModuleState setModuleSetpoint(SwerveModuleState setpoint) {
    // TODO add openloop support
    setpoint.optimize(getRotation2d());
    setpoint.speedMetersPerSecond *= setpoint.angle.minus(getRotation2d()).getCos();

    hardware.setPivotPose(setpoint.angle.getRotations());
    hardware.setDriveVel(setpoint.speedMetersPerSecond);

    return setpoint;
  }

  /** Sets all module motor input to 0 */
  public void stop() {
    hardware.stopMotors();
  }
}

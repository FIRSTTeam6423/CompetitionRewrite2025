// Copyright (c) 2025 FRC 6423 - Ward Melville Iron Patriots
// https://github.com/FIRSTTeam6423
// 
// Open Source Software; you can modify and/or share it under the terms of
// MIT license file in the root directory of this project

package wmironpatriots.subsystems.drive.module;

/** Generalized interface for controlling hardware internals of module */
public interface ModuleHardware {
  /** Record storing loggable hardare inputs */
  public record ModuleHardwareInputs(
      boolean pivotFaulted,
      boolean cancoderFaulted,
      double pivotPoseRevs,
      double cancoderPoseRevs,
      double pivotAppliedVolts,
      double pivotCurrentAmps,
      boolean driveFaulted,
      double drivePoseRevs,
      double driveVelMps,
      double driveAppliedVolts,
      double driveCurrentAmps,
      double driveTorqueAmps) {}

  /** Update a set of loggable inputs */
  public void updateModuleInputs(ModuleHardwareInputs inputs);

  public void setPivotCurrent(double currentAmps);

  public void setPivotPose(double poseRevs);

  public void setDriveCurrent(double currentAmps);

  public void setDriveVel(double velMps);

  public void stopMotors();

  /**
   * Enable motor coasting for easier movement
   *
   * @param enabled is coast mode enabled
   */
  public void coastModeEnabled(boolean enabled);
}

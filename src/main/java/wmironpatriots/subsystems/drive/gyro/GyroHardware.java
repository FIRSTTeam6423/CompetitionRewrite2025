// Copyright (c) 2025 FRC 6423 - Ward Melville Iron Patriots
// https://github.com/FIRSTTeam6423
// 
// Open Source Software; you can modify and/or share it under the terms of
// MIT license file in the root directory of this project

package wmironpatriots.subsystems.drive.gyro;

import edu.wpi.first.math.geometry.Rotation2d;

public interface GyroHardware {
  /**
   * @return {@link Rotation2d} of gyro heading
   */
  public Rotation2d getRotation2d();
}

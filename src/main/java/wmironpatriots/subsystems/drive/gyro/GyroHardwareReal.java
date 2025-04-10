// Copyright (c) 2025 FRC 6423 - Ward Melville Iron Patriots
// https://github.com/FIRSTTeam6423
// 
// Open Source Software; you can modify and/or share it under the terms of
// MIT license file in the root directory of this project

package wmironpatriots.subsystems.drive.gyro;

import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;

public class GyroHardwareReal implements GyroHardware {
  private final Pigeon2 pigeon;

  public GyroHardwareReal(int pigeonId) {
    pigeon = new Pigeon2(pigeonId);
  }

  @Override
  public Rotation2d getRotation2d() {
    return pigeon.getRotation2d();
  }
}

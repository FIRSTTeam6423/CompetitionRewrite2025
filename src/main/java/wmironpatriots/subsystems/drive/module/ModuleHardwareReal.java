// Copyright (c) 2025 FRC 6423 - Ward Melville Iron Patriots
// https://github.com/FIRSTTeam6423
// 
// Open Source Software; you can modify and/or share it under the terms of
// MIT license file in the root directory of this project

package wmironpatriots.subsystems.drive.module;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import lib.TalonFxUtil;

public class ModuleHardwareReal implements ModuleHardware {
  private final TalonFX pivot, drive;
  private final CANcoder cancoder;

  private final TalonFXConfiguration pivotCfg, driveCfg;
  private final CANcoderConfiguration cancoderCfg;

  private final TorqueCurrentFOC currentReq;
  private final VelocityTorqueCurrentFOC velRequest;
  private final PositionTorqueCurrentFOC poseRequest;

  public ModuleHardwareReal(
      int pivotId,
      int driveId,
      int cancoderId,
      double cancoderOffsetRevs,
      boolean pivotInverted,
      boolean driveInverted) {
    
    // Init hardware
    pivot = new TalonFX(pivotId);
    drive = new TalonFX(driveId);
    cancoder = new CANcoder(cancoderId);

    TalonFxUtil.addTalon(pivot);
    TalonFxUtil.addTalon(drive);

    // Configure pivot motor
    pivotCfg = TalonFxUtil.getDefaultTalonCfg();
      
    pivotCfg.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    pivotCfg.MotorOutput.Inverted = pivotInverted ? InvertedValue.CounterClockwise_Positive : InvertedValue.Clockwise_Positive;

    pivotCfg.CurrentLimits.StatorCurrentLimit = 40.0;
    pivotCfg.CurrentLimits.StatorCurrentLimitEnable = true;

    pivotCfg.TorqueCurrent.PeakForwardTorqueCurrent = 40.0;
    pivotCfg.TorqueCurrent.PeakReverseTorqueCurrent = 40.0;
    pivotCfg.TorqueCurrent.TorqueNeutralDeadband = 0.0;

    pivotCfg.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.FusedCANcoder;
    pivotCfg.Feedback.SensorToMechanismRatio = 1.0;
    pivotCfg.Feedback.RotorToSensorRatio = 0.0; // TODO put reduction
    pivotCfg.Feedback.FeedbackRemoteSensorID = cancoderId;
    pivotCfg.ClosedLoopGeneral.ContinuousWrap = true;
    pivotCfg.ClosedLoopRamps.TorqueClosedLoopRampPeriod = 0.02;

    pivotCfg.Slot0.kP = 0.0;
    pivotCfg.Slot0.kD = 0.0;
    pivotCfg.Slot0.kA = 0.0;
    pivotCfg.Slot0.kV = 0.0;
    pivotCfg.Slot0.kS = 0.0;

    pivot.getConfigurator().apply(pivotCfg);

    // Configure drive motor
    driveCfg = TalonFxUtil.getDefaultTalonCfg();

    driveCfg.MotorOutput.NeutralMode = NeutralModeValue.Brake;
    driveCfg.MotorOutput.Inverted =
        driveInverted ? InvertedValue.Clockwise_Positive : InvertedValue.CounterClockwise_Positive;

    driveCfg.CurrentLimits.StatorCurrentLimit = 120.0;
    driveCfg.CurrentLimits.StatorCurrentLimitEnable = true;

    driveCfg.TorqueCurrent.PeakForwardTorqueCurrent = 120.0;
    driveCfg.TorqueCurrent.PeakReverseTorqueCurrent = -120.0;

    // TODO driveConfig.Feedback.SensorToMechanismRatio = DRIVE_REDUCTION / (WHEEL_RADIUS_METERS * 2 * Math.PI);
    driveCfg.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
    driveCfg.ClosedLoopGeneral.ContinuousWrap = true;
    driveCfg.ClosedLoopRamps.TorqueClosedLoopRampPeriod = 0.02;

    driveCfg.Slot0.kP = 0.0;
    driveCfg.Slot0.kD = 0.0;
    driveCfg.Slot0.kA = 0.0;
    driveCfg.Slot0.kV = 0.0;
    driveCfg.Slot0.kS = 0.0;

    drive.getConfigurator().apply(driveCfg);

    cancoderCfg = new CANcoderConfiguration();
    // TODO config
    cancoder.getConfigurator().apply(cancoderCfg);

    // Init motor requests
    currentReq = new TorqueCurrentFOC(0.0);
    velRequest = new VelocityTorqueCurrentFOC(0.0);
    poseRequest = new PositionTorqueCurrentFOC(0.0);
  }

  @Override
  public void updateModuleInputs(ModuleHardwareInputs inputs) {
    inputs = new ModuleHardwareInputs(false, false, 0, 0, 0, 0, false, 0, 0, 0, 0, 0);
  }

  @Override
  public void setPivotCurrent(double currentAmps) {
    pivot.setControl(currentReq.withOutput(currentAmps));
  }

  @Override
  public void setPivotPose(double poseRevs) {
    pivot.setControl(poseRequest.withPosition(poseRevs));
  }

  @Override
  public void setDriveCurrent(double currentAmps) {
    drive.setControl(currentReq.withOutput(currentAmps));
  }

  @Override
  public void setDriveVel(double velRpm) {
    drive.setControl(velRequest.withVelocity(velRpm));
  }

  @Override
  public void stopMotors() {
    pivot.stopMotor();
    drive.stopMotor();
  }

  @Override
  public void coastModeEnabled(boolean enabled) {
    NeutralModeValue idleMode = enabled ? NeutralModeValue.Coast : NeutralModeValue.Brake;
    pivotCfg.MotorOutput.NeutralMode = idleMode;
    driveCfg.MotorOutput.NeutralMode = idleMode;

    pivot.getConfigurator().apply(pivotCfg);
    drive.getConfigurator().apply(driveCfg);
  }
}

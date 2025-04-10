package wmironpatriots.subsystems.drive.module;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionTorqueCurrentFOC;
import com.ctre.phoenix6.controls.TorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

public class ModuleHardwareReal implements ModuleHardware {
    private final TalonFX pivot, drive;
    private final CANcoder cancoder;

    private final TalonFXConfiguration pivotConfig, driveConfig;
    private final CANcoderConfiguration cancoderConfig;

    private final TorqueCurrentFOC currentReq;
    private final VelocityTorqueCurrentFOC velRequest;
    private final PositionTorqueCurrentFOC poseRequest;

    public ModuleHardwareReal(int pivotId, int driveId, int CANcoderId, double cancoderOffsetRevs, boolean pivotInverted, boolean driveInverted) {
        pivot = new TalonFX(pivotId);
        drive = new TalonFX(driveId);
        cancoder = new CANcoder(CANcoderId);

        pivotConfig = new TalonFXConfiguration();
        // TODO config
        pivot.getConfigurator().apply(pivotConfig);

        driveConfig = new TalonFXConfiguration();
        // TODO config
        drive.getConfigurator().apply(driveConfig);

        cancoderConfig = new CANcoderConfiguration();
        // TODO config
        cancoder.getConfigurator().apply(cancoderConfig);

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
    }
}

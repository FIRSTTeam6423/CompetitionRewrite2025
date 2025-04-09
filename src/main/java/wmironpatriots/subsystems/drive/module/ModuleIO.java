package wmironpatriots.subsystems.drive.module;

/** Generalized interface for controlling hardware internals of module */
public interface ModuleIO {
    /** Record storing loggable hardare inputs */
    public record ModuleIOInputs(
        boolean pivotFaulted,
        boolean cancoderFaulted,
        double pivotPoseRevs,
        double cancoderPoseRevs,
        double pivotAppliedVolts,
        double pivotCurrentAmps,
        boolean driveFaulted,
        double drivePoseRevs,
        double driveVelRPM,
        double driveAppliedVolts,
        double driveCurrentAmps,
        double driveTorqueAmps
    ) {}

    /** Update a set of loggable inputs */
    public void updateModuleInputs(ModuleIOInputs inputs);

    public void setPivotCurrent(double current);

    public void setPivotPose(double poseRevs);

    public void setDriveCurrent(double current);

    public void setDriveVel(double velMPS);

    public void stopMotors();

    /**
     * Enable motor coasting for easier movement
     * 
     * @param enabled is coast mode enabled
     */
    public void coastModeEnabled(boolean enabled);
}
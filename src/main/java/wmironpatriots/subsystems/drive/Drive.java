package wmironpatriots.subsystems.drive;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import wmironpatriots.subsystems.drive.gyro.GyroHardware;
import wmironpatriots.subsystems.drive.module.Module;
import wmironpatriots.subsystems.drive.module.Module.ModuleCfg;

public class Drive {
    // * CONSTANTS
    public static final Translation2d[] MODULE_OFFSETS = new Translation2d[4]; 

    private Module[] modules;
    private GyroHardware gyro;

    private final SwerveDriveKinematics kinematics;
    private final SwerveDrivePoseEstimator odometry;

    public Drive(GyroHardware gyro, ModuleCfg... cfgs) {
        modules = new Module[cfgs.length];
        Translation2d[] offsets = new Translation2d[cfgs.length];
        for(ModuleCfg cfg : cfgs) {
            modules[cfg.index()] = new Module(cfg);
            offsets[cfg.index()] = cfg.moduleOffset();
        }
        this.gyro = gyro;

        kinematics = new SwerveDriveKinematics(offsets);
        odometry = new SwerveDrivePoseEstimator(kinematics, gyro.getRotation2d(), new SwerveModulePosition[4], new Pose2d());
    }
}

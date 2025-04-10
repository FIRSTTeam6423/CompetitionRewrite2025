package wmironpatriots.subsystems.drive.module;

import monologue.Annotations.Log;
import wmironpatriots.Robot;
import wmironpatriots.subsystems.drive.module.ModuleHardware.ModuleHardwareInputs;

public class Module {
    // CONSTANTS
    public static final double PIVOT_REDUCTION = 21.428571428571427;
    public static final double DRIVE_REDUCTION = 6.122448979591837;
    public static final double WHEEL_RADIUS_METERS = 0.049784;

    public static final record ModuleCfg(int pivotId, int driveId, int cancoderId, double cancoderOffsetRevs, boolean pivotInverted, boolean driveInverted) {}

    @Log private ModuleHardwareInputs loggedInputs = new ModuleHardwareInputs(false, false, 0, 0, 0, 0, false, 0, 0, 0, 0, 0);

    private final ModuleHardware io;

    public Module(ModuleCfg cfg) {
        io = Robot.isReal() 
            ? new ModuleHardwareReal(cfg)
            : new ModuleHardwareReal(cfg); // TODO replace with simulated hardware

    }

    public void periodic() {
        io.updateModuleInputs(loggedInputs);
    }
}

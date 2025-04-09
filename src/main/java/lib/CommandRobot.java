package lib;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public abstract class CommandRobot extends TimedRobot {
    private final Timer gcTimer = new Timer();
    private final CommandScheduler scheduler = CommandScheduler.getInstance();

    private Command autonCommand;

    public CommandRobot(double period) {
        super(period);
    }

    public CommandRobot() {
        super(kDefaultPeriod);
    }

    @Override
    public void robotPeriodic() {
        scheduler.run();

        if (gcTimer.hasElapsed(5)) {
            System.gc();
        }
    }

    @Override
    public void disabledInit() {
        scheduler.cancelAll();
        System.gc();
    }

    @Override
    public void disabledExit() {
        scheduler.cancelAll();
        System.gc();
    }

    @Override
    public void autonomousInit() {
        autonCommand = getAuton();

        if (autonCommand != null) {
            autonCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {
        scheduler.cancelAll();
        System.gc();
    }

    @Override
    public void teleopInit() {
        if (autonCommand != null) {
            autonCommand.cancel();
        } 
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {}

    @Override
    public void testInit() {
        scheduler.cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {}

    protected abstract Command getAuton();
}

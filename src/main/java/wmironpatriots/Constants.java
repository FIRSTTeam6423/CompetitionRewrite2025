package wmironpatriots;

import static edu.wpi.first.units.Units.Seconds;
import static edu.wpi.first.units.Units.Volts;

import edu.wpi.first.units.measure.Time;
import edu.wpi.first.units.measure.Voltage;

public class Constants {
    public static final Time LOOPTIME = Seconds.of(0.02);
    public static final Voltage BROWNOUT_VOLTAGE = Volts.of(6.0);
}

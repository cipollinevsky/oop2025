public class DigitalClock extends Clock {

    public enum Mode {
        H24,
        H12
    }

    private final Mode mode;

    public DigitalClock(City city, Mode mode) {
        super(city);
        this.mode = mode;
    }

    @Override
    public String toString() {
        if (mode == Mode.H24) {
            return super.toString();
        } else {
            int hour = getHour();
            int minute = getMinute();
            int second = getSecond();

            String ampm = (hour < 12) ? "AM" : "PM";
            int hour12 = hour % 12;
            if (hour12 == 0) {
                hour12 = 12; // godzina 0 to 12 AM/PM
            }

            return String.format("%d:%02d:%02d %s", hour12, minute, second, ampm);
        }
    }
}

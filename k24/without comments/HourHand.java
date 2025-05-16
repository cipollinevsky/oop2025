import java.time.LocalTime;

public class HourHand extends ClockHand {

    private double angle;

    @Override
    public void setTime(LocalTime time) {
        int hour = time.getHour() % 12; // 12-godzinny format
        int minute = time.getMinute();
        int second = time.getSecond();

        angle = hour * 30.0 + minute * 0.5 + second * (0.5 / 60.0);
    }

    @Override
    public String toSvg() {
        return String.format(
                "<line x1=\"100\" y1=\"100\" x2=\"100\" y2=\"50\" stroke=\"black\" stroke-width=\"4\" " +
                        "transform=\"rotate(%.2f 100 100)\" />%n", angle);
    }
}

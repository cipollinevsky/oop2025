import java.time.LocalTime;

public class MinuteHand extends ClockHand {

    private double angle;

    @Override
    public void setTime(LocalTime time) {
        int minute = time.getMinute();
        int second = time.getSecond();

        angle = minute * 6.0 + second * 0.1; // 6° na minutę + 0.1° na sekundę
    }

    @Override
    public String toSvg() {
        return String.format(
                "<line x1=\"100\" y1=\"100\" x2=\"100\" y2=\"30\" stroke=\"black\" stroke-width=\"2\" " +
                        "transform=\"rotate(%.2f 100 100)\" />%n", angle);
    }
}

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;

public class AnalogClock extends Clock {

    private final List<ClockHand> hands;

    public AnalogClock() {
        super(null);
        hands = List.of(
                new HourHand(),
                new MinuteHand(),
                new SecondHand()
        );
    }

    public AnalogClock(City city) {
        super(city);
        hands = List.of(
                new HourHand(),
                new MinuteHand(),
                new SecondHand()
        );
        updateHands();
    }

    private void updateHands() {
        LocalTime time = getTime();
        for (ClockHand hand : hands) {
            hand.setTime(time);
        }
    }

    protected LocalTime getTime() {
        return super.getTime();
    }

    @Override
    public void setTime(int hour, int minute, int second) {
        super.setTime(hour, minute, second);
        updateHands();
    }

    @Override
    public void setCurrentTime() {
        super.setCurrentTime();
        updateHands();
    }

    public void toSvg(String path) throws IOException {
        StringBuilder svg = new StringBuilder();

        svg.append("""
            <svg width="200" height="200" xmlns="http://www.w3.org/2000/svg" version="1.1">
            <circle cx="100" cy="100" r="90" stroke="black" stroke-width="4" fill="white"/>
            """);

        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30);
            double x1 = 100 + Math.sin(angle) * 80;
            double y1 = 100 - Math.cos(angle) * 80;
            double x2 = 100 + Math.sin(angle) * 90;
            double y2 = 100 - Math.cos(angle) * 90;
            svg.append(String.format("<line x1=\"%.1f\" y1=\"%.1f\" x2=\"%.1f\" y2=\"%.1f\" stroke=\"black\" stroke-width=\"2\" />%n", x1, y1, x2, y2));
        }

        for (ClockHand hand : hands) {
            svg.append(hand.toSvg());
        }

        svg.append("</svg>");

        Files.writeString(Path.of(path), svg.toString());
    }
}

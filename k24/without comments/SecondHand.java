import java.time.LocalTime;

public class SecondHand extends ClockHand {

    private double angle;

    @Override
    public void setTime(LocalTime time) {
        int seconds = time.getSecond();
        angle = seconds * 6.0;
    }

    @Override
    public String toSvg() {
        return String.format(
                "<line x1=\"100\" y1=\"100\" x2=\"100\" y2=\"15\" stroke=\"red\" stroke-width=\"1\" " +
                        "transform=\"rotate(%.2f 100 100)\" />%n", angle);
    }
}

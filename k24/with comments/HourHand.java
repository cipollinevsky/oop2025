import java.time.LocalTime;

/**
 * Klasa reprezentująca wskazówkę godzinową zegara tarczowego.
 */
public class HourHand extends ClockHand {

    // Kąt wskazówki godzinowej w stopniach (0° = 12:00:00)
    private double angle;

    /**
     * Ustawia kąt wskazówki godzinowej na podstawie czasu.
     * Wskazówka porusza się płynnie z dokładnością do sekundy.
     * Kąt obliczamy jako:
     * 30° na każdą godzinę + 0.5° na każdą minutę + (0.5° / 60) na każdą sekundę.
     * @param time czas lokalny
     */
    @Override
    public void setTime(LocalTime time) {
        int hour = time.getHour() % 12; // 12-godzinny format
        int minute = time.getMinute();
        int second = time.getSecond();

        angle = hour * 30.0 + minute * 0.5 + second * (0.5 / 60.0);
    }

    /**
     * Zwraca znacznik SVG wskazówki godzinowej.
     * Wskazówka jest linią od środka tarczy (100,100) do długości 50,
     * o grubości 4 i czarnym kolorze.
     * @return łańcuch znaków SVG
     */
    @Override
    public String toSvg() {
        return String.format(
                "<line x1=\"100\" y1=\"100\" x2=\"100\" y2=\"50\" stroke=\"black\" stroke-width=\"4\" " +
                        "transform=\"rotate(%.2f 100 100)\" />%n", angle);
    }
}

import java.time.LocalTime;

/**
 * Klasa reprezentująca wskazówkę minutową zegara tarczowego.
 */
public class MinuteHand extends ClockHand {

    // Kąt wskazówki minutowej w stopniach (0° = 12:00:00)
    private double angle;

    /**
     * Ustawia kąt wskazówki minutowej na podstawie czasu.
     * Wskazówka porusza się płynnie z dokładnością do sekundy.
     * Kąt obliczamy jako:
     * 6° na każdą minutę + 0.1° na każdą sekundę.
     * @param time czas lokalny
     */
    @Override
    public void setTime(LocalTime time) {
        int minute = time.getMinute();
        int second = time.getSecond();

        angle = minute * 6.0 + second * 0.1; // 6° na minutę + 0.1° na sekundę
    }

    /**
     * Zwraca znacznik SVG wskazówki minutowej.
     * Wskazówka jest linią od środka tarczy (100,100) do długości 70,
     * o grubości 2 i czarnym kolorze.
     * @return łańcuch znaków SVG
     */
    @Override
    public String toSvg() {
        return String.format(
                "<line x1=\"100\" y1=\"100\" x2=\"100\" y2=\"30\" stroke=\"black\" stroke-width=\"2\" " +
                        "transform=\"rotate(%.2f 100 100)\" />%n", angle);
    }
}

import java.time.LocalTime;

/**
 * Klasa reprezentująca wskazówkę sekundową zegara tarczowego.
 */
public class SecondHand extends ClockHand {

    // Kąt wskazówki sekundowej w stopniach (0° = 12:00:00)
    private double angle;

    /**
     * Ustawia kąt wskazówki sekundowej na podstawie sekund czasu.
     * Ruch jest skokowy, co sekundę przeskakuje o 6 stopni (360° / 60 sekund).
     * @param time czas lokalny
     */
    @Override
    public void setTime(LocalTime time) {
        int seconds = time.getSecond();
        angle = seconds * 6.0; // 6 stopni na sekundę
    }

    /**
     * Zwraca znacznik SVG wskazówki sekundowej.
     * Wskazówka jest linią od środka tarczy (100,100) do długości 85,
     * o grubości 1 i czerwonym kolorze.
     * @return łańcuch znaków SVG
     */
    @Override
    public String toSvg() {
        // Transformacja rotate wokół punktu (100,100)
        return String.format(
                "<line x1=\"100\" y1=\"100\" x2=\"100\" y2=\"15\" stroke=\"red\" stroke-width=\"1\" " +
                        "transform=\"rotate(%.2f 100 100)\" />%n", angle);
    }
}

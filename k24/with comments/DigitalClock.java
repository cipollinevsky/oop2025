import java.time.LocalTime;

/**
 * Zegar cyfrowy z trybem 12- lub 24-godzinnym.
 */
public class DigitalClock extends Clock {

    // Typ wyliczeniowy trybu zegara
    public enum Mode {
        H24, // tryb 24-godzinny
        H12  // tryb 12-godzinny
    }

    private final Mode mode;

    /**
     * Konstruktor zegara cyfrowego z podanym miastem i trybem.
     * @param city miasto
     * @param mode tryb zegara (12- lub 24-godzinny)
     */
    public DigitalClock(City city, Mode mode) {
        super(city);
        this.mode = mode;
    }

    /**
     * Zwraca czas w formacie w zależności od trybu.
     * - W trybie 24-godzinnym wywołuje metodę toString z klasy nadrzędnej.
     * - W trybie 12-godzinnym formatuje czas jako h:mm:ss AM/PM,
     *   bez zer wiodących w godzinie.
     * @return sformatowany czas
     */
    @Override
    public String toString() {
        if (mode == Mode.H24) {
            // Używamy formatowania klasy Clock (hh:mm:ss)
            return super.toString();
        } else {
            // Tryb 12-godzinny
            int hour = getHour();
            int minute = getMinute();
            int second = getSecond();

            // Konwersja na 12h format
            String ampm = (hour < 12) ? "AM" : "PM";
            int hour12 = hour % 12;
            if (hour12 == 0) {
                hour12 = 12; // godzina 0 to 12 AM/PM
            }

            // Formatowanie z jednocyfrową godziną (bez zer wiodących)
            return String.format("%d:%02d:%02d %s", hour12, minute, second, ampm);
        }
    }
}

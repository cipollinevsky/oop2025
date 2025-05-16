import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Abstrakcyjna klasa reprezentująca zegar.
 */
public abstract class Clock {

    // Aktualny czas w zegarze
    private int hour;
    private int minute;
    private int second;
    private LocalTime time;

    // Miasto, z którym powiązany jest zegar
    private City city;

    /**
     * Konstruktor tworzący zegar powiązany z podanym miastem.
     * @param city miasto, dla którego ustawiany jest czas
     */
    public Clock(City city) {
        this.city = city;
        setCurrentTime();
    }

    /**
     * Ustawia czas zegara na aktualny czas systemowy w strefie czasowej miasta.
     */
    public void setCurrentTime() {
        // Pobranie strefy czasowej miasta
        ZoneId zone = city.getZoneId();

        // Aktualny czas w strefie czasowej miasta
        LocalTime localTime = ZonedDateTime.now(zone).toLocalTime();

        // Ustawienie czasu w zegarze
        setTime(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
    }

    /**
     * Ustawia czas zegara na zadany.
     * Rzuca IllegalArgumentException, jeśli parametry są poza zakresem.
     * @param hour godzina [0-23]
     * @param minute minuta [0-59]
     * @param second sekunda [0-59]
     */
    public void setTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23)
            throw new IllegalArgumentException("Nieprawidłowa godzina: " + hour);
        if (minute < 0 || minute > 59)
            throw new IllegalArgumentException("Nieprawidłowa minuta: " + minute);
        if (second < 0 || second > 59)
            throw new IllegalArgumentException("Nieprawidłowa sekunda: " + second);

        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Zwraca czas zegara w formacie hh:mm:ss.
     * @return czas jako tekst
     */
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    /**
     * Zwraca referencję do obiektu City powiązanego z zegarem.
     * @return obiekt City
     */
    public City getCity() {
        return city;
    }

    /**
     * Zmienia miasto zegara na nowe i przesuwa czas zgodnie z różnicą
     * między strefami czasowymi starym i nowym miasta.
     * @param newCity nowe miasto
     */
    public void setCity(City newCity) {
        if (newCity == null) {
            throw new IllegalArgumentException("Miasto nie może być null");
        }
        // Obliczenie różnicy w godzinach między strefami
        ZoneId oldZone = city.getZoneId();
        ZoneId newZone = newCity.getZoneId();

        // Aktualny czas w starej strefie
        ZonedDateTime oldTime = ZonedDateTime.now(oldZone);

        // Przesunięcie czasu do nowej strefy
        ZonedDateTime newTime = oldTime.withZoneSameInstant(newZone);

        // Ustaw nowy czas i miasto
        setTime(newTime.getHour(), newTime.getMinute(), newTime.getSecond());
        city = newCity;
    }

    // Gettery na czas (jeśli potrzebne)
    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }
    public int getSecond() {
        return second;
    }

    protected LocalTime getTime() {
        return this.time;
    }

}

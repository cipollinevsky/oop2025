import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;

public class AnalogClock extends Clock {

    // Finalna lista wskazówek, polimorficznie przechowujemy wszystkie trzy
    private final List<ClockHand> hands;

    // Konstruktor - wymaga City dla konstruktora Clock, więc tworzymy przeciążony
    public AnalogClock() {
        super(null);  // domyślnie bez miasta, można ustawić potem setCity()
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
        // Aktualizacja wskazówek wg aktualnego czasu w Clock (krok 4 - setCity i setTime)
        updateHands();
    }

    /**
     * Metoda nadpisująca setTime z klasy Clock (opcjonalnie, ale można w tym kroku zostawić bez nadpisywania)
     * Jednak jeśli chcesz, by wskazówki zmieniały się automatycznie po wywołaniu setTime lub setCurrentTime
     * można użyć metody updateHands() w setTime i setCurrentTime lub w analogiczny sposób
     * albo zastosować wzorzec obserwatora (Observer) - ale tu proste rozwiązanie poniżej.
     */

    // Metoda aktualizująca pozycję wskazówek wg wewnętrznego czasu Clock
    private void updateHands() {
        LocalTime time = getTime();
        for (ClockHand hand : hands) {
            hand.setTime(time);
        }
    }

    protected LocalTime getTime() {
        // Jeśli pole 'time' jest prywatne w Clock, potrzebujemy metody protected lub public getter w Clock,
        // np. protected LocalTime getTime()
        return super.getTime();
    }

    /**
     * Przykładowa metoda setTime (jeśli nie nadpisujemy, można wywołać updateHands() po ustawieniu czasu)
     * W zależności od implementacji Clock (jeśli setTime jest finalne lub nie)
     */
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

    /**
     * Zwraca SVG tarczy wraz ze wskazówkami i zapisuje do pliku.
     * @param path ścieżka pliku SVG
     */
    public void toSvg(String path) throws IOException {
        StringBuilder svg = new StringBuilder();

        // Nagłówek SVG (np. 200x200, centrum w 100,100)
        svg.append("""
            <svg width="200" height="200" xmlns="http://www.w3.org/2000/svg" version="1.1">
            <circle cx="100" cy="100" r="90" stroke="black" stroke-width="4" fill="white"/>
            """);

        // Dodajemy oznaczenia godzin (np. kreski co 30 stopni)
        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30);
            double x1 = 100 + Math.sin(angle) * 80;
            double y1 = 100 - Math.cos(angle) * 80;
            double x2 = 100 + Math.sin(angle) * 90;
            double y2 = 100 - Math.cos(angle) * 90;
            svg.append(String.format("<line x1=\"%.1f\" y1=\"%.1f\" x2=\"%.1f\" y2=\"%.1f\" stroke=\"black\" stroke-width=\"2\" />%n", x1, y1, x2, y2));
        }

        // Dodajemy wskazówki
        for (ClockHand hand : hands) {
            svg.append(hand.toSvg());
        }

        // Koniec SVG
        svg.append("</svg>");

        // Zapis do pliku
        Files.writeString(Path.of(path), svg.toString());
    }
}

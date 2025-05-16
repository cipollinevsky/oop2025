import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class City {

    private final String name;
    private final String country;
    private final int timezoneOffset;   // przesunięcie strefy czasowej względem UTC (np. 2)
    private final double longitude;     // długość geograficzna w stopniach
    private final double latitude;      // szerokość geograficzna
    private final ZoneId zoneId;

    // Konstruktor
    public City(String name, String country, int timezoneOffset, double longitude, double latitude) {
        this.name = name;
        this.country = country;
        this.timezoneOffset = timezoneOffset;
        this.longitude = longitude;
        this.latitude = latitude;
        // Budujemy ZoneId np. "UTC+02:00"
        this.zoneId = ZoneId.ofOffset("UTC", java.time.ZoneOffset.ofHours(timezoneOffset));
    }

    public static void generateAnalogClocksSvg(List<City> cityList, AnalogClock analogClock) throws IOException {
        // Nazwa katalogu to wynik metody toString() analogClock (np. "AnalogClock{...}")
        String dirName = analogClock.toString();

        Path dirPath = Paths.get(dirName);

        // Tworzymy katalog, jeśli nie istnieje
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // Dla każdego miasta z listy:
        for (City city : cityList) {
            // Ustawiamy miasto zegara
            analogClock.setCity(city);

            // Generujemy nazwę pliku SVG (np. "Warszawa.svg")
            String fileName = city.getName().replaceAll("\\s+", "_") + ".svg";
            Path filePath = dirPath.resolve(fileName);

            // Zapisujemy do pliku wynik metody toSvg
            analogClock.toSvg(filePath.toString());
        }
    }

    // Gettery
    public String getName() {
        return name;
    }
    public String getCountry() {
        return country;
    }
    public int getTimezoneOffset() {
        return timezoneOffset;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public ZoneId getZoneId() {
        return zoneId;
    }

    /**
     * Prywatna metoda parsująca linię z pliku i tworząca obiekt City.
     * Załóżmy, że linia ma format CSV: name,country,timezoneOffset,longitude,latitude
     * @param line pojedyncza linia z pliku
     * @return obiekt City
     */
    private static City parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Nieprawidłowy format linii: " + line);
        }
        String name = parts[0].trim();
        String country = parts[1].trim();
        int timezoneOffset = Integer.parseInt(parts[2].trim());
        double longitude = Double.parseDouble(parts[3].trim());
        double latitude = Double.parseDouble(parts[4].trim());

        return new City(name, country, timezoneOffset, longitude, latitude);
    }

    /**
     * Publiczna metoda czytająca plik i zwracająca mapę (nazwa miasta -> obiekt City)
     * @param path ścieżka do pliku
     * @return mapa miast
     * @throws IOException w razie błędu odczytu
     */
    public static Map<String, City> parseFile(String path) throws IOException {
        Map<String, City> cityMap = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                City city = parseLine(line);
                cityMap.put(city.getName(), city);
            }
        }
        return cityMap;
    }

    /**
     * Metoda zwracająca lokalny czas średni (LMT) na podstawie czasu strefowego i długości geograficznej.
     * Przesunięcie czasu w sekundach to (longitude / 15) * 3600
     * @param zoneTime czas według strefy czasowej
     * @return lokalny czas średni (LMT)
     */
    public LocalTime localMeanTime(LocalTime zoneTime) {
        // przesunięcie w sekundach
        double offsetSeconds = (longitude / 15.0) * 3600.0;

        // Zamiana na całkowitą liczbę sekund (możemy zaokrąglić)
        int offsetSec = (int) Math.round(offsetSeconds);

        // Dodajemy przesunięcie sekund do czasu strefowego
        int totalSeconds = zoneTime.toSecondOfDay() + offsetSec;

        // Normalizacja (czas w zakresie 0-86399 sekund)
        totalSeconds = ((totalSeconds % 86400) + 86400) % 86400;

        return LocalTime.ofSecondOfDay(totalSeconds);
    }

    /**
     * Statyczny komparator do sortowania miast według największej różnicy między czasem strefowym
     * a lokalnym średnim (LMT).
     * @param c1 pierwsze miasto
     * @param c2 drugie miasto
     * @return wartość komparatora
     */
    public static int worstTimezoneFit(City c1, City c2) {
        LocalTime now = LocalTime.now();

        LocalTime c1ZoneTime = now; // Zakładamy teraz jako czas strefowy (dla uproszczenia)
        LocalTime c2ZoneTime = now;

        LocalTime c1LMT = c1.localMeanTime(c1ZoneTime);
        LocalTime c2LMT = c2.localMeanTime(c2ZoneTime);

        // Obliczamy różnicę w sekundach między LMT a czasem strefowym (modulo 86400)
        int diff1 = Math.abs(c1LMT.toSecondOfDay() - c1ZoneTime.toSecondOfDay());
        int diff2 = Math.abs(c2LMT.toSecondOfDay() - c2ZoneTime.toSecondOfDay());

        // Porównujemy różnice malejąco (większe różnice na początku)
        return Integer.compare(diff2, diff1);
    }

    @Override
    public String toString() {
        return name;
    }
}

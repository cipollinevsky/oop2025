import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            // Krok 3: Wczytujemy dane miast z pliku strefy.csv
            Map<String, City> cities = City.parseFile("strefy.csv");
            System.out.println("Wczytano miasta: " + cities.keySet());

            // Wybierzmy kilka miast do testów
            City warsaw = cities.get("Warszawa");
            City kyiv = cities.get("Kijów");
            City madrid = cities.get("Madryt");

            // Krok 4 + 1: Tworzymy DigitalClock z miastem Warszawa i trybem 24h
            DigitalClock clock = new DigitalClock(warsaw, DigitalClock.Mode.H24);

            // Ustawiamy czas systemowy
            clock.setCurrentTime();
            System.out.println("Czas w Warszawie (24h): " + clock);

            // Krok 2: Testujemy tryb 12h
            DigitalClock clock12 = new DigitalClock(warsaw, DigitalClock.Mode.H12);
            clock12.setTime(13, 5, 9);
            System.out.println("Czas w Warszawie (12h): " + clock12);

            // Krok 4: Zmiana miasta z Warszawy na Kijów (powinna zmienić czas o +1h)
            clock.setCity(kyiv);
            System.out.println("Po zmianie miasta na Kijów (24h): " + clock);

            // Krok 5: Testujemy localMeanTime na przykładzie Madrytu
            LocalTime madridZoneTime = LocalTime.of(12, 0, 0);
            LocalTime madridLMT = madrid.localMeanTime(madridZoneTime);
            System.out.println("Madryt czas strefowy: " + madridZoneTime);
            System.out.println("Madryt lokalny czas średni: " + madridLMT);

            // Krok 6: Sortujemy miasta wg worstTimezoneFit i wypisujemy nazwy
            List<City> cityList = new ArrayList<>(cities.values());
            cityList.sort(City::worstTimezoneFit);
            System.out.println("Miasta posortowane wg worstTimezoneFit:");
            cityList.forEach(c -> System.out.println("  " + c.getName()));

            // Krok 7-11: Tworzymy AnalogClock i testujemy wskazówki oraz SVG
            AnalogClock analogClock = new AnalogClock(warsaw);

            // Ustawiamy aktualny czas (to też ustawi wskazówki)
            analogClock.setCurrentTime();
            // Zapisujemy tarczę i wskazówki do SVG w pliku
            String svgPath = "analog_clock_warsaw.svg";
            analogClock.toSvg(svgPath);
            System.out.println("Zapisano analogowy zegar do pliku: " + svgPath);

            // Krok 12: Generujemy katalog i pliki SVG dla wszystkich miast na liście
            City.generateAnalogClocksSvg(cityList, analogClock);
            System.out.println("Wygenerowano pliki SVG dla miast w katalogu: " + analogClock.toString());

        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania pliku: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Inny błąd: " + e.getMessage());
        }
    }
}

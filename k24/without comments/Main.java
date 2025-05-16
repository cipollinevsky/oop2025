import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Map<String, City> cities = City.parseFile("strefy.csv");
            System.out.println("Wczytano miasta: " + cities.keySet());

            City warsaw = cities.get("Warszawa");
            City kyiv = cities.get("Kijów");
            City madrid = cities.get("Madryt");

            DigitalClock clock = new DigitalClock(warsaw, DigitalClock.Mode.H24);

            clock.setCurrentTime();
            System.out.println("Czas w Warszawie (24h): " + clock);

            DigitalClock clock12 = new DigitalClock(warsaw, DigitalClock.Mode.H12);
            clock12.setTime(13, 5, 9);
            System.out.println("Czas w Warszawie (12h): " + clock12);

            clock.setCity(kyiv);
            System.out.println("Po zmianie miasta na Kijów (24h): " + clock);

            LocalTime madridZoneTime = LocalTime.of(12, 0, 0);
            LocalTime madridLMT = madrid.localMeanTime(madridZoneTime);
            System.out.println("Madryt czas strefowy: " + madridZoneTime);
            System.out.println("Madryt lokalny czas średni: " + madridLMT);

            List<City> cityList = new ArrayList<>(cities.values());
            cityList.sort(City::worstTimezoneFit);
            System.out.println("Miasta posortowane wg worstTimezoneFit:");
            cityList.forEach(c -> System.out.println("  " + c.getName()));

            AnalogClock analogClock = new AnalogClock(warsaw);

            analogClock.setCurrentTime();
            String svgPath = "analog_clock_warsaw.svg";
            analogClock.toSvg(svgPath);
            System.out.println("Zapisano analogowy zegar do pliku: " + svgPath);
            
            City.generateAnalogClocksSvg(cityList, analogClock);
            System.out.println("Wygenerowano pliki SVG dla miast w katalogu: " + analogClock.toString());

        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania pliku: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Inny błąd: " + e.getMessage());
        }
    }
}

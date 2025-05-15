import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CountryWithoutProvinces extends Country {
    // Struktura przechowująca dane dzienne: data -> obiekt z zakażeniami i zgonami
    private Map<LocalDate, DailyStat> dailyStatistics;

    public CountryWithoutProvinces(String name) {
        super(name);
    }

    // Metoda dodająca statystyki - powinna już istnieć wg poprzednich kroków
    public void addDailyStatistic(LocalDate date, int confirmedCases, int deaths) {
        dailyStatistics.put(date, new DailyStat(confirmedCases, deaths));
    }

    @Override
    public int getConfirmedCases(LocalDate date) {
        DailyStat stat = dailyStatistics.get(date);
        // Zakładamy poprawność daty, więc stat nie powinien być null
        return stat != null ? stat.confirmedCases : 0;
    }

    @Override
    public int getDeaths(LocalDate date) {
        DailyStat stat = dailyStatistics.get(date);
        return stat != null ? stat.deaths : 0;
    }

    // Klasa pomocnicza do przechowywania danych dziennych
    private static class DailyStat {
        final int confirmedCases;
        final int deaths;

        DailyStat(int confirmedCases, int deaths) {
            this.confirmedCases = confirmedCases;
            this.deaths = deaths;
        }
    }

    @Override
    public Set<LocalDate> getAllDates() {
        // Zwracamy wszystkie klucze mapy - daty, dla których mamy statystyki
        return dailyStatistics.keySet();
    }
}

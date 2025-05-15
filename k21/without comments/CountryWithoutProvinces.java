import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class CountryWithoutProvinces extends Country {
    private Map<LocalDate, DailyStat> dailyStatistics;

    public CountryWithoutProvinces(String name) {
        super(name);
    }

    public void addDailyStatistic(LocalDate date, int confirmedCases, int deaths) {
        dailyStatistics.put(date, new DailyStat(confirmedCases, deaths));
    }

    @Override
    public int getConfirmedCases(LocalDate date) {
        DailyStat stat = dailyStatistics.get(date);
        return stat != null ? stat.confirmedCases : 0;
    }

    @Override
    public int getDeaths(LocalDate date) {
        DailyStat stat = dailyStatistics.get(date);
        return stat != null ? stat.deaths : 0;
    }

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
        return dailyStatistics.keySet();
    }
}

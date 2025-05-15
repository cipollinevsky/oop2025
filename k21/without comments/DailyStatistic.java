public class DailyStatistic {
    private final int confirmedCases;
    private final int deaths;

    public DailyStatistic(int confirmedCases, int deaths) {
        this.confirmedCases = confirmedCases;
        this.deaths = deaths;
    }

    public int getConfirmedCases() {
        return confirmedCases;
    }

    public int getDeaths() {
        return deaths;
    }
}

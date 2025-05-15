import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class CountryWithProvinces extends Country {
    private Country[] provinces;

    public CountryWithProvinces(String name, Country[] provinces) {
        super(name);
        this.provinces = provinces;
    }

    public Country[] getProvinces() {
        return provinces;
    }

    @Override
    public int getConfirmedCases(LocalDate date) {
        int sum = 0;
        for (Country province : provinces) {
            sum += province.getConfirmedCases(date);
        }
        return sum;
    }

    @Override
    public int getDeaths(LocalDate date) {
        int sum = 0;
        for (Country province : provinces) {
            sum += province.getDeaths(date);
        }
        return sum;
    }

    @Override
    public Set<LocalDate> getAllDates() {
        Set<LocalDate> allDates = new HashSet<>();
        // Zbieramy wszystkie daty z każdej prowincji
        for (Country province : provinces) {
            allDates.addAll(province.getAllDates());
        }
        return allDates;
    }
}

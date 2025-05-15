import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Ustawiamy ścieżki do plików CSV z danymi
            // Podmień ścieżki na rzeczywiste pliki na twoim dysku
            String confirmedCasesFile = "confirmed_cases.csv";
            String deathsFile = "deaths.csv";
            Country.setFiles(confirmedCasesFile, deathsFile);

            // 2. Lista nazw krajów do wczytania
            String[] countriesToLoad = {"Afghanistan", "Australia", "Poland", "NonexistentCountry"};

            // 3. Wczytujemy kraje z pliku
            Country[] countries = Country.fromCsv(countriesToLoad);

            // 4. Wyświetlamy podstawowe dane dla wczytanych krajów
            for (Country c : countries) {
                System.out.println("Country: " + c.getName());
                // Przykładowo: liczba zakażeń i zgonów w dniu 2021-03-01
                LocalDate sampleDate = LocalDate.of(2021, 3, 1);
                System.out.println("Confirmed cases on " + sampleDate + ": " + c.getConfirmedCases(sampleDate));
                System.out.println("Deaths on " + sampleDate + ": " + c.getDeaths(sampleDate));
                System.out.println();
            }

            // 5. Sortujemy kraje malejąco wg liczby zgonów w okresie 2021-02-01 do 2021-04-01
            LocalDate startDate = LocalDate.of(2021, 2, 1);
            LocalDate endDate = LocalDate.of(2021, 4, 1);
            List<Country> countryList = new ArrayList<>(List.of(countries));
            Country.sortByDeaths(countryList, startDate, endDate);

            System.out.println("Countries sorted by deaths from " + startDate + " to " + endDate + ":");
            for (Country c : countryList) {
                System.out.println(c.getName());
            }
            System.out.println();

            // 6. Zapisujemy dane dla każdego kraju do pliku
            for (Country c : countries) {
                String outputFile = c.getName() + "_data.txt";
                c.saveToDataFile(outputFile);
                System.out.println("Saved data for " + c.getName() + " to file: " + outputFile);
            }

        } catch (FileNotFoundException e) {
            System.err.println("File error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

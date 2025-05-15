import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            String confirmedCasesFile = "confirmed_cases.csv";
            String deathsFile = "deaths.csv";
            Country.setFiles(confirmedCasesFile, deathsFile);

            String[] countriesToLoad = {"Afghanistan", "Australia", "Poland", "NonexistentCountry"};

            Country[] countries = Country.fromCsv(countriesToLoad);

            for (Country c : countries) {
                System.out.println("Country: " + c.getName());
                LocalDate sampleDate = LocalDate.of(2021, 3, 1);
                System.out.println("Confirmed cases on " + sampleDate + ": " + c.getConfirmedCases(sampleDate));
                System.out.println("Deaths on " + sampleDate + ": " + c.getDeaths(sampleDate));
                System.out.println();
            }

            LocalDate startDate = LocalDate.of(2021, 2, 1);
            LocalDate endDate = LocalDate.of(2021, 4, 1);
            List<Country> countryList = new ArrayList<>(List.of(countries));
            Country.sortByDeaths(countryList, startDate, endDate);

            System.out.println("Countries sorted by deaths from " + startDate + " to " + endDate + ":");
            for (Country c : countryList) {
                System.out.println(c.getName());
            }
            System.out.println();
            
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

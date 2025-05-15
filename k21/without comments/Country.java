import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class Country {
    private final String name;
    private static String confirmedCasesFilePath;
    private static String deathsFilePath;

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static void setFiles(String confirmedFilePath, String deathsFilePath) throws FileNotFoundException {
        File confirmedFile = new File(confirmedFilePath);
        File deathsFile = new File(deathsFilePath);

        if (!confirmedFile.exists() || !confirmedFile.canRead()) {
            throw new FileNotFoundException(confirmedFilePath);
        }
        if (!deathsFile.exists() || !deathsFile.canRead()) {
            throw new FileNotFoundException(deathsFilePath);
        }

        Country.confirmedCasesFilePath = confirmedFilePath;
        Country.deathsFilePath = deathsFilePath;
    }

    public static Country fromCsv(String countryName) throws CountryNotFoundException {
        try (
                Scanner scannerConfirmed = new Scanner(new File(confirmedCasesFilePath));
                Scanner scannerDeaths = new Scanner(new File(deathsFilePath))
        ) {
            String[] headerCountries = scannerConfirmed.nextLine().split(";");
            String[] headerProvinces = scannerConfirmed.nextLine().split(";");

            CountryColumns columns = getCountryColumns(headerCountries, countryName);

            Country country;

            boolean hasProvinces = !(columns.columnCount == 1 && headerProvinces[columns.firstColumnIndex].equals("nan"));

            if (!hasProvinces) {
                country = new CountryWithoutProvinces(countryName);
            } else {
                Country[] provinces = new Country[columns.columnCount];
                for (int i = 0; i < columns.columnCount; i++) {
                    provinces[i] = new CountryWithoutProvinces(headerProvinces[columns.firstColumnIndex + i]);
                }
                country = new CountryWithProvinces(countryName, provinces);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");

            while (scannerConfirmed.hasNextLine() && scannerDeaths.hasNextLine()) {
                String[] confirmedLine = scannerConfirmed.nextLine().split(";");
                String[] deathsLine = scannerDeaths.nextLine().split(";");

                LocalDate date = LocalDate.parse(confirmedLine[0], formatter);

                if (!hasProvinces) {
                    int confirmedCases = Integer.parseInt(confirmedLine[columns.firstColumnIndex]);
                    int deaths = Integer.parseInt(deathsLine[columns.firstColumnIndex]);

                    ((CountryWithoutProvinces) country).addDailyStatistic(date, confirmedCases, deaths);

                } else {
                    CountryWithProvinces cwp = (CountryWithProvinces) country;
                    for (int i = 0; i < columns.columnCount; i++) {
                        int confirmedCases = Integer.parseInt(confirmedLine[columns.firstColumnIndex + i]);
                        int deaths = Integer.parseInt(deathsLine[columns.firstColumnIndex + i]);

                        CountryWithoutProvinces province = (CountryWithoutProvinces) cwp.getProvinces()[i];
                        province.addDailyStatistic(date, confirmedCases, deaths);
                    }
                }
            }

            return country;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class CountryColumns {
        public final int firstColumnIndex;
        public final int columnCount;

        public CountryColumns(int firstColumnIndex, int columnCount) {
            this.firstColumnIndex = firstColumnIndex;
            this.columnCount = columnCount;
        }
    }

    private static CountryColumns getCountryColumns(String[] headerLine, String countryName) throws CountryNotFoundException {
        int firstIndex = -1;
        int count = 0;

        for (int i = 0; i < headerLine.length; i++) {
            if (headerLine[i].equals(countryName)) {
                if (firstIndex == -1) {
                    firstIndex = i;
                }
                count++;
            }
        }

        if (count == 0) {
            throw new CountryNotFoundException(countryName);
        }

        return new CountryColumns(firstIndex, count);
    }

    public static Country[] fromCsv(String[] countryNames) {
        List<Country> countriesList = new ArrayList<>();

        for (String countryName : countryNames) {
            try {
                Country country = fromCsv(countryName);
                countriesList.add(country);
            } catch (CountryNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        return countriesList.toArray(new Country[0]);
    }

    public abstract int getConfirmedCases(LocalDate date);

    public abstract int getDeaths(LocalDate date);

    public static void sortByDeaths(List<Country> countries, LocalDate startDate, LocalDate endDate) {
        Comparator<Country> deathsComparator = (c1, c2) -> {
            int deathsC1 = sumDeathsInPeriod(c1, startDate, endDate);
            int deathsC2 = sumDeathsInPeriod(c2, startDate, endDate);

            return Integer.compare(deathsC2, deathsC1);
        };

        countries.sort(deathsComparator);
    }

    private static int sumDeathsInPeriod(Country country, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            sum += country.getDeaths(date);
        }
        return sum;
    }

    public abstract Set<LocalDate> getAllDates();

    public void saveToDataFile(String outputPath) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yy");

        Set<LocalDate> datesSet = getAllDates();
        List<LocalDate> datesList = new ArrayList<>(datesSet);
        Collections.sort(datesList);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (LocalDate date : datesList) {
                int confirmed = getConfirmedCases(date);
                int deaths = getDeaths(date);

                String line = date.format(formatter) + "\t" + confirmed + "\t" + deaths;

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

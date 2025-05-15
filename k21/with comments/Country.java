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

    /**
     * Metoda tworzy i wypełnia dany kraj danymi z pliku confirmedCasesFilePath.
     * Rzuca CountryNotFoundException, gdy kraj nie istnieje w pliku.
     */
    public static Country fromCsv(String countryName) throws CountryNotFoundException {
        try (
                Scanner scannerConfirmed = new Scanner(new File(confirmedCasesFilePath));
                Scanner scannerDeaths = new Scanner(new File(deathsFilePath))
        ) {
            // Wczytanie nagłówków (pierwsze 2 wiersze)
            String[] headerCountries = scannerConfirmed.nextLine().split(";");
            String[] headerProvinces = scannerConfirmed.nextLine().split(";");

            // Pobranie kolumn dotyczących danego kraju (początek + ilość)
            CountryColumns columns = getCountryColumns(headerCountries, countryName);

            Country country;

            // Sprawdzenie czy mamy prowincje czy "nan"
            boolean hasProvinces = !(columns.columnCount == 1 && headerProvinces[columns.firstColumnIndex].equals("nan"));

            if (!hasProvinces) {
                // Kraj bez prowincji
                country = new CountryWithoutProvinces(countryName);
            } else {
                // Kraj z prowincjami - tworzymy tablicę dla prowincji
                Country[] provinces = new Country[columns.columnCount];
                for (int i = 0; i < columns.columnCount; i++) {
                    provinces[i] = new CountryWithoutProvinces(headerProvinces[columns.firstColumnIndex + i]);
                }
                country = new CountryWithProvinces(countryName, provinces);
            }

            // Format daty w pliku: M/d/yy (np. 1/22/20)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");

            // Teraz przejdź po kolejnych wierszach danych (od trzeciego wiersza)
            while (scannerConfirmed.hasNextLine() && scannerDeaths.hasNextLine()) {
                String[] confirmedLine = scannerConfirmed.nextLine().split(";");
                String[] deathsLine = scannerDeaths.nextLine().split(";");

                // Parsowanie daty z pierwszej kolumny
                LocalDate date = LocalDate.parse(confirmedLine[0], formatter);

                if (!hasProvinces) {
                    // Tylko jedna kolumna z danymi (dla kraju)
                    int confirmedCases = Integer.parseInt(confirmedLine[columns.firstColumnIndex]);
                    int deaths = Integer.parseInt(deathsLine[columns.firstColumnIndex]);

                    // Wywołujemy metodę z klasy CountryWithoutProvinces
                    ((CountryWithoutProvinces) country).addDailyStatistic(date, confirmedCases, deaths);

                } else {
                    // Kraj z prowincjami - aktualizujemy statystyki dla każdej prowincji
                    CountryWithProvinces cwp = (CountryWithProvinces) country;
                    for (int i = 0; i < columns.columnCount; i++) {
                        int confirmedCases = Integer.parseInt(confirmedLine[columns.firstColumnIndex + i]);
                        int deaths = Integer.parseInt(deathsLine[columns.firstColumnIndex + i]);

                        // Prowincja jest CountryWithoutProvinces (zgodnie z krokiem 1)
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

    // Wewnętrzna klasa CountryColumns oraz getCountryColumns pozostają bez zmian (jak w Kroku 3)

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

    /**
     * Przeciążona wersja fromCsv - przyjmuje tablicę nazw krajów i zwraca tablicę obiektów Country.
     * Pomija kraje, które nie zostały znalezione i wyświetla ich nazwy na standardowe wyjście.
     *
     * @param countryNames tablica nazw krajów do załadowania
     * @return tablica obiektów Country dla znalezionych krajów
     */
    public static Country[] fromCsv(String[] countryNames) {
        // Lista dynamiczna do przechowania poprawnie załadowanych krajów
        List<Country> countriesList = new ArrayList<>();

        for (String countryName : countryNames) {
            try {
                // Próba załadowania kraju przez metodę z pojedynczą nazwą
                Country country = fromCsv(countryName);
                countriesList.add(country);
            } catch (CountryNotFoundException e) {
                // W przypadku braku kraju wyświetlamy jego nazwę (getMessage() zwraca nazwę kraju)
                System.out.println(e.getMessage());
                // Pomiń ten kraj, czyli nic więcej nie robimy w pętli dla tego elementu
            }
        }

        // Konwersja listy na tablicę Country[]
        return countriesList.toArray(new Country[0]);
    }

    /**
     * Zwraca liczbę potwierdzonych przypadków dla podanej daty.
     * Metoda abstrakcyjna, implementowana w klasach dziedziczących.
     *
     * @param date data, dla której pobieramy dane
     * @return liczba potwierdzonych przypadków
     */
    public abstract int getConfirmedCases(LocalDate date);

    /**
     * Zwraca liczbę zgonów dla podanej daty.
     * Metoda abstrakcyjna, implementowana w klasach dziedziczących.
     *
     * @param date data, dla której pobieramy dane
     * @return liczba zgonów
     */
    public abstract int getDeaths(LocalDate date);

    /**
     * Sortuje listę krajów malejąco według sumy zgonów w okresie od startDate do endDate (włącznie).
     *
     * @param countries lista krajów do posortowania
     * @param startDate data początkowa zakresu
     * @param endDate data końcowa zakresu
     */
    public static void sortByDeaths(List<Country> countries, LocalDate startDate, LocalDate endDate) {
        // Comparator porównujący sumę zgonów dla dwóch krajów w podanym okresie
        Comparator<Country> deathsComparator = (c1, c2) -> {
            // Obliczamy sumę zgonów w okresie dla c1
            int deathsC1 = sumDeathsInPeriod(c1, startDate, endDate);
            // Obliczamy sumę zgonów w okresie dla c2
            int deathsC2 = sumDeathsInPeriod(c2, startDate, endDate);

            // Sortujemy malejąco: większa suma powinna być "mniejsza" w porządku sortowania
            return Integer.compare(deathsC2, deathsC1);
        };

        // Sortujemy listę za pomocą powyższego komparatora
        countries.sort(deathsComparator);
    }

    /**
     * Pomocnicza metoda sumująca liczbę zgonów w danym kraju w zadanym okresie dat.
     *
     * @param country kraj, którego dane sumujemy
     * @param startDate data początkowa
     * @param endDate data końcowa
     * @return suma zgonów w tym okresie
     */
    private static int sumDeathsInPeriod(Country country, LocalDate startDate, LocalDate endDate) {
        int sum = 0;
        // Iterujemy po kolejnych dniach od startDate do endDate (włącznie)
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            sum += country.getDeaths(date);
        }
        return sum;
    }

    /**
     * Zwraca wszystkie dostępne daty dla tego kraju (lub jego prowincji).
     *
     * @return zbiór dat (LocalDate)
     */
    public abstract Set<LocalDate> getAllDates();

    // Metoda saveToDataFile poniżej

    public void saveToDataFile(String outputPath) {
        // Formatter do formatu d.MM.yy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yy");

        // Pobieramy wszystkie daty i sortujemy je rosnąco
        Set<LocalDate> datesSet = getAllDates();
        List<LocalDate> datesList = new ArrayList<>(datesSet);
        Collections.sort(datesList);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            for (LocalDate date : datesList) {
                // Pobieramy dane z obecnych metod
                int confirmed = getConfirmedCases(date);
                int deaths = getDeaths(date);

                // Budujemy linię: data \t zakażenia \t zgony
                String line = date.format(formatter) + "\t" + confirmed + "\t" + deaths;

                // Zapis do pliku
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            // Można dodać obsługę błędów (np. wyrzucić RuntimeException lub logować)
            e.printStackTrace();
        }
    }
}

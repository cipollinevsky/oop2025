import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class FoodProduct extends Product {
    // Lista województw w kolejności takiej, jak w nagłówku CSV
    private final List<String> provinces;

    // Lista cen – każda linia to ceny z jednego miesiąca w kolejnych województwach
    private final List<Double[]> monthlyPrices;

    // Prywatny konstruktor – tworzy obiekt na podstawie nazwy, listy województw i danych cenowych
    private FoodProduct(String name, List<String> provinces, List<Double[]> monthlyPrices) {
        super(name);
        this.provinces = provinces;
        this.monthlyPrices = monthlyPrices;
    }

    // Statyczna metoda wytwórcza – wczytuje dane z pliku CSV i tworzy obiekt klasy FoodProduct
    public static FoodProduct fromCsv(Path path) {
        try (Scanner scanner = new Scanner(path)) {
            // Pierwsza linia: nazwa produktu (np. "CHLEB")
            String name = scanner.nextLine();

            // Druga linia: nagłówki województw oddzielone średnikami
            List<String> provinces = Arrays.asList(scanner.nextLine().split(";"));

            // Wczytujemy pozostałe linie – ceny dla każdego miesiąca
            List<Double[]> monthlyPrices = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Double[] prices = Arrays.stream(line.split(";"))
                        .map(value -> value.replace(",", "."))
                        .map(Double::valueOf)
                        .toArray(Double[]::new);

                // Sprawdzamy, czy ilość cen odpowiada liczbie województw
                if (prices.length != provinces.size()) {
                    throw new RuntimeException("Niepoprawna liczba cen w wierszu");
                }

                monthlyPrices.add(prices);
            }

            return new FoodProduct(name, provinces, monthlyPrices);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Zwraca cenę dla konkretnego województwa w danym miesiącu i roku
    public double getPrice(int year, int month, String province) {
        // Sprawdzenie poprawności miesiąca
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Miesiąc musi być w zakresie 1–12");
        }

        // Wyznaczenie indeksu miesiąca na podstawie daty
        int index = (year - 2010) * 12 + (month - 1);

        // Sprawdzenie zakresu dat
        if (index < 0 || index >= monthlyPrices.size()) {
            throw new IndexOutOfBoundsException("Data poza zakresem 01.2010–03.2022");
        }

        // Sprawdzenie, czy województwo istnieje
        int provinceIndex = provinces.indexOf(province);
        if (provinceIndex == -1) {
            throw new IndexOutOfBoundsException("Nieznane województwo: " + province);
        }

        return monthlyPrices.get(index)[provinceIndex];
    }

    // Nadpisanie metody getPrice z klasy Product
    // Oblicza średnią cenę ze wszystkich województw
    @Override
    public double getPrice(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Miesiąc musi być w zakresie 1–12");
        }

        int index = (year - 2010) * 12 + (month - 1);
        if (index < 0 || index >= monthlyPrices.size()) {
            throw new IndexOutOfBoundsException("Data poza zakresem 01.2010–03.2022");
        }

        Double[] prices = monthlyPrices.get(index);
        return Arrays.stream(prices).mapToDouble(Double::doubleValue).average().orElseThrow();
    }
}

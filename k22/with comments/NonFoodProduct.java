import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class NonFoodProduct extends Product {
    // Tablica przechowująca ceny produktu w kolejnych miesiącach
    private Double[] prices;

    // Konstruktor – wywołuje konstruktor klasy bazowej Product
    private NonFoodProduct(String name, Double[] prices) {
        super(name);
        this.prices = prices;
    }

    // Statyczna metoda tworząca obiekt na podstawie pliku CSV
    public static NonFoodProduct fromCsv(Path path) {
        String name;
        Double[] prices;

        try {
            Scanner scanner = new Scanner(path);

            name = scanner.nextLine();     // Pierwsza linia: nazwa produktu
            scanner.nextLine();            // Druga linia: nagłówki (ignorowane)

            prices = Arrays.stream(scanner.nextLine().split(";"))
                    .map(value -> value.replace(",", "."))
                    .map(Double::valueOf)
                    .toArray(Double[]::new);

            scanner.close();

            return new NonFoodProduct(name, prices);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Implementacja metody z klasy bazowej
    @Override
    public double getPrice(int year, int month) {
        // Sprawdzamy poprawność miesiąca
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Month must be between 1 and 12");
        }

        // Obliczamy indeks w tablicy na podstawie roku i miesiąca
        int index = (year - 2010) * 12 + (month - 1);

        // Sprawdzamy czy indeks mieści się w zakresie danych (od stycznia 2010 do marca 2022)
        if (index < 0 || index >= prices.length) {
            throw new IndexOutOfBoundsException("Date out of range: 01.2010 to 03.2022");
        }

        return prices[index];
    }
}

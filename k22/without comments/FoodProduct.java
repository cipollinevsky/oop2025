import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class FoodProduct extends Product {
    private final List<String> provinces;

    private final List<Double[]> monthlyPrices;

    private FoodProduct(String name, List<String> provinces, List<Double[]> monthlyPrices) {
        super(name);
        this.provinces = provinces;
        this.monthlyPrices = monthlyPrices;
    }

    public static FoodProduct fromCsv(Path path) {
        try (Scanner scanner = new Scanner(path)) {
            String name = scanner.nextLine();

            List<String> provinces = Arrays.asList(scanner.nextLine().split(";"));

            List<Double[]> monthlyPrices = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                Double[] prices = Arrays.stream(line.split(";"))
                        .map(value -> value.replace(",", "."))
                        .map(Double::valueOf)
                        .toArray(Double[]::new);

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

    public double getPrice(int year, int month, String province) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Miesiąc musi być w zakresie 1–12");
        }

        int index = (year - 2010) * 12 + (month - 1);

        if (index < 0 || index >= monthlyPrices.size()) {
            throw new IndexOutOfBoundsException("Data poza zakresem 01.2010–03.2022");
        }

        int provinceIndex = provinces.indexOf(province);
        if (provinceIndex == -1) {
            throw new IndexOutOfBoundsException("Nieznane województwo: " + province);
        }

        return monthlyPrices.get(index)[provinceIndex];
    }

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

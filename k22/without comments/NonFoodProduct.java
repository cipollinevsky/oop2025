import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class NonFoodProduct extends Product {
    private Double[] prices;

    private NonFoodProduct(String name, Double[] prices) {
        super(name);
        this.prices = prices;
    }

    public static NonFoodProduct fromCsv(Path path) {
        String name;
        Double[] prices;

        try {
            Scanner scanner = new Scanner(path);

            name = scanner.nextLine();
            scanner.nextLine();

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

    @Override
    public double getPrice(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Month must be between 1 and 12");
        }

        int index = (year - 2010) * 12 + (month - 1);

        if (index < 0 || index >= prices.length) {
            throw new IndexOutOfBoundsException("Date out of range: 01.2010 to 03.2022");
        }

        return prices[index];
    }
}

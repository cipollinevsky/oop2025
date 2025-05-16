import java.util.HashMap;
import java.util.Map;

public class Cart {
    private final Map<Product, Integer> products = new HashMap<>();

    public void addProduct(Product product, int amount) {
        if (amount <= 0) return;

        products.put(product, products.getOrDefault(product, 0) + amount);
    }

    public double getPrice(int year, int month) {
        double total = 0.0;

        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int amount = entry.getValue();
            total += product.getPrice(year, month) * amount;
        }

        return total;
    }

    public double getInflation(int year1, int month1, int year2, int month2) {
        // Sprawdzamy poprawność kolejności dat
        if (year1 > year2 || (year1 == year2 && month1 >= month2)) {
            throw new IllegalArgumentException("Data początkowa musi być wcześniejsza niż końcowa.");
        }

        double price1 = getPrice(year1, month1);
        double price2 = getPrice(year2, month2);

        int months = (year2 - year1) * 12 + (month2 - month1);

        return ((price2 - price1) / price1) * 100.0 / months * 12;
    }
}

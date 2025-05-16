import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public abstract class Product {
    private final String name;

    private static final List<Product> products = new ArrayList<>();

    protected Product(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract double getPrice(int year, int month);

    public static void clearProducts() {
        products.clear();
    }

    public static List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public static void addProducts(Function<Path, Product> factoryMethod, Path directoryPath) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*.csv")) {
            for (Path file : stream) {
                try {
                    Product product = factoryMethod.apply(file);
                    products.add(product);
                } catch (Exception e) {
                    System.err.println("Nie udało się wczytać pliku: " + file.getFileName() + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Błąd podczas odczytu katalogu: " + directoryPath, e);
        }
    }

    public static Product getProduct(String prefix) throws AmbigiousProductException {
        List<Product> matches = products.stream()
                .filter(p -> p.getName().startsWith(prefix))
                .toList();

        if (matches.isEmpty()) {
            throw new IndexOutOfBoundsException("Nie znaleziono produktu zaczynającego się na: " + prefix);
        } else if (matches.size() == 1) {
            return matches.get(0);
        } else {
            List<String> names = matches.stream().map(Product::getName).toList();
            throw new AmbigiousProductException(names);
        }
    }

}

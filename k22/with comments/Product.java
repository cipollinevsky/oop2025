import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;

public abstract class Product {
    // Nazwa produktu
    private final String name;

    // Prywatna, wspólna lista produktów (statyczna, współdzielona)
    private static final List<Product> products = new ArrayList<>();

    // Konstruktor chroniony – dostępny tylko dla klas dziedziczących
    protected Product(String name) {
        this.name = name;
    }

    // Getter do nazwy
    public String getName() {
        return name;
    }

    // Abstrakcyjna metoda – każda podklasa musi zdefiniować sposób uzyskiwania ceny
    public abstract double getPrice(int year, int month);

    // Czyści listę produktów
    public static void clearProducts() {
        products.clear();
    }

    // Zwraca niezmienną kopię listy produktów (np. do przeglądania)
    public static List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    // Dodaje produkty z katalogu przy użyciu przekazanej metody tworzącej (factory method)
    public static void addProducts(Function<Path, Product> factoryMethod, Path directoryPath) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath, "*.csv")) {
            for (Path file : stream) {
                try {
                    // Używamy przekazanej funkcji fabrykującej do utworzenia obiektu produktu
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
        // Znajdujemy wszystkie produkty, których nazwa zaczyna się od zadanego prefiksu
        List<Product> matches = products.stream()
                .filter(p -> p.getName().startsWith(prefix))
                .toList();

        if (matches.isEmpty()) {
            throw new IndexOutOfBoundsException("Nie znaleziono produktu zaczynającego się na: " + prefix);
        } else if (matches.size() == 1) {
            return matches.get(0);
        } else {
            // Zbieramy pasujące nazwy i rzucamy nasz wyjątek
            List<String> names = matches.stream().map(Product::getName).toList();
            throw new AmbigiousProductException(names);
        }
    }

}

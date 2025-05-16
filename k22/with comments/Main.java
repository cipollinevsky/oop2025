import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== KROK 1: Testowanie NonFoodProduct.getPrice ===");
        NonFoodProduct np = NonFoodProduct.fromCsv(Paths.get("data/nonfood/mydlo.csv"));
        System.out.println("Produkt: " + np.getName());
        System.out.println("Cena w 01.2010: " + np.getPrice(2010, 1) + " zł");
        System.out.println("Cena w 03.2022: " + np.getPrice(2022, 3) + " zł");

        System.out.println("\n=== KROK 2: Testowanie FoodProduct.getPrice ===");
        FoodProduct fp = FoodProduct.fromCsv(Paths.get("data/food/jablka.csv"));
        System.out.println("Produkt: " + fp.getName());
        System.out.println("Cena w MAZOWIECKIE w 01.2010: " + fp.getPrice(2010, 1, "MAZOWIECKIE") + " zł");
        System.out.println("Średnia cena w 01.2010: " + fp.getPrice(2010, 1) + " zł");

        System.out.println("\n=== KROK 3: Wczytywanie wszystkich produktów ===");
        Product.clearProducts();
        Product.addProducts(NonFoodProduct::fromCsv, Paths.get("data/nonfood"));
        Product.addProducts(FoodProduct::fromCsv, Paths.get("data/food"));
        System.out.println("Wczytano produkty.");

        System.out.println("\n=== KROK 4: Testowanie wyszukiwania po prefiksie ===");

        try {
            // jeden wynik
            Product p1 = Product.getProduct("Bu");
            System.out.println("Znaleziono jednoznacznie: " + p1.getName());
        } catch (Exception e) {
            System.err.println("Błąd: " + e.getMessage());
        }

        try {
            // brak wyniku
            Product p2 = Product.getProduct("XYZ");
            System.out.println("Znaleziono: " + p2.getName());
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Brak produktu: " + e.getMessage());
        } catch (AmbigiousProductException e) {
            System.err.println("Nieoczekiwany wyjątek: " + e.getMessage());
        }

        try {
            // wiele wyników
            Product p3 = Product.getProduct("Ja");
            System.out.println("Znaleziono: " + p3.getName());
        } catch (AmbigiousProductException e) {
            System.err.println("Wiele pasujących produktów:");
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Brak produktu: " + e.getMessage());
        }

        System.out.println("\n=== KROK 5: Testowanie Cart ===");

        Cart cart = new Cart();
        try {
            cart.addProduct(Product.getProduct("Jabłka"), 5);  // 5 kg
            cart.addProduct(Product.getProduct("Mydło"), 2);   // 2 sztuki
            cart.addProduct(Product.getProduct("Chleb"), 4);   // 4 sztuki

            double price1 = cart.getPrice(2012, 1);
            double price2 = cart.getPrice(2022, 1);
            double inflation = cart.getInflation(2012, 1, 2022, 1);

            System.out.printf("Wartość koszyka w 01.2012: %.2f zł%n", price1);
            System.out.printf("Wartość koszyka w 01.2022: %.2f zł%n", price2);
            System.out.printf("Inflacja roczna: %.2f%%%n", inflation);
        } catch (Exception e) {
            System.err.println("Błąd koszyka: " + e.getMessage());
        }
    }
}

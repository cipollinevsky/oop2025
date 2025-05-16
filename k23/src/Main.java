import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Utwórz parser mapy
            MapParser parser = new MapParser();

            // 2. Parsuj plik SVG z mapą (np. "map.svg" powinno być w katalogu projektu)
            parser.parse("map.svg");

            // 3. Dodaj miasta do lądów (krok 14)
            parser.addCitiesToLands();

            // 4. Przypisz nazwy miast z labeli (krok 15)
            parser.matchLabelsToTowns();

            // 5. Dla każdego lądu wypisz listę miast wraz z oznaczeniem portów (krok 16)
            for (Land land : parser.getLands()) {
                System.out.println(land.toString());
            }

        } catch (IOException e) {
            System.err.println("Błąd wczytywania pliku: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Błąd wykonania: " + e.getMessage());
        }
    }
}

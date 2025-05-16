import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class LandTest {

    @Test
    void testAddCityThrowsExceptionWithCorrectMessage() {
        // Definiujemy prosty ląd - np. kwadrat od (0,0) do (10,10)
        Land land = new Land(List.of(
                new Point(0, 0),
                new Point(10, 0),
                new Point(10, 10),
                new Point(0, 10)
        ));

        // Tworzymy miasto portowe, które jest "na wodzie" - poza lądem, np. środek w (15, 5)
        City portCity = new City(new Point(15, 5), "PortCity", 2.0);

        // Sprawdzamy, że dodanie miasta powoduje wyjątek RuntimeException
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            land.addCity(portCity);
        });

        // Sprawdzamy, czy komunikat wyjątku to nazwa miasta
        assertEquals("PortCity", thrown.getMessage());
    }
}

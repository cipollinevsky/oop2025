import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class PolygonTest {

    // Test: punkt leży wewnątrz wielokąta
    @Test
    public void testPointInsidePolygon() {
        List<Point> points = List.of(
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
        );
        Polygon polygon = new Polygon(points);
        Point insidePoint = new Point(2, 2); // punkt wewnątrz

        assertTrue(polygon.inside(insidePoint), "Punkt powinien być wewnątrz wielokąta");
    }

    // Test: punkt leży pod wielokątem (y jest mniejsze niż najniższy punkt wielokąta)
    @Test
    public void testPointBelowPolygon() {
        List<Point> points = List.of(
                new Point(0, 1),
                new Point(4, 1),
                new Point(4, 5),
                new Point(0, 5)
        );
        Polygon polygon = new Polygon(points);
        Point belowPoint = new Point(2, 0); // punkt poniżej wielokąta (y < 1)

        assertFalse(polygon.inside(belowPoint), "Punkt pod wielokątem nie powinien być w środku");
    }

    // Test: punkt po prawej stronie wielokąta (x jest większe niż największy x wielokąta)
    @Test
    public void testPointRightOfPolygon() {
        List<Point> points = List.of(
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
        );
        Polygon polygon = new Polygon(points);
        Point rightPoint = new Point(5, 2); // punkt po prawej (x > 4)

        assertFalse(polygon.inside(rightPoint), "Punkt po prawej stronie nie powinien być w środku");
    }
}

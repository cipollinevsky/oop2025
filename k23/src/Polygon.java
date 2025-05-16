import java.util.List;

public class Polygon {
    private final List<Point> points;  // Lista punktów

    public Polygon(List<Point> points) {
        this.points = List.copyOf(points);  // Tworzymy niemodyfikowalną kopię listy
    }

    public List<Point> getPoints() {
        return points;
    }

    public boolean inside(Point point) {
        int counter = 0;
        int n = points.size();  // Używamy size() dla List

        for (int i = 0; i < n; i++) {
            Point pa = points.get(i);          // get() z List
            Point pb = points.get((i + 1) % n);

            // Reszta kodu...
        }
        return (counter % 2) == 1;
    }
}

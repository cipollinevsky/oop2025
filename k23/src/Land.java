import java.util.ArrayList;
import java.util.List;

public class Land extends Polygon {
    private List<City> cities = new ArrayList<>();

    public Land(List<Point> points) {
        super(points);
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public List<City> getCities() {
        return cities;
    }

    @Override
    public String toString() {
        return cities.stream()
                .map(City::toString)
                .collect(Collectors.joining(", "));
    }
}

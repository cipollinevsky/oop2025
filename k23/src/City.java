import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class City extends Polygon {
    // istniejące pola, np.
    public final Point center;
    private String name;

    public City(Point center, String name, double wallLength) {
        super(createSquarePoints(center, wallLength));
        this.center = center;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // Pakietowy dostęp do zbioru zasobów
    Set<Resource.Type> resources = new HashSet<>();

    public City(Point center, String name, double wallLength) {
        super(createWalls(center, wallLength));
        this.center = center;
        this.name = name;
    }

    // (inne metody, np. updatePortStatus itd.)

    /**
     * Metoda dodaje do zbioru 'resources' typy zasobów, które są w zasięgu 'range' od środka miasta.
     * Ryby (Fish) są dodawane tylko jeśli miasto jest portowe.
     * @param resourcesList lista zasobów do sprawdzenia
     * @param range maksymalna odległość zasobu od centrum miasta
     */
    public void addResourcesInRange(List<Resource> resourcesList, double range) {
        for (Resource resource : resourcesList) {
            double dist = distance(center, resource.point);

            if (dist <= range) {
                if (resource.type == Resource.Type.Fish) {
                    // Ryby tylko dla miast portowych
                    if (port) {
                        resources.add(resource.type);
                    }
                } else {
                    // Drewno i węgiel dodajemy zawsze jeśli w zasięgu
                    resources.add(resource.type);
                }
            }
        }
    }

    // Metoda do liczenia odległości euklidesowej między dwoma punktami
    private double distance(Point p1, Point p2) {
        double dx = p1.x() - p2.x();
        double dy = p1.y() - p2.y();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Pozostałe metody (getName, isPort, updatePortStatus itd.)

    @Override
    public String toString() {
        return name + (port ? "⚓" : "");
    }

}

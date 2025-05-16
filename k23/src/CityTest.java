import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class MapParser {

    // Istniejąca lista nazw (Label)
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "text")
    private List<Label> labels;

    // Nowa lista lądów - zielone polygony
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "polygon")
    private List<LandPolygon> lands = new ArrayList<>();

    // Nowa lista miast - czerwone rect
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "rect")
    private List<CityRect> cityRects = new ArrayList<>();

    // Po sparsowaniu możemy utworzyć listę City na podstawie cityRects
    private List<City> cities = new ArrayList<>();

    // Getter do lądów (typu Land)
    public List<Land> getLands() {
        List<Land> landList = new ArrayList<>();
        for (LandPolygon lp : lands) {
            // Mapowanie LandPolygon na Land (tworzymy listę punktów z polygonu)
            landList.add(lp.toLand());
        }
        return landList;
    }

    // Getter do miast
    public List<City> getCities() {
        return cities;
    }

    // Metoda wywoływana po parsowaniu, aby zbudować listę miast
    public void buildCities() {
        cities.clear();
        for (CityRect rect : cityRects) {
            // Obliczamy środek miasta na podstawie x, y, width i height
            double centerX = rect.x + rect.width / 2.0;
            double centerY = rect.y + rect.height / 2.0;

            Point center = new Point(centerX, centerY);
            City city = new City(center, null, Math.min(rect.width, rect.height)); // wallLength = minimum boku
            cities.add(city);
        }
    }

    // Klasy pomocnicze dla XML (dostosuj pola do rzeczywistego pliku SVG)
    public static class Label {
        @JacksonXmlProperty(isAttribute = true)
        public double x;
        @JacksonXmlProperty(isAttribute = true)
        public double y;
        @JacksonXmlProperty(isAttribute = false)
        public String value;
        // ...
    }

    public static class LandPolygon {
        @JacksonXmlProperty(isAttribute = true)
        public String points; // np. "x1,y1 x2,y2 ..."

        // Metoda konwertująca na Land
        public Land toLand() {
            List<Point> pointList = new ArrayList<>();
            String[] pairs = points.trim().split("\\s+");
            for (String pair : pairs) {
                String[] coords = pair.split(",");
                double x = Double.parseDouble(coords[0]);
                double y = Double.parseDouble(coords[1]);
                pointList.add(new Point(x, y));
            }
            return new Land(pointList);
        }
    }

    public static class CityRect {
        @JacksonXmlProperty(isAttribute = true)
        public double x;
        @JacksonXmlProperty(isAttribute = true)
        public double y;
        @JacksonXmlProperty(isAttribute = true)
        public double width;
        @JacksonXmlProperty(isAttribute = true)
        public double height;
    }

    public void addCitiesToLands() {
        for (Land land : getLands()) {           // dla każdego lądu
            for (City city : getCities()) {      // dla każdego miasta
                if (land.inside(city.center)) { // jeśli środek miasta jest na lądzie
                    land.addCity(city);          // dodaj miasto do lądu
                }
            }
        }
    }
}

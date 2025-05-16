import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class City {

    private final String name;
    private final String country;
    private final int timezoneOffset;
    private final double longitude;
    private final double latitude;
    private final ZoneId zoneId;

    public City(String name, String country, int timezoneOffset, double longitude, double latitude) {
        this.name = name;
        this.country = country;
        this.timezoneOffset = timezoneOffset;
        this.longitude = longitude;
        this.latitude = latitude;
        this.zoneId = ZoneId.ofOffset("UTC", java.time.ZoneOffset.ofHours(timezoneOffset));
    }

    public static void generateAnalogClocksSvg(List<City> cityList, AnalogClock analogClock) throws IOException {
        String dirName = analogClock.toString();

        Path dirPath = Paths.get(dirName);

        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        for (City city : cityList) {
            analogClock.setCity(city);

            String fileName = city.getName().replaceAll("\\s+", "_") + ".svg";
            Path filePath = dirPath.resolve(fileName);

            analogClock.toSvg(filePath.toString());
        }
    }

    public String getName() {
        return name;
    }
    public String getCountry() {
        return country;
    }
    public int getTimezoneOffset() {
        return timezoneOffset;
    }
    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }
    public ZoneId getZoneId() {
        return zoneId;
    }

    private static City parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) {
            throw new IllegalArgumentException("Nieprawidłowy format linii: " + line);
        }
        String name = parts[0].trim();
        String country = parts[1].trim();
        int timezoneOffset = Integer.parseInt(parts[2].trim());
        double longitude = Double.parseDouble(parts[3].trim());
        double latitude = Double.parseDouble(parts[4].trim());

        return new City(name, country, timezoneOffset, longitude, latitude);
    }

    public static Map<String, City> parseFile(String path) throws IOException {
        Map<String, City> cityMap = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                City city = parseLine(line);
                cityMap.put(city.getName(), city);
            }
        }
        return cityMap;
    }

    public LocalTime localMeanTime(LocalTime zoneTime) {
        double offsetSeconds = (longitude / 15.0) * 3600.0;

        int offsetSec = (int) Math.round(offsetSeconds);

        int totalSeconds = zoneTime.toSecondOfDay() + offsetSec;

        totalSeconds = ((totalSeconds % 86400) + 86400) % 86400;

        return LocalTime.ofSecondOfDay(totalSeconds);
    }

    public static int worstTimezoneFit(City c1, City c2) {
        LocalTime now = LocalTime.now();

        LocalTime c1ZoneTime = now;
        LocalTime c2ZoneTime = now;

        LocalTime c1LMT = c1.localMeanTime(c1ZoneTime);
        LocalTime c2LMT = c2.localMeanTime(c2ZoneTime);

        int diff1 = Math.abs(c1LMT.toSecondOfDay() - c1ZoneTime.toSecondOfDay());
        int diff2 = Math.abs(c2LMT.toSecondOfDay() - c2ZoneTime.toSecondOfDay());

        return Integer.compare(diff2, diff1);
    }

    @Override
    public String toString() {
        return name;
    }
}

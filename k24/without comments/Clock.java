import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public abstract class Clock {

    private int hour;
    private int minute;
    private int second;
    private LocalTime time;

    private City city;

    public Clock(City city) {
        this.city = city;
        setCurrentTime();
    }

    public void setCurrentTime() {

        ZoneId zone = city.getZoneId();

        LocalTime localTime = ZonedDateTime.now(zone).toLocalTime();

        setTime(localTime.getHour(), localTime.getMinute(), localTime.getSecond());
    }

    public void setTime(int hour, int minute, int second) {
        if (hour < 0 || hour > 23)
            throw new IllegalArgumentException("Nieprawidłowa godzina: " + hour);
        if (minute < 0 || minute > 59)
            throw new IllegalArgumentException("Nieprawidłowa minuta: " + minute);
        if (second < 0 || second > 59)
            throw new IllegalArgumentException("Nieprawidłowa sekunda: " + second);

        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City newCity) {
        if (newCity == null) {
            throw new IllegalArgumentException("Miasto nie może być null");
        }

        ZoneId oldZone = city.getZoneId();
        ZoneId newZone = newCity.getZoneId();

        ZonedDateTime oldTime = ZonedDateTime.now(oldZone);

        ZonedDateTime newTime = oldTime.withZoneSameInstant(newZone);

        setTime(newTime.getHour(), newTime.getMinute(), newTime.getSecond());
        city = newCity;
    }

    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }
    public int getSecond() {
        return second;
    }

    protected LocalTime getTime() {
        return this.time;
    }

}

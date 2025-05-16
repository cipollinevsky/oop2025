import java.time.LocalTime;

/**
 * Abstrakcyjna klasa reprezentująca wskazówkę zegara tarczowego.
 */
public abstract class ClockHand {

    /**
     * Ustawia wskazówkę na odpowiedni kąt na podstawie przekazanego czasu.
     * @param time czas lokalny, według którego ustawia się wskazówka.
     */
    public abstract void setTime(LocalTime time);

    /**
     * Zwraca łańcuch znaków zawierający znacznik SVG reprezentujący tę wskazówkę.
     * @return napis ze znacznikiem SVG wskazówki.
     */
    public abstract String toSvg();
}

public class Point {
    // Publiczne, ostateczne pola przechowujące współrzędne
    public final double x;
    public final double y;

    // Konstruktor ustawiający wartości x i y
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getter zwracający wartość pola x
    public double x() {
        return x;
    }

    // Getter zwracający wartość pola y
    public double y() {
        return y;
    }
}

public class Resource {
    // Typ wyliczeniowy zasobu
    public enum Type {
        Coal,
        Wood,
        Fish
    }

    // Publiczne, finalne pola
    public final Point point;  // pozycja zasobu na mapie
    public final Type type;    // typ zasobu

    /**
     * Konstruktor klasy Resource.
     * @param point pozycja zasobu na mapie
     * @param type typ zasobu
     */
    public Resource(Point point, Type type) {
        this.point = point;
        this.type = type;
    }
}

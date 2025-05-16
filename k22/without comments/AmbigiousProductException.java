import java.util.List;

public class AmbigiousProductException extends Exception {
    public AmbigiousProductException(List<String> matchingNames) {
        super("Znaleziono wiele pasujących produktów:\n" + String.join("\n", matchingNames));
    }
}

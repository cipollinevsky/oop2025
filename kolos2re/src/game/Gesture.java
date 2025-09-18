package game;

public enum Gesture {
    ROCK,
    PAPER,
    SCISSORS;

    public static Gesture fromString(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        return switch (input.toLowerCase()) {
            case "r" -> ROCK;
            case "p" -> PAPER;
            case "s" -> SCISSORS;
            default -> throw new IllegalArgumentException("Unknown gesture: " + input);
        };
    }

    public int compareWith(Gesture other) {
        if (this == other) {
            return 0;
        }

        return switch (this) {
            case ROCK -> (other == SCISSORS) ? 1 : -1;
            case PAPER -> (other == ROCK) ? 1 : -1;
            case SCISSORS -> (other == PAPER) ? 1 : -1;
        };
    }
}

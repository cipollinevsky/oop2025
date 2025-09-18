package game;

public class Duel {
    private Player player1;
    private Player player2;

    private Gesture gesture1;
    private Gesture gesture2;

    private Runnable onEnd;

    public Duel(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;

        p1.enterDuel(this);
        p2.enterDuel(this);
    }

    public void setOnEnd(Runnable onEnd) {
        this.onEnd = onEnd;
    }

    public void handleGesture(Player player, Gesture gesture) {
        if (player == player1) {
            gesture1 = gesture;
        } else if (player == player2) {
            gesture2 = gesture;
        }

        if (gesture1 != null && gesture2 != null && onEnd != null) {
            onEnd.run();
        }
    }

    public record Result(Player winner, Player loser) {}

    public Result evaluate() {
        if (gesture1 == null || gesture2 == null) {
            return null;
        }

        int comparison = gesture1.compareWith(gesture2);
        if (comparison == 0) {
            return null;
        } else if (comparison > 0) {
            return new Result(player1, player2);
        } else {
            return new Result(player2, player1);
        }
    }

    public void finish() {
        player1.leaveDuel();
        player2.leaveDuel();
    }
}

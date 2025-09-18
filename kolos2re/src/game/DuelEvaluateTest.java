package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DuelEvaluateTest {
    @Test
    void testPlayer1Wins() {
        Player player1 = new Player();
        Player player2 = new Player();
        Duel duel = new Duel(player1, player2);

        player1.makeGesture(Gesture.ROCK);
        player2.makeGesture(Gesture.SCISSORS);

        Duel.Result result = duel.evaluate();

        assertNotNull(result, "Result can not be null if winner exists");
        assertEquals(player1, result.winner(), "The winner should be player1");
        assertEquals(player2, result.loser(), "The loser should be player2");
    }

    @Test
    void testDraw() {
        Player player1 = new Player();
        Player player2 = new Player();
        Duel duel = new Duel(player1, player2);

        player1.makeGesture(Gesture.PAPER);
        player2.makeGesture(Gesture.PAPER);

        Duel.Result result = duel.evaluate();

        assertNull(result, "Result must be null if draw");
    }
}

package game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DuelTest {
    @Test
    void testPlayersEnterDuel() {
        Player player1 = new Player();
        Player player2 = new Player();

        Duel duel = new Duel(player1, player2);

        assertTrue(player1.isDueling(), "Player1 must be in duel");
        assertTrue(player2.isDueling(), "Player2 must be in duel");
    }

}

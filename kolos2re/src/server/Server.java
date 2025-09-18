package server;

import game.Duel;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final int PORT = 12345;

    private List<ClientHandler> clients = new ArrayList<>();
    private Database database;

    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }

    public void listen() {

    }

    public void challengeToDuel(ClientHandler challenger, String challengeeLogin) {
        for (ClientHandler client : clients) {
            if (client == challenger) {
                challenger.sendMessage("You cannot challenge yourself!");
                return;
            }

            if (client.isDueling()) {
                challenger.sendMessage("Player " + challengeeLogin + " is currently in a duel!");
                return;
            }

            startDuel(challenger, client);
            return;
        }

        challenger.sendMessage("Not found player with login: " + challengeeLogin);
    }

    private void startDuel(ClientHandler challenger, ClientHandler challengee) {
        game.Duel duel = new game.Duel(challenger, challengee);

        duel.setOnEnd(() -> {
            game.Duel.Result result = duel.evaluate();

            if (result == null) {
                ((ClientHandler) challenger).sendMessage("Draw!");
                ((ClientHandler) challengee).sendMessage("Draw!");
            } else {
                ((ClientHandler) result.winner()).sendMessage("You won!");
                ((ClientHandler) result.loser()).sendMessage("You lose!");

                database.updateLeaderboard(
                        ((ClientHandler) result.winner()).getLogin(),
                        ((ClientHandler) result.loser()).getLogin()
                );
            }

            duel.finish();

            System.out.println("Leaderboard:");
            database.getLeaderboard().forEach((login, points) ->
                    System.out.println(login + " : " + points)
            );
        });

        challenger.sendMessage("Duel started with: " + challengee.getLogin());
        challengee.sendMessage("Duel started with: " + challenger.getLogin());
    }

    public void addClient(ClientHandler client) {
        clients.add(client);
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}

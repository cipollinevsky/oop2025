package server;

import game.Player;

import java.io.*;
import java.net.Socket;

public class ClientHandler extends Player implements Runnable{
    private Socket socket;
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    private String login;


    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String getLogin() {
        return login;
    }

    @Override
    public void  run() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (isDueling()) {
                    game.Gesture gesture = game.Gesture.fromString(message);
                    if (gesture != null) {
                        makeGesture(gesture);
                    }
                } else {
                    server.challengeToDuel(this, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}

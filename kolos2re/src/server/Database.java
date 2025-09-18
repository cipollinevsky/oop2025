package server;

import game.Duel;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.sql.*;

public class Database {

    private String url = "jdbc:sqlite:users.db";

    public Database() {
    }

    public boolean authenticate(String login, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, login);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateLeaderboard(String winner, String loser) {
        String sqlWinner =  "UPDATE users SET points = points + 1 WHERE login = ?";
        String sqlLoser =  "UPDATE users SET points = points - 1 WHERE login = ?";

        try (Connection conn = DriverManager.getConnection(url)) {
            try (PreparedStatement pstmt = conn.prepareStatement(sqlWinner)) {
                pstmt.setString(1, winner);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(sqlLoser)) {
                pstmt.setString(1, loser);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getLeaderboard() {
        String sql = "SELECT login, points FROM users ORDER BY points DESC";
        Map<String, Integer> leaderboard = new LinkedHashMap<>();

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String login = rs.getString("login");
                int points = rs.getInt("points");
                leaderboard.put(login, points);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }
}

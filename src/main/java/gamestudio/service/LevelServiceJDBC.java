package gamestudio.service;

import gamestudio.entity.Level;

import java.sql.*;
import java.util.List;

public class LevelServiceJDBC implements LevelService{
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "gamestudio";
    public static final String SELECT_BY_NAME = "SELECT name, difficulty, level.level FROM level WHERE name = ?";
    public static final String SELECT_BY_DIFFICULTY = "SELECT name, difficulty, level.level FROM level WHERE difficulty = ?";
    public static final String INSERT = "INSERT INTO level (name, difficulty, level) VALUES (?, ?, ?)";

    @Override
    public void addLevel(Level level) throws LevelException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT)
        ) {
            statement.setString(1, level.getName());
            statement.setInt(2, level.getDifficulty());
            statement.setString(3, level.getLevel());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new LevelException("Problem inserting level", e);
        }
    }

    @Override
    public Level getLevel(String name) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME)
        ) {
            statement.setString(1, name);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Level(rs.getString(1), rs.getInt(2), rs.getString(3));
                }

                throw new LevelException("There is no such a level in database!");
            }
        } catch (SQLException e) {
            throw new LevelException("Problem selecting level", e);
        }
    }

    @Override
    public Level getLevel(int difficulty, List<String> playedGames) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_DIFFICULTY)
        ) {
            statement.setInt(1, difficulty);
            try (ResultSet rs = statement.executeQuery()) {
                while(rs.next()){
                    if (!playedGames.contains(rs.getString(1))) {
                        return new Level(rs.getString(1), rs.getInt(2), rs.getString(3));
                    }
                }

                throw new LevelException("There is no such a level with this difficulty in database!");
            }
        } catch (SQLException e) {
            throw new LevelException("Problem selecting level", e);
        }
    }

    @Override
    public Level nextLevel(Level currLevel, List<String> playedGames) {
        return null;
    }

    @Override
    public Level prevLevel(Level currLevel, List<String> playedGames) {
        return null;
    }

    @Override
    public boolean checkConection() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)
        ) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}

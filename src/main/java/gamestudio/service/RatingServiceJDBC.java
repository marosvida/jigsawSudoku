package gamestudio.service;

import gamestudio.entity.Rating;

import java.sql.*;

public class RatingServiceJDBC implements RatingService{
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "gamestudio";
    public static final String GET_CONCRETE_RATING = "SELECT rating FROM rating WHERE game = ? AND player = ?";
    public static final String GET_GAME_RATING = "SELECT rating FROM rating WHERE game = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String DELETE_RATING = "DELETE FROM rating WHERE game = ? AND player = ?";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, playedOn) VALUES (?, ?, ?, ?)";


    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(INSERT);
             PreparedStatement statement_delete = connection.prepareStatement(DELETE_RATING)
        ) {
            statement_delete.setString(1, rating.getGame());
            statement_delete.setString(2, rating.getPlayer());
            statement_delete.executeUpdate();

            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getPlayedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RatingException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GET_GAME_RATING)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                int sumOfRatings = 0;
                int numOfRatings = 0;
                while (rs.next()) {
                    sumOfRatings += rs.getInt(1);
                    numOfRatings++;
                }

                if(numOfRatings == 0 ) return 0;

                return Math.round(sumOfRatings/numOfRatings);
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GET_CONCRETE_RATING)
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if(rs.next())
                    return rs.getInt(1);

                throw new RatingException("There is no such a rating");
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting concrete rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting rating", e);
        }
    }
}

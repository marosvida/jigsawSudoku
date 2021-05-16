package gamestudio.service;

import gamestudio.entity.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

public class RatingServiceRestClient implements RatingService{
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void setRating(Rating rating) throws RatingException {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        return Objects.requireNonNull(restTemplate.getForEntity(url + "/" + game, Integer.class).getBody());
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        return Objects.requireNonNull(restTemplate.getForEntity(url + "/" + game + "/" + player, Integer.class).getBody());
    }

    @Override
    public void reset() throws RatingException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}

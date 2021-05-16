package gamestudio.service;

import gamestudio.entity.Rating;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {
    private RatingService service;

    @Test
    public void resetTest(){
        service =  new RatingServiceJDBC();
        service.reset();
        assertEquals(0, service.getAverageRating("jigsawSudoku"), "Rating database was not deleted!!!");
    }

    @Test
    public void getRatingTest(){
        service = new RatingServiceJDBC();
        service.reset();

        service.setRating(new Rating("jigsawSudoku", "hrac", 3, new Date()));
        assertEquals(3, service.getRating("jigsawSudoku", "hrac"), "Expected concrete rating was not obtained from rating database!");
    }

    @Test
    public void getAverageRatingTest(){
        service = new RatingServiceJDBC();
        service.reset();

        service.setRating(new Rating("jigsawSudoku", "jozo", 2, new Date()));
        service.setRating(new Rating("jigsawSudoku", "zuzka", 2, new Date()));
        service.setRating(new Rating("jigsawSudoku", "helena", 4, new Date()));
        service.setRating(new Rating("jigsawSudoku", "pavol", 4, new Date()));
        assertEquals(3, service.getAverageRating("jigsawSudoku"), "Expected average rating of a game was not obtained from rating database!");
    }

    @Test
    public void setRatingTest(){
        service = new RatingServiceJDBC();
        service.reset();

        service.setRating(new Rating("jigsawSudoku", "hrac", 5, new Date()));
        assertEquals(5, service.getRating("jigsawSudoku", "hrac"), "Rating was not added into rating database!");
    }
}

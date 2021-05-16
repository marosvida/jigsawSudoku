package gamestudio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gamestudio.entity.Score;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class ScoreServiceTest {
    private ScoreService service;

    @Test
    public void resetTest(){
        service = new ScoreServiceJDBC();
        service.reset();
        assertEquals(0, service.getTopScores("mines").size(), "Score database was not deleted!!!");
    }

    @Test
    public void addScoreTest(){
        service = new ScoreServiceJDBC();
        service.reset();

        service.addScore(new Score("jigsawSudoku", "hrac", 25, new Date()));
        assertEquals(1, service.getTopScores("jigsawSudoku").size(), "Score was not added into score database!");
    }

    @Test
    public void getTopScoresNumTest(){
        service = new ScoreServiceJDBC();
        service.reset();

        service.addScore(new Score("jigsawSudoku", "hrac1", 22, new Date()));
        service.addScore(new Score("jigsawSudoku", "hrac2", 24, new Date()));
        service.addScore(new Score("jigsawSudoku", "hrac3", 15, new Date()));
        service.addScore(new Score("jigsawSudoku", "hrac4", 35, new Date()));

        List<Score> scoreList = service.getTopScores("jigsawSudoku");

        assertEquals(4, scoreList.size(), "The amount of expected topScores was not obtained by getTopScores!");
    }

    @Test
    public void getTopScoresMaxNumTest(){
        service = new ScoreServiceJDBC();
        service.reset();

        //add 20 scores into database
        for(int i = 0; i < 20; i++){
            service.addScore(new Score("jigsawSudoku", "hrac1", 100, new Date()));
        }

        List<Score> scoreList = service.getTopScores("jigsawSudoku");

        assertEquals(10, scoreList.size(), "There was obtained more than 10 scores from score database!");
    }

    @Test
    public void getTopScoresOrderTest(){
        service = new ScoreServiceJDBC();
        service.reset();

        service.addScore(new Score("jigsawSudoku", "hrac1", 50, new Date()));
        service.addScore(new Score("jigsawSudoku", "hrac2", 100, new Date()));
        service.addScore(new Score("jigsawSudoku", "hrac3", 200, new Date()));
        service.addScore(new Score("jigsawSudoku", "hrac4", 75, new Date()));

        List<Score> scoreList = service.getTopScores("jigsawSudoku");

        Score score = scoreList.get(0);

        assertEquals("hrac3", score.getPlayer(), "The expected playerName of concrete score was not obtained!");
        assertEquals(200, score.getPoints(), "The expected score of concrete score was not obtained!");

    }


}

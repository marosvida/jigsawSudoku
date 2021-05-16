package gamestudio.service;

import gamestudio.entity.Level;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LevelServiceTest {
    private LevelService service;

    //test for getting the specific level by its name
    @Test
    public void getLevelByNameTest(){
        service = new LevelServiceJDBC();

        if(!service.checkConection()){
            service = new LevelServiceLocal();
        }

        Level level = service.getLevel("first love");

        assertEquals("first love", level.getName(), "The expected level was not obtained by getLevel() by specific its name!");
    }

    //test for getting the level with specific difficulty
    @Test
    public void getLevelByDifficultyTest(){
        service = new LevelServiceJDBC();

        if(!service.checkConection()){
            service = new LevelServiceLocal();
        }

        List<String> playedLevels = new ArrayList<>();

        Level level = service.getLevel(1, playedLevels);

        assertEquals(1, level.getDifficulty(), "The level with expected difficulty was not obtained by getLevel() by specific its difficulty!");
    }


    //test for getting the level with specific difficulty, but different that was played already
    @Test
    public void getNotPlayedLevelTest(){
        service = new LevelServiceJDBC();

        if(!service.checkConection()){
            service = new LevelServiceLocal();
        }

        List<String> playedLevels = new ArrayList<>();
        Level level = service.getLevel(1, playedLevels);
        playedLevels.add(level.getName());

        level = service.getLevel(1, playedLevels);

        assertEquals(1, level.getDifficulty(), "The level with expected difficulty was not obtained by getLevel() by specific its difficulty!");
        assertNotEquals(playedLevels.get(0), level.getName(), "The already played level was again obtained from database!");
    }
}

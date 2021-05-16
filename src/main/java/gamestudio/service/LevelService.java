package gamestudio.service;

import gamestudio.entity.Level;

import java.util.List;

public interface LevelService {
    void addLevel(Level level);
    Level getLevel(String name);
    Level getLevel(int difficulty, List<String> playedGames);
    boolean checkConection();
    Level nextLevel(Level currLevel, List<String> playedGames);
    Level prevLevel(Level currLevel, List<String> playedGames);
}

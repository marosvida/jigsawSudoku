package gamestudio.service;

import gamestudio.entity.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelServiceLocal implements LevelService{
    private List<Level> levelList;

    public LevelServiceLocal() {
        levelList = new ArrayList<>();
        levelList.add(new Level(0,"practise", 1, "2A7A4A6A5B1B9B8C3C9A8A3A1A4B7B6B5C2C5A6D9D0D8B2B3B1C4C3D5D6E8E7E4E0F2F9C1D2D8D4D9E5E7F3F6C6G4G1G5G2E3I8I9F7C8G9G7G3G1E6I2I4F5F7H1H0H2G3E0I4I6F8F4H3H2H9H6H8H5I7I1I"));
        levelList.add(new Level(1, "first love", 1, "0A0B4B0B0B0B3B0B0C0A9A0A4A0A1D0B5E0C6F0F0A0A0A0D0B0E4C0F8F0F7F0F4D0D3E0C0F0F0D0D5D0D0E0E0C0G1G0D9H0E6E0E4E0C2G0H0H0H0I0I0I0I3C0G5G0G8H0H2H0I1C0C0G0G9G0H0H0I2I0I0I"));
        levelList.add(new Level(2, "forest cottage", 2, "7A0A6A0A0B0B0B0C0C0A0D4A0A0A0B8B0C0C0D0D0D0E5A3B0B0C8C0D0D0D0E0E0B3F0C0C0D0G0G0E8E0B0F1C6I0D1G5H0H0E0E0F0F0I2G0G0H0H9E4F0F0F0I0G0G3H9H0E6F7F2I1I0G0G0H1H7H0I5I9I0I"));
        levelList.add(new Level(3, "mountain climber", 3, "0A0A2A0A0B0B5B0C0C0A0A0D0B1B0C6C0C0C0A0A0D0D0B0B2B8B0C0A0D8D1D0E0E0E0C9C0D0D6D2F0E8E3E0G0G3F0F0F0F0F2E1E0G0G0F1F4H0H0H0H0E0G0I0H0F1H0I7G0G0G0G0I0H0H5H0I0I0I4I0I0I"));
        levelList.add(new Level(4, "the river", 1, "0A0A0A4B0C0C0D3D0D0A0A2A0B0C3C7C0C0D0A0A0A6B4C1E0C0D0D2B0B0B0B0E7E1C0D0D0B3F0E2E0E4E0E1I0D0B0F3E5G0G0G0G0I8I0F0F0F1G7G8G0G0I0I0F0F8F7H0H0H6G0I0I0F4H0H0H0H5H0H0I0I"));
        levelList.add(new Level(5, "rainy day", 2, "0A0A0A7A0B5B0B0B8B0A0A9C0A0B0C0B6B1B3A0C1C0C0C0C0D0D0D0A0E8E0E0E0C0C0D0D0F7G6G0G0E0E8D1D0D0F0G0G0G0G0E5E0I0D0F0F0G0F0G0H2E0I9I6F3F0F0F0H0H1I0I0I8H0H0H3H0H1H0I0I0I"));
        levelList.add(new Level(6, "puzzle", 3, "0A4A0A0B0B5C0C0C0C2A0A0A0A0B0B0C0C7C0D0D1D0A9A0B5B6E0C0F5D0G0G0B6B0H0E0C0F0D0G0G0B0G0H0E0E0F0D0G6G0G0G0H9E0E0F6D8D0D2H0H1H0E0E3F0F0F0F0H0I0H0H1E0F0I0I1I0I0I0I4I0I"));
    }

    @Override
    public void addLevel(Level level) {
        levelList.add(level);
    }

    @Override
    public Level getLevel(String name) {
        for (Level level : levelList) {
            if (level.getName().equals(name)) {
                return level;
            }
        }

        throw new LevelException("Level with this name is not in local database!");
    }

    @Override
    public Level getLevel(int difficulty, List<String> playedGames) {
        for (Level level : levelList) {
            if (level.getDifficulty() == difficulty && !playedGames.contains(level.getName())) {
                return level;
            }
        }

        throw new LevelException("Level with this difficulty is not in local database!");
    }

    @Override
    public Level nextLevel(Level currLevel, List<String> playedGames) {
        for(int idx = currLevel.getIdent()+1; idx < levelList.size(); idx++){
            Level level = levelList.get(idx);
            if (level.getDifficulty() == currLevel.getDifficulty() && !playedGames.contains(level.getName())) {
                return level;
            }
        }

        for(int i = 0; i < currLevel.getIdent(); i++){
            Level level = levelList.get(i);
            if (level.getDifficulty() == currLevel.getDifficulty() && !playedGames.contains(level.getName())) {
                return level;
            }
        }

        return null;
    }

    @Override
    public Level prevLevel(Level currLevel, List<String> playedGames) {
        for(int idx = currLevel.getIdent()-1; idx >= 0; idx--){
            Level level = levelList.get(idx);
            if (level.getDifficulty() == currLevel.getDifficulty() && !playedGames.contains(level.getName())) {
                return level;
            }
        }

        for(int i = levelList.size() - 1; i > currLevel.getIdent(); i--){
            Level level = levelList.get(i);
            if (level.getDifficulty() == currLevel.getDifficulty() && !playedGames.contains(level.getName())) {
                return level;
            }
        }

        return null;
    }

    @Override
    public boolean checkConection() {
        return true;
    }

}

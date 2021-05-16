package gamestudio.service;

import gamestudio.entity.Level;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class LevelServiceJPA implements LevelService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addLevel(Level level) {
        entityManager.persist(level);
    }

    @Override
    public Level getLevel(String name) {
        return (Level) entityManager.createNamedQuery("Level.getLevelByName")
                .setParameter("name", name).getResultList().get(0);
    }

    @Override
    public Level getLevel(int difficulty, List<String> playedGames) {
        return (Level) entityManager.createNamedQuery("Level.getLevelByDifficulty")
                .setParameter("difficulty", difficulty).getResultList().get(0);
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
        return entityManager.isOpen();
    }
}

package gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery( name = "Level.getLevelByName",
            query = "SELECT l from Level l WHERE l.name=:name")
@NamedQuery( name = "Level.getLevelByDifficulty",
        query = "SELECT l from Level l WHERE l.difficulty=:difficulty")
public class Level {
    @Id
    @GeneratedValue
    private int ident;

    private String name;
    private int difficulty;
    private String level;

    public Level() {
    }

    public Level(String name, int difficulty, String level) {
        this.name = name;
        this.difficulty = difficulty;
        this.level = level;
    }

    public Level(int id, String name, int difficulty, String level) {
        this.ident = id;
        this.name = name;
        this.difficulty = difficulty;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }
}

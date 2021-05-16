package gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Date;

@Entity
@NamedQuery( name = "Comment.getComments",
            query = "SELECT c FROM Comment c WHERE c.game=:game ORDER BY c.playedOn")
@NamedQuery( name = "Comment.resetComments",
            query = "DELETE FROM Comment")
public class Comment {
    @Id
    @GeneratedValue
    private int ident;

    private String game;
    private String player;
    private String comment;
    private Date playedOn;

    public Comment() {
    }

    public Comment(String game, String player, String comment, Date playedOn) {
        this.game = game;
        this.player = player;
        this.comment = comment;
        this.playedOn = playedOn;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPlayedOn() {
        return playedOn;
    }

    public void setPlayedOn(Date playedOn) {
        this.playedOn = playedOn;
    }

    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }
}

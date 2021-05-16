package gamestudio.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NamedQuery( name = "User.getPassword",
        query = "SELECT u.password FROM User u WHERE u.login=:login")
@NamedQuery( name = "User.getLogin",
        query = "SELECT u FROM User u WHERE u.login=:login")
@NamedQuery( name = "User.resetUsers",
        query = "DELETE FROM User")
@Table(name = "Player")
public class User {
    @Id
    @GeneratedValue
    private int ident;

    private String login;
    private String password;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

package gamestudio.service;

import gamestudio.entity.User;

public interface UserService {
    void addUser(User user);
    String getPassword(String login);
    boolean isUser(String login);
    void reset();
}

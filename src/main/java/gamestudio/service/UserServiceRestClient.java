package gamestudio.service;

import gamestudio.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class UserServiceRestClient implements UserService{
    private final String url = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addUser(User user) {
        restTemplate.postForEntity(url, user, User.class);
    }

    @Override
    public boolean isUser(String login) {
        return false;
    }

    @Override
    public String getPassword(String login) {
        return null;
    }

    @Override
    public void reset() {

    }
}

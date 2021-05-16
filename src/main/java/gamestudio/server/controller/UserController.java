package gamestudio.server.controller;

import gamestudio.entity.Comment;
import gamestudio.entity.Rating;
import gamestudio.entity.User;
import gamestudio.service.CommentService;
import gamestudio.service.RatingService;
import gamestudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private User loggedUser;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private RatingService ratingService;

    @RequestMapping("/login")
    public String login(String login, String password) {
        if(!login.equals("") && !password.equals("")){
            if(userService.isUser(login)){
                if (userService.getPassword(login).equals(password)) {
                    loggedUser = new User(login, password);
                    return "redirect:/jigsawSudoku";
                }
            }
        }

        return "redirect:/jigsawSudoku";
    }

    @RequestMapping("/register")
    public String register(String login, String password) {
        if(!login.equals("") && !password.equals("")) {
            if(!userService.isUser(login)){
                if(password.length() >= 8){
                    loggedUser = new User(login, password);
                    userService.addUser(loggedUser);
                }
            }
        }

        return "redirect:/jigsawSudoku";
    }

    @RequestMapping("/logout")
    public String logout() {
        loggedUser = null;
        return "redirect:/jigsawSudoku";
    }

    @RequestMapping("/comment")
    public String comment(String comment){
        if(!comment.trim().equals("")){
            commentService.addComment(new Comment("jigsawSudoku", loggedUser.getLogin(), comment, new Date()));
        }
        return "redirect:/jigsawSudoku";
    }

    @RequestMapping("/rate")
    public String rate(String rating){
        System.out.println(rating + loggedUser.getLogin());
        System.out.println(ratingService);
        ratingService.setRating(new Rating("jigsawSudoku", loggedUser.getLogin(), Integer.parseInt(rating), new Date()));
        return "redirect:/jigsawSudoku";
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

}

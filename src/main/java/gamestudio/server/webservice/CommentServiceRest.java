package gamestudio.server.webservice;

import gamestudio.entity.Comment;
import gamestudio.service.CommentException;
import gamestudio.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public void addComment(@RequestBody Comment comment){
        commentService.addComment(comment);
    }

    @GetMapping("/{game}")
    public List<Comment> getComments(@PathVariable String game){
        return commentService.getComments(game);
    }

}

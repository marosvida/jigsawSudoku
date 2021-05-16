package gamestudio.service;

import gamestudio.entity.Comment;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {
    private CommentService service;

    @Test
    public void resetTest(){
        service = new CommentServiceJDBC();
        service.reset();

        assertEquals(0, service.getComments("jigsawSudoku").size(), "Comment database was not deleted!");
    }

    @Test
    public void addCommentTest(){
        service = new CommentServiceJDBC();
        service.reset();

        service.addComment(new Comment("jigsawSudoku", "ja", "hra je velmi dobra", new Date()));
        assertEquals(1, service.getComments("jigsawSudoku").size(), "Comment was not added into comment database!");
    }

    @Test
    public void getCommentsNumTest(){
        service = new CommentServiceJDBC();
        service.reset();

        service.addComment(new Comment("jigsawSudoku", "hrac1", "hra je velmi dobra", new Date()));
        service.addComment(new Comment("jigsawSudoku", "hrac2", "hra je velmi dobra2", new Date()));
        service.addComment(new Comment("jigsawSudoku", "hrac3", "hra je velmi dobra3", new Date()));
        service.addComment(new Comment("jigsawSudoku", "hrac4", "hra je velmi dobra4", new Date()));

        List<Comment> commentList = service.getComments("jigsawSudoku");

        assertEquals(4, commentList.size(), "The amount of expected comments was not obtained by getComments!");
    }

    @Test
    public void getCommentsMaxNumTest(){
        service = new CommentServiceJDBC();
        service.reset();

        //add 20 comments into database
        for(int i = 0; i < 20; i++){
            service.addComment(new Comment("jigsawSudoku", "hrac1", "hra je velmi dobra", new Date()));
        }

        List<Comment> commentList = service.getComments("jigsawSudoku");

        assertEquals(10, commentList.size(), "There was obtained more than 10 comments from comment database!");
    }

    @Test
    public void getCommentsTest(){
        service = new CommentServiceJDBC();
        service.reset();

        service.addComment(new Comment("jigsawSudoku", "hrac1", "hra je velmi dobra", new Date()));
        service.addComment(new Comment("jigsawSudoku", "hrac2", "hra je velmi dobra2", new Date()));
        service.addComment(new Comment("jigsawSudoku", "hrac3", "hra je velmi dobra3", new Date()));
        service.addComment(new Comment("jigsawSudoku", "hrac4", "hra je velmi dobra4", new Date()));

        List<Comment> commentList = service.getComments("jigsawSudoku");

        Comment comment = commentList.get(2);

        assertEquals("hrac3", comment.getPlayer(), "The expected playerName of concrete comment was not obtained!");
        assertEquals("hra je velmi dobra3", comment.getComment(), "The expected comment of concrete comment was not obtained!");

    }

}

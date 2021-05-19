package gamestudio.server.controller;

import gamestudio.entity.Level;
import gamestudio.entity.Rating;
import gamestudio.entity.Score;
import gamestudio.game.jigsawSudoku.core.*;
import gamestudio.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/jigsawSudoku")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class JigsawSudokuController {
    @Autowired
    private ScoreService scoreService;

    @Autowired
    private RatingService ratingService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserController userController;

    private LevelService levelService = new LevelServiceLocal();

    private int difficulty = 1;
    private int value = 0;
    private List<String> playedLevels = new ArrayList<>();

    private Level level = levelService.getLevel(difficulty, playedLevels);

    private Field field = new Field(level);

    @RequestMapping
    public String jigsawSudoku(@RequestParam(required = false) String row, @RequestParam(required = false) String column, Model model) {

        try {
            field.writeNumber(Integer.parseInt(row), Integer.parseInt(column), value);
            if(field.getGameState() == GameState.SOLVED){
                playedLevels.add(level.getName());
                if(userController.isLogged()){
                    scoreService.addScore(new Score("jigsawSudoku", userController.getLoggedUser().getLogin(), field.getScore(), new Date()));
                }
            }
        } catch (Exception e) {
            //Maros: Tato vynimka znamena, ze parametre neprisli spravne
        }

        fillModel(model);
        return "jigsawSudoku";
    }

    private void fillModel(Model model) {
        model.addAttribute("scores", scoreService.getTopScores("jigsawSudoku"));
        model.addAttribute("htmlField", getHtmlField());
        model.addAttribute("values", getValues());
        model.addAttribute("difficulties", getDifficulties());
        model.addAttribute("rating", ratingService.getAverageRating("jigsawSudoku"));
        model.addAttribute("comments", commentService.getComments("jigsawSudoku"));
        model.addAttribute("levelName", level.getName().substring(0,1).toUpperCase() + level.getName().substring(1));
        model.addAttribute("rating", getRating());
        model.addAttribute("currRating", getCurrGameAverageRating());
    }


    @RequestMapping("/value")
    public String changeValue(String value, Model model) {
        this.value = Integer.parseInt(value);
        fillModel(model);
        return "jigsawSudoku";
    }

    public int getValue(){
        return value;
    }

    @RequestMapping("/difficulty")
    public String changeDifficulty(String difficulty, Model model) {
        this.difficulty = Integer.parseInt(difficulty);
        level = levelService.getLevel(this.difficulty, playedLevels);
        field = new Field(level);
        fillModel(model);
        return "jigsawSudoku";
    }

    @RequestMapping("/next")
    public String nextLevel(Model model){
        level = levelService.nextLevel(level, playedLevels);
        field = new Field(level);
        fillModel(model);
        return "jigsawSudoku";
    }

    @RequestMapping("/prev")
    public String prevLevel(Model model){
        level = levelService.prevLevel(level, playedLevels);
        field = new Field(level);
        fillModel(model);
        return "jigsawSudoku";
    }

    public String getDifficulties(){
        StringBuilder sb = new StringBuilder();
        sb.append("<div>\n");

        sb.append(String.format("<a class='btn %s' role='button' href='/jigsawSudoku/difficulty?difficulty=1'>Easy</a>\n", difficulty == 1 ? "btn-primary" : "btn-secondary"));
        sb.append(String.format("<a class='btn %s' role='button' href='/jigsawSudoku/difficulty?difficulty=2'>Medium</a>\n", difficulty == 2 ? "btn-primary" : "btn-secondary"));
        sb.append(String.format("<a class='btn %s' role='button' href='/jigsawSudoku/difficulty?difficulty=3'>Hard</a>\n", difficulty == 3 ? "btn-primary" : "btn-secondary"));

        sb.append("</div>\n");
        return sb.toString();
    }

    public String getRating(){
        StringBuilder sb = new StringBuilder();
        sb.append("<form action='/rate' class='rating' id='addRatingForm'>");

        int currPlayerRating = 1;
        if(userController.isLogged()){
            try{
                currPlayerRating = ratingService.getRating("jigsawSudoku", userController.getLoggedUser().getLogin());
            } catch (RatingException e) {
                currPlayerRating = 1;
            }
        }

        for(int i = 5; i > 0; i--){
            sb.append(String.format("<input type='radio' name='rating' value='%d' id=%d %s>", i, i, currPlayerRating == i ? "checked" : ""));
            sb.append(String.format("<label for='%d'>☆</label>", i));
        }

        sb.append("</form>");
        return sb.toString();
    }

    public String getCurrGameAverageRating(){
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='currRating'>");

        int currRating = ratingService.getAverageRating("jigsawSudoku");

        for(int i = 5; i > 0; i--){
            sb.append(String.format("<input type='radio' name='rating' value='%d' id='r%d' %s disabled>", i, i, currRating == i ? "checked" : ""));
            sb.append(String.format("<label for='r%d'>☆</label>", i));
        }

        sb.append("</div>");
        return sb.toString();
    }

    public String getValues(){
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        int num = 0;

        for (int row = 0; row < 5; row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < 2; column++) {
                sb.append("<td class='my-1'>");
                sb.append(String.format("<a class='btn btn-outline-secondary %s' role='button' href='/jigsawSudoku/value?value=%d'>%d</a>\n", num == value ? "active" : "", num, num ));
                sb.append("</td>\n");
                num++;
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        return sb.toString();
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='field'>\n");

        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                Tile tile = field.getTile(row, column);
                sb.append(String.format("<td class='%s tile'>", getColor(tile.getSection())));
                if(field.getGameState() == GameState.SOLVED){
                    sb.append(String.format("<a class='%s btn btn-field disabled' role='button' href='/jigsawSudoku?row=%d&column=%d'>%s</a>\n", isAssignment(tile), row, column, tile.getValue() == 0 ? "-" : tile.getValue()));
                } else if(field.getGameState() == GameState.PAUSED && row == field.getUsedRow() && column == field.getUsedColumn()){
                    sb.append(String.format("<a class='%s text-danger btn-field' role='button' href='/jigsawSudoku?row=%d&column=%d'>%s</a>\n", isAssignment(tile), row, column, tile.getValue() == 0 ? "-" : tile.getValue()));
                } else {
                    sb.append(String.format("<a class='%s btn-field' role='button' href='/jigsawSudoku?row=%d&column=%d'>%s</a>\n", isAssignment(tile), row, column, tile.getValue() == 0 ? "-" : tile.getValue()));
                }
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        return sb.toString();
    }

    private String getColor(Section section){
        if(section == Section.A) return "blue";
        if(section == Section.B) return "red";
        if(section == Section.C) return "green";
        if(section == Section.D) return "yellow";
        if(section == Section.E) return "purple";
        if(section == Section.F) return "black";
        if(section == Section.G) return "crimson";
        if(section == Section.H) return "cyan";
        if(section == Section.I) return "orange";

        return null;
    }

    private String isAssignment(Tile tile){
        return tile instanceof Assignment ? "bold" : "nic";
    }

    public boolean isSolved(){
        return field.getGameState() == GameState.SOLVED;
    }

}

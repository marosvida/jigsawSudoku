package gamestudio.game.jigsawSudoku.consoleUI;

import gamestudio.entity.Comment;
import gamestudio.entity.Level;
import gamestudio.entity.Rating;
import gamestudio.entity.Score;
import gamestudio.game.jigsawSudoku.core.*;
import gamestudio.game.jigsawSudoku.core.Record;
import gamestudio.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("([0-9])([A-I])([1-9])");
    private static final Pattern SELECT_DIFFICULTY = Pattern.compile("[1-3]");
    private static final Pattern RATE_THE_GAME = Pattern.compile("[1-5]");

    private final Field field;
    private final String GAME = "jigsawSudoku";
    private final ArrayList<Record> record;
    private List<String> playedLevels = new ArrayList<>();
    private String playerName;
    private String levelName;

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private LevelService levelService;

    public void run(){
        printRules();

        getUserName();

        play();
    }

    private void getUserName() {
        System.out.println("Please enter your user name: ");
        playerName = scanner.nextLine().toUpperCase();

        while(playerName.isEmpty()){
            playerName = scanner.nextLine().toUpperCase();
        }
    }

    private void printRules() {
        System.out.println("Welcome!");
        System.out.println("The goal of the Jigsaw Sudoku is to fill the 9x9 board with 1-9 numbers.");
        System.out.println("Numbers must be unique in each row and column.");
        System.out.println("Also numbers must be unique in each section, which are represented by different shapes\nnot only by 3x3 squares like in the regular sudoku.");
        System.out.println("The sections is differentiate by colors.");
        System.out.println("Good luck! Have fun! :-)");
    }

    private void play(){
        initializeLevel();

        printTopScrores();

        gameLoop();

        if(field.getGameState() == GameState.SOLVED){
            printField();
            System.out.println("Well done!");

            Score score = new Score(GAME, playerName, field.getScore(), new Date());
            scoreService.addScore(score);
            System.out.println("Your score: " + score.getPoints());

            handleRating();
            askForNewGame();
        }

    }

    private void initializeLevel() {
        Level level;
        String difficulty;
        Matcher matcher;

        do{
            System.out.println("Please enter the level difficulty(1 - EASY, 2 - MEDIUM, 3 - HARD): ");
            difficulty = scanner.nextLine().replaceAll("\\s", "");

            while(difficulty.isEmpty()){
                difficulty = scanner.nextLine().replaceAll("\\s", "");
            }

            matcher = SELECT_DIFFICULTY.matcher(difficulty);
        } while(!matcher.matches());

        if(!levelService.checkConection()) {
            levelService = new LevelServiceLocal();
        }

        level = levelService.getLevel(Integer.parseInt(difficulty), playedLevels);

        field.generate(level);
        levelName = level.getName();
        playedLevels.add(levelName);
    }

    private void printTopScrores() {
        List<Score> scoreList = scoreService.getTopScores(GAME);
        System.out.println("TABLE OF TOP SCORES:");
        for(int idx = 0; idx < scoreList.size(); idx++){
            System.out.printf("%d. %s: %s\n", idx+1, scoreList.get(idx).getPlayer(), scoreList.get(idx).getPoints());
        }
        if(scoreList.size() == 0){
            System.out.println("  There's no score yet.");
        }

        System.out.println("----------------------");
    }

    private void gameLoop() {
        do{
            printField();
            if(field.getGameState() == GameState.PAUSED) {
                System.out.println("WARNING!!! The number u entered is already used somewhere, CHANGE IT!");
            }
            handleInput();

        } while (field.getGameState() != GameState.SOLVED);
    }

    private void handleRating() {
        Matcher matcher;
        String rating;

        do {
            System.out.println("Please rate this game (1 - 5): ");
            rating = scanner.nextLine().replaceAll("\\s", "");

            while(rating.isEmpty()){
                rating = scanner.nextLine().replaceAll("\\s", "");
            }
            matcher = RATE_THE_GAME.matcher(rating);
        } while (!matcher.matches());

        ratingService.setRating(new Rating(GAME, playerName, Integer.parseInt(rating), new Date()));

        System.out.println("Current game rating: " + ratingService.getAverageRating(GAME));
    }

    private void askForNewGame() {
        System.out.println("Would you like to play another game? (y/n): ");
        String command = scanner.next().toUpperCase();

        if("Y".equals(command) || "YES".equals(command)){
            play();
        } else {
            System.out.println("See you next time!");
            System.exit(0);
        }
    }

    private void printField(){
        Tile currTile;

        System.out.printf("      %s\n", levelName.toUpperCase());

        System.out.println("  1 2 3 4 5 6 7 8 9");
        for(int row = 0; row < field.getRowCount(); row++){
            System.out.print((char) (row + 'A'));
            for(int column = 0; column < field.getColumnCount(); column++){
                System.out.print(" ");

                currTile = field.getTile(row, column);

                if(currTile != null){
                    colorSystemOut(String.valueOf(currTile.getValue()), findColor(currTile.getSection()), currTile instanceof Assignment);
                }
            }

            System.out.println();
        }
    }

    private void handleInput() {
        System.out.println("Enter command (X - exit, 3a1 - number 3 to A1 tile, COMMENT - for comment section, REPLAY - for see the replay): ");
        String command = scanner.nextLine().toUpperCase().replaceAll("\\s", "");

        while(command.isEmpty()){
            command = scanner.nextLine().toUpperCase().replaceAll("\\s", "");
        }

        if("X".equals(command))
            System.exit(0);

        Matcher matcher = COMMAND_PATTERN.matcher(command);
        if(matcher.matches()){
            int row = command.charAt(1) - 'A';
            int column = Integer.parseInt(matcher.group(3)) - 1;
            int value = Integer.parseInt(matcher.group(1));

            field.writeNumber(row, column, value);
            return;
        }

        if("REPLAY".equals(command)){
            handleReplay();
            return;
        }

        if ("COMMENT".equals(command)) {
            handleComment();
            return;
        }

        handleInput();
    }

    private void handleReplay() {
        String command;

        int idx = 0;

        do {
            printRecord(idx);
            System.out.println("\u001B[0m" + "Enter command next/prev/exit: ");
            command = scanner.nextLine().toUpperCase();

            if("NEXT".equals(command)){
                if(record.size() > idx +1) idx++;
            } else if("PREV".equals(command)){
                if(idx > 0) idx--;
            }

        } while (!"EXIT".equals(command));
    }

    private void handleComment() {
        String comment;
        List<Comment> commentList = commentService.getComments(GAME);

        if(commentList.isEmpty()){
            System.out.println("There's no comment yet.");
        } else {
            for (Comment value : commentList) {
                printComment(value);
            }
        }

        System.out.println("Please enter your comment or X to exit comment section: ");
        comment = scanner.nextLine();

        if ("x".equals(comment) || "X".equals(comment)) {
            return;
        }

        commentService.addComment(new Comment(GAME, playerName, comment, new Date()));
    }

    private void printComment(Comment comment) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-YYYY");
        System.out.println(comment.getPlayer() + "  " + format.format(comment.getPlayedOn()));
        System.out.println(comment.getComment());
    }

    private void printRecord(int idx){
        int[][] currRecord = record.get(idx).getRecord();
        int[] currChange = record.get(idx).getChange();

        if(idx == 0){
            System.out.println("THE GAME SET UP");
        } else {
            System.out.printf("STEP %d/%d - %d %c%d\n", idx, record.size() - 1, currChange[2], currChange[0] + 'A', currChange[1] + 1);
        }

        System.out.println("  1 2 3 4 5 6 7 8 9");
        for(int row = 0; row < field.getRowCount(); row++){
            System.out.print((char) (row + 'A'));
            for(int column = 0; column < field.getColumnCount(); column++){
                System.out.print(" ");

                colorSystemOut(String.valueOf(currRecord[row][column]), findColor(field.getTile(row, column).getSection()), field.getTile(row, column) instanceof Assignment);
            }

            System.out.println();
        }
        checkRecordParametres(idx);
    }

    private void checkRecordParametres(int idx){
        if(record.size() == idx +1) {
            System.out.println("U are on the end of records. Type exit to came back to the game.");
        } else if(idx == 0){
            System.out.println("There's no previous records.");
        }
    }

    public ConsoleUI(Field field) {
        this.field = field;
        this.record = field.getRecordsList();
    }

    private static void colorSystemOut(String text, String color, boolean bold) {
        final String ANSI_RESET = "\u001B[0m";
        final String HIGH_INTENSITY	= "\u001B[1m";

        StringBuilder cString = new StringBuilder();

        if(text.equals("0")) text = "-";

        if(bold) cString.append(HIGH_INTENSITY);

        cString.append(color + text + ANSI_RESET);

        System.out.print(cString.toString());
    }

    private String findColor(Section section){

        final String ANSI_BLACK = "\u001B[30m";
        final String ANSI_RED = "\u001B[31m";
        final String ANSI_GREEN = "\u001B[32m";
        final String ANSI_YELLOW = "\u001B[33m";
        final String ANSI_BLUE = "\u001B[34m";
        final String ANSI_PURPLE = "\u001B[35m";
        final String ANSI_CYAN = "\u001B[36m";
        final String ANSI_WHITE = "\u001B[37m";
        final String MINE = "\u001B[53m";

        if(section == Section.A) return ANSI_BLUE;
        if(section == Section.B) return ANSI_RED;
        if(section == Section.C) return ANSI_GREEN;
        if(section == Section.D) return ANSI_YELLOW;
        if(section == Section.E) return ANSI_PURPLE;
        if(section == Section.F) return ANSI_BLACK;
        if(section == Section.G) return ANSI_WHITE;
        if(section == Section.H) return ANSI_CYAN;
        if(section == Section.I) return MINE;

        return ANSI_BLACK;
    }
}

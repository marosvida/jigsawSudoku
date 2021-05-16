package gamestudio.game.jigsawSudoku.core;

import gamestudio.entity.Level;

import java.util.ArrayList;

public class Field {
    private final int rowCount = 9;
    private final int columnCount = 9;
    private final Tile[][] gameField;
    private final ArrayList<Record> recordsList = new ArrayList<>();
    private GameState gameState;
    private int usedRow = -1, usedColumn = -1;
    private long startMillis;
    private int difficulty;


    public void generate(Level level){
        startMillis = System.currentTimeMillis();
        difficulty = level.getDifficulty();
        gameState = GameState.PLAYING;

        generateGameField(level.getLevel());

        //add first record
        recordsList.add(new Record(0, 0, 0, gameField));
    }

    public void writeNumber(int row, int column, int value){
        //check for valid input
        if(!checkInput(row, column, value)) return;

        //Tile must be instance of Guess
        if (gameField[row][column] instanceof Guess) {

            if (gameState == GameState.PAUSED) {
                if (row == usedRow && column == usedColumn) {

                    ((Guess) gameField[row][column]).setValue(value);

                    gameState = GameState.PLAYING;
                    usedRow = -1;
                    usedColumn = -1;
                } else {
                    //if game is PAUSED and player tries to change different Tile
                    return;
                }
            } else {
                gameField[row][column].setTileState(TileState.GUESSING);
                ((Guess) gameField[row][column]).setValue(value);
            }

            //add record
            recordsList.add(new Record(row, column, value, gameField));

            //check for if is number already used
            if(isNumberUsed(row, column, value)){
                this.gameState = GameState.PAUSED;
                return;
            }

            if(isSolved()) this.gameState = GameState.SOLVED;
        }
    }

    public void writeHelpingGuess(int row, int column, int help){
        //check for valid input
        if(!checkInput(row, column, help)) return;

        if(gameField[row][column] instanceof Guess){
            gameField[row][column].setTileState(TileState.HELPING);
            ((Guess) gameField[row][column]).setHelp(help == 0 ? 0 : help - 1, help);
        }
    }

    private boolean isSolved(){
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < columnCount; j++){
                if(gameField[i][j].getValue() == 0) return false;
            }
        }

        return true;
    }


    private boolean isNumberUsed(int row, int column, int value){
        if(value == 0) return false;

        //check for row
        if(usedInRow(row, column, value)){
            return true;
        }

        //check for column
        if(usedInColumn(row, column, value)){
            return true;
        }

        //check for section
        if(usedInSection(row, column, value)){
            return true;
        }

        return false;
    }

    private boolean usedInRow(int row, int column, int value) {
        for(int i = 0; i < rowCount; i++){
            if(i == column) continue;
            if(gameField[row][i].getValue() == value) {
                usedRow = row;
                usedColumn = column;
                return true;
            }
        }
        return false;
    }

    private boolean usedInColumn(int row, int column, int value) {
        for(int i = 0; i < columnCount; i++){
            if(i == row) continue;
            if(gameField[i][column].getValue() == value) {
                usedRow = row;
                usedColumn = column;
                return true;
            }
        }
        return false;
    }

    private boolean usedInSection(int row, int column, int value) {
        Section section = gameField[row][column].getSection();
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < columnCount; j++){
                if(i == row && j == column) continue;
                if(gameField[i][j].getSection() == section)
                    if(gameField[i][j].getValue() == value) {
                        usedRow = row;
                        usedColumn = column;
                        return true;
                    }
            }
        }
        return false;
    }

    private void generateGameField(String level) {
        int idx = 0;
        for(int row = 0; row < rowCount; row++){
            for(int column = 0; column < columnCount; column++){
                setTileInField(row, column, level.charAt(idx++) - '0', level.charAt(idx));
                idx++;
            }
        }
    }

    private void setTileInField(int row, int column, int value, char section){
        //check for valid input
        if(!checkInput(row, column, value)) return;

        if(value > 0){
            gameField[row][column] = new Assignment(value, Section.getSection(section));
        } else {
            gameField[row][column] = new Guess(Section.getSection(section));
        }
    }

    private boolean checkInput(int row, int column, int value) {
        if(row < 0  || row > 8) return false;
        if(column < 0 || column > 8 ) return false;
        if(value < 0 || value > 9) return false;
        return true;
    }

    public int getScore(){
        return ((1000 * difficulty) - (int) (System.currentTimeMillis() - startMillis) / 1000) * difficulty;
    }

    public Field(Level level) {
        this.gameState = GameState.PLAYING;
        gameField = new Tile[9][9];
        this.generate(level);
    }

    public Field() {
        this.gameState = GameState.PLAYING;
        gameField = new Tile[9][9];
    }

    public Tile getTile(int row, int column) {
        return gameField[row][column];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public GameState getGameState() {
        return gameState;
    }

    public ArrayList<Record> getRecordsList() {
        return recordsList;
    }

    public int getUsedRow() {
        return usedRow;
    }

    public int getUsedColumn() {
        return usedColumn;
    }
}

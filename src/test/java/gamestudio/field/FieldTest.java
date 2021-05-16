package gamestudio.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import gamestudio.entity.Level;
import gamestudio.game.jigsawSudoku.core.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class FieldTest {
    private final Level level1 =  new Level("level1", 1, "0A0B4B0B0B0B3B0B0C0A9A0A4A0A1D0B5E0C6F0F0A0A0A0D0B0E4C0F8F0F7F0F4D0D3E0C0F0F0D0D5D0D0E0E0C0G1G0D9H0E6E0E4E0C2G0H0H0H0I0I0I0I3C0G5G0G8H0H2H0I1C0C0G0G9G0H0H0I2I0I0I");
    private final Random randomGenerator = new Random();
    private final Field field;
    private Tile currTile;

    public FieldTest() {
        field = new Field();
    }

    //test for correctly generate tiles values
    @Test
    public void generateTestTilesValues(){
        field.generate(level1);

        int idx = 0;
        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                currTile = field.getTile(row, column);

                assertEquals((level1.getLevel().charAt(idx) - '0'), currTile.getValue(), "Field was initialized incorrectly - wrong values was set to the tiles.");
                idx+=2;
            }
        }
    }

    //test for correctly generate tiles sections
    @Test
    public void generateTestTilesSections(){
        field.generate(level1);

        int idx = 1;
        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                currTile = field.getTile(row, column);

                //test for section
                Section sec = currTile.getSection();
                assertEquals(level1.getLevel().charAt(idx), Section.getChar(sec), "Field was initialized incorrectly - wrong sections was added to the tiles.");
                idx+=2;
            }
        }
    }

    //test for no overwritting assignemt tiles
    @Test
    public void writeNumberTestAssignment(){
        field.generate(level1);

        int randNum;
        int assignment;

        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                currTile = field.getTile(row, column);

                randNum = randomGenerator.nextInt(10);

                if(currTile.getTileState() == TileState.FINAL) {
                    assignment = currTile.getValue();
                    field.writeNumber(row, column, randNum);
                    assertEquals(assignment, currTile.getValue(), "WRONG!! The number was written into the ASSIGNMENT TILE!");
                }
            }
        }
    }

    //test for write number into guess tile, when gameState == PLAYING
    @Test
    public void writeNumberTestGuess(){
        field.generate(level1);
        int randNum;

        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                currTile = field.getTile(row, column);

                randNum = randomGenerator.nextInt(10);

                if(currTile.getTileState() != TileState.FINAL && field.getGameState() == GameState.PLAYING) {
                    field.writeNumber(row, column, randNum);
                    assertEquals(randNum, currTile.getValue(), "The number was not written into GUESS TILE!");
                }
            }
        }
    }

    //test for unpause the game, after it was paused and 0 has been written
    @Test
    public void writeNumberTestIsUsed(){
        field.generate(level1);
        int usedNum;

        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                currTile = field.getTile(row, column);

                usedNum = findUsedNumber(row, column);

                //no used num find, so go on another tile
                if(usedNum == 0) continue;

                //test for UNPAUSE the game, after pausing, and the 0 has been written
                if(currTile.getTileState() != TileState.FINAL && field.getGameState() == GameState.PLAYING) {
                    field.writeNumber(row, column, usedNum);
                    assertEquals(GameState.PAUSED, field.getGameState(), "After wrote the used number, gameState did not change to PAUSED!");
                    field.writeNumber(row, column, 0);
                    assertEquals(GameState.PLAYING, field.getGameState(), "After deleting the used number with 0, gameState did not change back to PLAYING!");
                }
            }
        }
    }

    //test for game stays paused after again used number has been written
    @Test
    public void writeNumberTestByUsedNumberAgain(){
        field.generate(level1);
        int usedNum;

        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                currTile = field.getTile(row, column);

                usedNum = findUsedNumber(row, column);

                //no used num find, so go on another tile
                if(usedNum == 0) continue;

                //test for not to UNPAUSE the game if another(or same) used number has been written
                if(currTile.getTileState() != TileState.FINAL && field.getGameState() == GameState.PLAYING) {
                    field.writeNumber(row, column, usedNum);
                    assertEquals(GameState.PAUSED, field.getGameState(), "After wrote the used number, gameState did not change to PAUSED!");
                    field.writeNumber(row, column, usedNum);
                    assertEquals(GameState.PAUSED, field.getGameState(), "GameState did not stay to PAUSED after agin used number has been written");
                }
            }
        }
    }

    //test for after using writeHelpingGuess the TileState on Assignment Tile did not changed
    @Test
    public void writeHelpingGuessTileStateAssignmentTest(){
        field.generate(level1);
        int randHelp;

        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                randHelp = randomGenerator.nextInt(9) + 1;
                currTile = field.getTile(row, column);

                field.writeHelpingGuess(row, column, randHelp);

                //test for unchanged TileState on ASSIGNMENT
                if(currTile instanceof Assignment){
                    assertEquals(TileState.FINAL, currTile.getTileState(), "WRONG!!! There was written help guess into the ASSIGNMENT TILE!");
                }
            }
        }
    }

    //test for after using writeHelpingGuess the TileState on Guess Tile did changed to HELPING
    @Test
    public void writeHelpingGuessTileStateGuessTest(){
        field.generate(level1);
        int randHelp;

        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                randHelp = randomGenerator.nextInt(9) + 1;
                currTile = field.getTile(row, column);

                field.writeHelpingGuess(row, column, randHelp);

                //test for changed TileState on GUESS
                if(currTile instanceof Guess){
                    assertEquals(TileState.HELPING, currTile.getTileState(), "After using writeHelpingGuess, the TileState was not changed into the HELPING.");
                }
            }
        }
    }

    //test for correctly written help into help[] in GUESS
    @Test
    public void writeHelpingGuessTest(){
        field.generate(level1);
        int randHelp;
        int help[];

        for(int row = 0; row < field.getRowCount(); row++){
            for(int column = 0; column < field.getColumnCount(); column++){
                currTile = field.getTile(row, column);

                randHelp = randomGenerator.nextInt(9) + 1;

                field.writeHelpingGuess(row, column, randHelp);

                if(currTile instanceof Guess){
                    help = ((Guess) currTile).getHelp();
                    assertEquals(randHelp, help[randHelp == 0 ? 0 : randHelp-1], "After writeHelpingGuess number was not added into the help[] correctly.");
                }
            }
        }
    }


    private int findUsedNumber(int row, int column){
        //try to find used num in row
        for(int i = 0; i < field.getRowCount(); i++){
            if(field.getTile(row, i).getValue() != 0)
                return field.getTile(row, i).getValue();
        }


        //try to find used num in column
        for(int i = 0; i < field.getColumnCount(); i++){
            if(field.getTile(i, column).getValue() != 0)
                return field.getTile(i, column).getValue();
        }

        //try to find used num in section
        Section section = field.getTile(row, column).getSection();
        for(int i = 0; i < field.getRowCount(); i++){
            for(int j = 0; j < field.getColumnCount(); j++){
                if(i == row && j == column) continue;
                if(field.getTile(i, j).getSection() == section)
                    if(field.getTile(i, j).getValue() != 0) {
                        return  field.getTile(i, j).getValue();
                    }
            }
        }

        return 0;
    }
}

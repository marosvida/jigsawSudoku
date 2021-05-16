package gamestudio;

import gamestudio.game.jigsawSudoku.consoleUI.ConsoleUI;
import gamestudio.game.jigsawSudoku.core.Field;

public class Main {
    public static void main(String[] args) {
        Field field = new Field();
        ConsoleUI consoleUI = new ConsoleUI(field);
        consoleUI.run();
    }
}

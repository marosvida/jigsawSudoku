package gamestudio.game.jigsawSudoku.core;

public class Assignment extends Tile {
    public Assignment(int value, Section section) {
        super(value, section);
        this.setTileState(TileState.FINAL);
    }
}

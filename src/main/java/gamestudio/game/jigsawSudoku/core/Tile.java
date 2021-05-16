package gamestudio.game.jigsawSudoku.core;

public abstract class Tile {
    private TileState tileState;
    protected int value;
    private Section section;

    public Tile(int value, Section section) {
        this.value = value;
        this.section = section;
    }

    public int getValue() {
        return value;
    }

    public Section getSection() {
        return section;
    }

    public TileState getTileState() {
        return tileState;
    }

    void setTileState(TileState tileState) {
        this.tileState = tileState;
    }
}

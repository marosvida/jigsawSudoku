package gamestudio.game.jigsawSudoku.core;

public class Guess extends Tile {
    private int[] help = new int[9];

    public Guess(Section section) {
        super(0, section);
        this.setTileState(TileState.GUESSING);
    }



    public int[] getHelp() {
        return help;
    }

    public int getNumber(int idx){
        return help[idx];
    }

    public void setHelp(int idx, int val) {
        this.help[idx] = val;
    }

    void setValue(int value) {
        this.value = value;
    }
}

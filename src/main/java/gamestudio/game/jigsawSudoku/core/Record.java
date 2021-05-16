package gamestudio.game.jigsawSudoku.core;

public class Record {
    private int[][] record;
    private int[] change = new int[3];

    private void fillRecord(Tile[][] record){
        for(int row = 0; row < 9; row++){
            for(int column = 0; column < 9; column++){
                this.record[row][column] = record[row][column].getValue();
            }
        }
    }

    public Record(int row, int column, int value, Tile[][] record){
        this.record = new int[9][9];
        fillRecord(record);

        this.change[0] = row;
        this.change[1] = column;
        this.change[2] = value;
    }

    public int[][] getRecord() {
        return record;
    }

    public int[] getChange() {
        return change;
    }
}

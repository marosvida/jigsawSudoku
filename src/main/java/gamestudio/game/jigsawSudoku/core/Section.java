package gamestudio.game.jigsawSudoku.core;

public enum Section {
    A,B,C,D,E,F,G,H,I;

    public static Section getSection(char chr){
        if(chr == 'A') return Section.A;
        if(chr == 'B') return Section.B;
        if(chr == 'C') return Section.C;
        if(chr == 'D') return Section.D;
        if(chr == 'E') return Section.E;
        if(chr == 'F') return Section.F;
        if(chr == 'G') return Section.G;
        if(chr == 'H') return Section.H;
        if(chr == 'I') return Section.I;

        return null;
    }

    public static char getChar(Section sec){
        if(sec == A) return 'A';
        if(sec == B) return 'B';
        if(sec == C) return 'C';
        if(sec == D) return 'D';
        if(sec == E) return 'E';
        if(sec == F) return 'F';
        if(sec == G) return 'G';
        if(sec == H) return 'H';
        if(sec == I) return 'I';

        return 'Z';
    }
}

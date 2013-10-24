package name.paramonov.doomep.shogi;

import java.util.List;

public class EmptyPiece extends Piece {
    protected int x;
    protected int y;
    protected int allegiance;


    public EmptyPiece (int x, int y) {
        this.x = x;
        this.y = y;
        this.allegiance = 0; 
    }

    public void checkMove(List<List<Piece>> board) {}
    public void move() {}
    public Piece promote() {}
}

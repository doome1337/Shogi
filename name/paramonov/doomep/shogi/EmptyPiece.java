package name.paramonov.doomep.shogi;

public class EmptyPiece extends Piece {
    /** Constructs an EmptyPiece at a given location.
     * @param   x       The x-value 
     * at which this EmptyPiece is.
     * @param   y       The y-value
     * at which this EmptyPiece is.
     */
    public EmptyPiece (int x, int y) {
        this.x = x;
        this.y = y;
        this.allegiance = 0; 
    }

    public boolean checkMove(GameState state, int x, int y) {
        return false;
    }
    
    public GameState move(GameState state, int x, int y) {
        if (this.checkMove(state, x, y)) {
            return state;
        }
    }
    
    public boolean isPromotable() {
        return false;
    }

    public Piece promote() {
        if (this.isPromotable()) {
            return this;
        }
    }
}

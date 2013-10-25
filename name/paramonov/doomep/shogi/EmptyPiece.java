package name.paramonov.doomep.shogi;
//TODO: Documentation.
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
        this.pieceName = "Empty Tile";
    }

    protected boolean isValidMove(GameState state, int x, int y) {
        return false;
    }
    
    protected boolean isPromotable() {
        return false;
    }

    protected Piece promote() {
        return this;
    }

    @Override
    protected Piece demote() {
        return this;
	}
}

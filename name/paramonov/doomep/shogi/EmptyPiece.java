package name.paramonov.doomep.shogi;

/** A class representing a tile on which there is no piece.
 * @author              Dmitry Andreevich Paramonov
 * @author              Jiayin Huang
 */
public class EmptyPiece extends Piece {
    /** Constructs an EmptyPiece at a given location.
     * @param   x       The x-value 
     * at which this EmptyPiece is.
     * @param   y       The y-value
     * at which this EmptyPiece is.
     */
    public EmptyPiece (int x, int y) {
        super(x, y, 0);
        this.pieceName = "Empty Tile";
        this.doubleCharRepresentation = new String[]{"  ","  ", "  "};
    }

    /** Returns whether or not this EmptyPiece can move to a given tile.
     * As EmptyPieces can't move, 
     * this means that this always returns false.
     * @param   state   The state of the board before the move.
     * @param   x       The x-value of the tile to which this EmptyPiece is trying to move.
     * @param   y       The y-value of the tile to which this EmptyPiece is trying to move.
     * @return          Whether this EmptyPiece can move to a given tile,
     * which is always false.
     */
    protected boolean isValidMove(GameState state, int x, int y) {
        return false;
    }
    
    /** Returns whether or not this EmptyPiece can promote
     * @return          Whether or not this EmptyPiece can promote, 
     * which is always false.
     */
    protected boolean isPromotable() {
        return false;
    }

    /** Returns the Piece this EmptyPiece promotes to.
     * As EmptyPieces don't promote, returns itself.
     * @return          The Piece this EmptyPiece promotes to,
     * which is itself.
     */
    protected Piece promote() {
        return this;
    }

    @Override
    /**
     * Returns the Piece this EmptyPiece demotes to.
     * As EmptyPieces don't demote, returns itself.
     * @return          The Piece this EmptyPiece demotes to,
     * which is itself.
     */
    protected Piece demote() {
        return this;
	}
}

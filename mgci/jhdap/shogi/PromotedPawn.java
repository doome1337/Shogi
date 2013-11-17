package mgci.jhdap.shogi;
/** A Class representing a promoted pawn in a game of shogi.
 */
public class PromotedPawn extends UnpromotablePiece {
    /** Constructs a promoted pawn at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this promoted pawn is located.
     * @param   y           The y-value at which this promoted pawn is located.
     * @param   allegiance  The allegiance of this promoted pawn.
     */
    public PromotedPawn (int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Pawn";
        this.doubleCharRepresentation = new String[]{"p+","P*", "P+"};
        this.imageNames = new String[]{"dPPawn.png", "nPPawn.png", "uPpawn.png"};
	}
    
    /** Returns whether a move can be undertaken by this PromotedPawn.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this PromotedPawn can move to the given x and y values.
     */
	@Override
    protected boolean isValidNonDropMove(GameState state, int x, int y) {
	    /* Checks if the target tile is in front 
	     * and in one of three horizontal directions, 
	     * at the side, or directly behind.
	     * Then verifies if the target tile is still in the board, 
	     * and whether the target tile could be moved into.
	     * */
	    return ((((y == this.y+this.allegiance) 
	               && (x == this.x-1 || x == this.x || x == this.x+1)) 
	              || ((y == this.y) 
	               && (x == this.x-1 || x == this.x+1))
	              || ((y == this.y-this.allegiance) 
	               && (x == this.x))) 
	              && !(y < 0 || y > 8)
	              && !(x < 0 || x > 8)
	               && (state.getPieceAt(x, y).getAllegiance() != this.allegiance));
	}

	/** Returns the piece this promoted pawn demotes to.
     * In this case, this is a pawn with the same x and y values, 
     * and the same allegiance.
     * @return              The pawn equivalent of this promoted pawn.
     */
	@Override
    protected Piece demote() {
		return new Pawn(this.x, this.y, this.allegiance);
	}
}

package mgci.jhdap.shogi;

/** A Class representing a promoting rook in a game of shogi.
 */
public class PromotedRook extends UnpromotablePiece {
    /** Constructs a promoted rook at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this promoted rook is located.
     * @param   y           The y-value at which this promoted rook is located.
     * @param   allegiance  The allegiance of this promoted rook.
     */
    public PromotedRook(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Rook";
        this.doubleCharRepresentation = new String[]{"r+","R*", "R+"};
        this.imageNames = new String[]{"dPRook.png", "nPRook.png", "uPRook.png"};
    }
    
    /** Checks whether this PromotedRook can move to a given location.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this PromotedRook can move to the target tile.
     */
    @Override
    protected boolean isValidNonDropMove(GameState state, int x, int y) {
        //TODO: Doublecheck this thing. Is messy.
        /* Begins by verifying if the target tile is with the board.
         * Then checks if the change in x or y values is 0 (same row/column).
         * Then verifies if all the tiles on the way to the target tile are empty.
         * Finally, verifies if the target tile can be entered.
         * Then checks to see if target tile is adjacent diagonally.
         * Finally, we or both obtained values.
         */
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove1 = !(x < 0 && x > 8) && !(y < 0 && y > 8);
        boolean validMove2 = !(x < 0 && x > 8) && !(y < 0 && y > 8);
        if (validMove1 && dy == 0 && dx != 0){
            for (int i = 1; validMove1 && i < Math.abs(dx); i++) {
                validMove1 = state.getPieceAt(this.x+i*((int)Math.signum(dx)), y) instanceof EmptyPiece;
            }
            if (validMove1) {
                validMove1 = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else if (validMove1 && dx == 0 && dy != 0) {
            for (int i = 1; validMove1 && i*Math.signum(dy) < dy; i++) {
                validMove1 = state.getPieceAt(x, this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
            }
            if (validMove1) {
                validMove1 = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else {
            validMove1 = false;
        }
        if (validMove2) {
            validMove2 = Math.abs(dy) == 1 && Math.abs(dx) == 1 && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        }
        return validMove1 || validMove2;
    }

    /** Returns the piece this promoted rook demotes to.
     * In this case, this is a rook with the same x and y values, 
     * and the same allegiance.
     * @return              The rook equivalent of this promoted rook.
     */
    @Override
    protected Piece demote() {
        return new Rook(this.x, this.y, this.allegiance);
    }
}

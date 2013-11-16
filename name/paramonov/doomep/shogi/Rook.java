package name.paramonov.doomep.shogi;

/** A Class representing a rook in a game of shogi.
 */
public class Rook extends PromotablePiece {
    /** Constructs a rook at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this rook is located.
     * @param   y           The y-value at which this rook is located.
     * @param   allegiance  The allegiance of this rook.
     */
    public Rook(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Rook";
        this.doubleCharRepresentation = new String[]{"r ","R#", "R "};
        this.imageNames = new String[]{"dRook.png", "nRook.png", "uRook.png"};
    }

    /** Checks whether this Rook can move to a given location.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this Rook can move to the target tile.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        /* Begins by verifying if the target tile is with the board.
         * Then checks if the change in x or y values is 0 (same row/column).
         * Then verifies if all the tiles on the way to the target tile are empty.
         * Finally, verifies if the target tile can be entered.
         */
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove = !(x < 0 || x > 8) && !(y < 0 || y > 8);
        if (validMove && dy == 0 && dx != 0){
            for (int i = 1; validMove && i < Math.abs(dx); i++) {
                validMove = state.getPieceAt(this.x+i*((int)Math.signum(dx)), y) instanceof EmptyPiece;
            }
            if (validMove) {
                validMove = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else if (validMove && dx == 0 && dy != 0) {
            for (int i = 1; validMove && i < Math.abs(dy); i++) {
                validMove = state.getPieceAt(x, this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
            }
            if (validMove) {
                validMove = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else {
            validMove = false;
        }
        return validMove;
    }
    
    /** Returns the Piece this rook promotes to.
     * Creates a PromotedRook with the same allegiance 
     * and location as this rook.
     * @return              The PromotedRook equivalent of this Rook.
     */
    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedRook(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    /** Returns the piece this Rook demotes to.
     * As rooks are not a promoted piece, 
     * demoting them has no effect.
     * @return              This Rook.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

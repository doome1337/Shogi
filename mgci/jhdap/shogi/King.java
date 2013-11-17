package mgci.jhdap.shogi;

/** A Class representing a king in a game of shogi.
 */
public class King extends UnpromotablePiece {
    /** Constructs a king at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this king is located.
     * @param   y           The y-value at which this king is located.
     * @param   allegiance  The allegiance of this king.
     */
    public King(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "King";
        this.doubleCharRepresentation = new String[]{"k ","K#", "K "};
        this.imageNames = new String[]{"dKing.png", "nKing.png", "uKing.png"};
        this.checkmatable = true;
    }
    
    /** Checks whether this King can move to a given location.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this King can move to the target tile.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        /* Checks to see if the target tile is at most one tile away. 
         * Then verifies if the target tile is within the board.
         * Then checks to see if the target tile can be moved into.
         * Then, checks to see if the King would be attacked after moving.
         */
        boolean validMove = Math.abs(this.x-x) < 2 && Math.abs(this.y-y) < 2 
                        && !(x < 0 || x > 8)
                        && !(y < 0 || y > 8)
                         && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        state.setPieceAt(this.x, this.y, new EmptyPiece(this.x, this.y));
        Piece tempRep = state.getPieceAt(x, y);
        state.setPieceAt(x, y, this);
        validMove = validMove && !state.isAttacked(x, y, -this.allegiance);
        state.setPieceAt(x, y, tempRep);
        state.setPieceAt(this.x, this.y, this);
        return validMove;
    }
    
    /** Checks whether this King can move to a given location,
     * without verifying for check.
     * This is to prevent infinite loops of each king checking for the other.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this King can move to the target tile.
     */
    @Override
    protected boolean isUncheckedMove(GameState state, int x, int y) {
        /* Checks to see if the target tile is at most one tile away. 
         * Then verifies if the target tile is within the board.
         * Then checks to see if the target tile can be moved into.
         */
        boolean validMove = Math.abs(this.x-x) < 2 && Math.abs(this.y-y) < 2 
                        && !(x < 0 || x > 8)
                        && !(y < 0 || y > 8)
                         && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        return validMove;    
    }

    /** Returns the piece this king demotes to.
     * As kings are not a promoted piece, 
     * demoting them has no effect.
     * @return              This king.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

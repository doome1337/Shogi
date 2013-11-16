package mgci.jhdap.shogi;

/** A Class representing a bishop in a game of shogi.
 */
public class Bishop extends PromotablePiece {
    /** Constructs a bishop at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this bishop is located.
     * @param   y           The y-value at which this bishop is located.
     * @param   allegiance  The allegiance of this bishop.
     */
    public Bishop(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Bishop";
        this.doubleCharRepresentation = new String[]{"b ","B#", "B "};
        this.imageNames = new String[]{"dBishop.png", "nBishop.png", "uBishop.png"};
    }

    /** Checks whether this Bishop can move to a given location.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this Bishop can move to the target tile.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        /* Begins by verifying if a move is within the range of the board.
         * Then verifies if a move is on one of the diagonals (the change in x equals the change in y).
         * Then it checks to see if all tile on the way to the target tile is empty.
         * Finally, it check whether the target tile can be landed on.
         */
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove = !(x < 0 || x > 8) && !(y < 0 || y > 8);
        if (validMove && Math.abs(dx) == Math.abs(dy) && dy != 0) {
            for (int i = 1; validMove && i < Math.abs(dy); i++) {
                validMove = state.getPieceAt(this.x+i*((int)Math.signum(dx)), this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
            }
            if (validMove) {
                validMove = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else {
            validMove = false;
        }
        return validMove;
    }

    /** Returns the Piece this bishop promotes to.
     * Creates a PromotedBishop with the same allegiance 
     * and location as this bishop.
     * @return              The PromotedBishop equivalent of this Bishop.
     */
    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedBishop(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    /** Returns the piece this Bishop demotes to.
     * As bishops are not a promoted piece, 
     * demoting them has no effect.
     * @return              This Bishop.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

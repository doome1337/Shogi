package mgci.jhdap.shogi;

/** A Class representing a promoted bishop in a game of shogi.
 */
public class PromotedBishop extends UnpromotablePiece {
    /** Constructs a promoted bishop at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this promoted bishop is located.
     * @param   y           The y-value at which this promoted bishop is located.
     * @param   allegiance  The allegiance of this promoted bishop.
     */
    public PromotedBishop(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Bishop";
        this.doubleCharRepresentation = new String[]{"b+","B*", "B+"};
        this.imageNames = new String[]{"dPBishop.png", "nPBishop.png", "uPBishop.png"};
    }

    /** Checks whether this PromotedBishop can move to a given location.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this PromotedBishop can move to the target tile.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: Doublecheck this thing. Is messy.
        /* Begins by verifying if a move is within the range of the board.
         * Then verifies if a move is on one of the diagonals (the change in x equals the change in y).
         * Then it checks to see if all tile on the way to the target tile is empty.
         * Finally, it check whether the target tile can be landed on.
         * It then checks to see if a tile is horizontally adjacent.
         * If one of those is true, this method returns true.
         */
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove1 = !(x < 0 || x > 8) && !(y < 0 || y > 8);
        boolean validMove2 = !(x < 0 || x > 8) && !(y < 0 || y > 8);
        if (validMove1 && dx*Math.signum(dx) == dy*Math.signum(dy) && dy != 0) {
            for (int i = 1; validMove1 && i < Math.abs(dy); i++) {
                validMove1 = state.getPieceAt(this.x+i*((int)Math.signum(dx)), this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
            }
            if (validMove1) {
                validMove1 = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else {
            validMove1 = false;
        }
        if (validMove2) {
            validMove2 = ((Math.abs(dx) == 1 && dy == 0) || (Math.abs(dy) == 1 && dx == 0)) && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        }
        return validMove1 || validMove2;
    }

    /** Returns the piece this promoted bishop demotes to.
     * In this case, this is a bishop with the same x and y values, 
     * and the same allegiance.
     * @return              The bishop equivalent of this promoted bishop.
     */
    @Override
    protected Piece demote() {
        return new Bishop(this.x, this.y, this.allegiance);
    }

}

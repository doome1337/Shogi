package mgci.jhdap.shogi;
/** A Class representing a promoted silver general in a game of shogi.
 */
public class PromotedSilverGeneral extends UnpromotablePiece {
    /** Constructs a promoted silver general at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this promoted silver general is located.
     * @param   y           The y-value at which this promoted silver general is located.
     * @param   allegiance  The allegiance of this promoted silver general.
     */
    public PromotedSilverGeneral(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Silver General";
        this.doubleCharRepresentation = new String[]{"s+","S*", "S+"};
        this.imageNames = new String[]{"dPSilver.png", "nPSilver.png", "uPSilver.png"};
    }

    /** Returns whether a move can be undertaken by this PromotedSilverGeneral.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this PromotedSilverGeneral can move to the given x and y values.
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

    /** Returns the piece this promoted silver general demotes to.
     * In this case, this is a silver general with the same x and y values, 
     * and the same allegiance.
     * @return              The silver general equivalent of this promoted silver general.
     */
    @Override
    protected Piece demote() {
        return new SilverGeneral(this.x, this.y, this.allegiance);
    }
}

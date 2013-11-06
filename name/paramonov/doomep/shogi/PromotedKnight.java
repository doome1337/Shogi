package name.paramonov.doomep.shogi; 
/** A Class representing a promoted knight in a game of shogi.
 */
public class PromotedKnight extends UnpromotablePiece {
    /** Constructs a promoted knight at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this promoted knight is located.
     * @param   y           The y-value at which this promoted knight is located.
     * @param   allegiance  The allegiance of this promoted knight.
     */
    public PromotedKnight(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Knight";
        this.doubleCharRepresentation = new String[]{"n+","N*", "N+"};
    }

    /** Returns whether a move can be undertaken by this PromotedKnight.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this PromotedKnight can move to the given x and y values.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
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

    /** Returns the piece this promoted knight demotes to.
     * In this case, this is a knight with the same x and y values, 
     * and the same allegiance.
     * @return              The knight equivalent of this promoted knight.
     */
    @Override
    protected Piece demote() {
        return new Knight(this.x, this.y, this.allegiance);
    }
}

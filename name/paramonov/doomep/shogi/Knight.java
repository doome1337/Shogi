package name.paramonov.doomep.shogi;
/** A Class representing a knight in a game of shogi.
 */
public class Knight extends PromotablePiece {
    /** Constructs a knight at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this knight is located.
     * @param   y           The y-value at which this knight is located.
     * @param   allegiance  The allegiance of this knight.
     */
    public Knight(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Knight";
        this.doubleCharRepresentation = new String[]{"n ","N#", "N "};
    }
    
    /** Returns whether a move can be undertaken by this Knight.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this Knight can move to the given x and y values.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: Comment.
        return (((y == this.y+2*this.allegiance) 
                && (x == this.x-1 || x == this.x+1))  
               && !(y < 0 || y > 8)
               && !(x < 0 || x > 8)
                && (state.getPieceAt(x, y).getAllegiance() != this.allegiance));
    }
    
    /** Returns the Piece this knight promotes to.
     * Creates a PromotedKnight with the same allegiance 
     * and location as this knight.
     * @return              The PromotedKnight 
     * equivalent of this Knight.
     */
    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedKnight(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }
    
    /** Returns the piece this knight demotes to.
     * As knights are not a promoted piece, 
     * demoting them has no effect.
     * @return              This knight.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

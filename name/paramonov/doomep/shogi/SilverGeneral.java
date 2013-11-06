package name.paramonov.doomep.shogi;

/** A Class representing a silver general in a game of shogi.
 */
public class SilverGeneral extends PromotablePiece {
    /** Constructs a silver general at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this silver general is located.
     * @param   y           The y-value at which this silver general is located.
     * @param   allegiance  The allegiance of this silver general.
     */
    public SilverGeneral(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Silver General";
        this.doubleCharRepresentation = "S ";
    }
    
    /** Returns whether a move can be undertaken by this SilverGeneral.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this SilverGeneral can move to the given x and y values.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: Comment.
        return ((((y == this.y+this.allegiance) 
               && (x == this.x-1 || x == this.x || x == this.x+1)) 
              || ((y == this.y-this.allegiance) 
               && (x == this.x-1 || x == this.x+1))) 
              && !(y < 0 || y > 8)
              && !(x < 0 || x > 8)
               && (state.getPieceAt(x, y).getAllegiance() != this.allegiance));
    }

    /** Returns the Piece this silver general promotes to.
     * Creates a PromotedSilverGeneral with the same allegiance 
     * and location as this silver general.
     * @return              The PromotedSilverGeneral 
     * equivalent of this SilverGeneral.
     */
    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedSilverGeneral(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    /** Returns the piece this silver general demotes to.
     * As silver generals are not a promoted piece, 
     * demoting them has no effect.
     * @return              This silver general.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

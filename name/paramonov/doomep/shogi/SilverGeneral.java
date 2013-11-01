package name.paramonov.doomep.shogi;

/** A Class representing a silver general in a game of shogi.
 */
public class SilverGeneral extends PromotablePiece {
    /** Constructs a silver general at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this pawn is located.
     * @param   y           The y-value at which this pawn is located.
     * @param   allegiance  The allegiance of this pawn.
     */
    public SilverGeneral(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Silver General";
    }
    
    /** Constructs a lance at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this lance is located.
     * @param   y           The y-value at which this lance is located.
     * @param   allegiance  The allegiance of this lance.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
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

    /** Returns the piece this pawn demotes to.
     * As silver generals are not a promoted piece, 
     * demoting them has no effect.
     * @return              This silver general.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

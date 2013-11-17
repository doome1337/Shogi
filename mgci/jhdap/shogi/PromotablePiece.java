package mgci.jhdap.shogi;

/** An abstract class representing a piece that can promote.
 * @author      Dmitry Andreevich Paramonov
 * @author      Jiaying Huang
 */
public abstract class PromotablePiece extends Piece {
    /** Constructs a promotable piece at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this piece is located.
     * @param   y           The y-value at which this piece is located.
     * @param   allegiance  The allegiance of this piece.
     */
    public PromotablePiece(int x, int y, int allegiance) {
        super(x, y, allegiance);
    }
    
    protected abstract boolean isValidNonDropMove(GameState state, int x, int y);

    /** Returns whether or not this PromotablePiece can promote.
     * @return  Whether the pawn can promote 
     * in its current location.
     */
    protected boolean isPromotable () {
        /* Uses the formula:
        (5+a)a &lt; ay,
        where a is the allegiance number, 
        and y is the y-value of this pawn.
        If allegiance is +!, 
        then pawns promote in ranks 7, 8 and 9.
        If allegiance is -1, 
        then pawns promote in ranks 1, 2 and 3.
        If allegiance is +1, 
        the formula is 6 &lt; y, 
        which is true for ranks 7, 8 and 9.
        If allegiance is -1,
        the formula is -4 &lt; -y,
        which is true for ranks 1, 2 and 3.
        Therefore the function works 
        for all necessary occasions.*/
        return ((4 + this.allegiance) * this.allegiance 
                < this.y * this.allegiance);
    }

    protected abstract Piece promote();

    protected abstract Piece demote();
}

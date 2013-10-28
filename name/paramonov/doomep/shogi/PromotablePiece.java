package name.paramonov.doomep.shogi;

/** An abstract class representing a piece that can promote.
 * @author      Dmitry Andreevich Paramonov
 * @author      Jiaying Huang
 */
public abstract class PromotablePiece extends Piece {
    protected abstract boolean isValidMove(GameState state, int x, int y);

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

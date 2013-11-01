package name.paramonov.doomep.shogi;
/** An abstract class representing a piece that cannot promote.
 * @author      Dmitry Andreevich Paramonov
 * @author      Jiaying Huang
 */
public abstract class UnpromotablePiece extends Piece {
    /** Constructs an unpromotable piece at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this piece is located.
     * @param   y           The y-value at which this piece is located.
     * @param   allegiance  The allegiance of this piece.
     */
    public UnpromotablePiece(int x, int y, int allegiance) {
        super(x, y, allegiance);
    }
    
    protected abstract boolean isValidMove(GameState state, int x, int y);

    /** Returns whether or not this piece can promote.
     * @return  Whether or not this piece can promote,
     * which is always false.
     */
    protected boolean isPromotable() {
        return false;
    }

    /** Returns the piece this piece promotes to.
     * As this piece doesn't promote, this method returns itself.
     * @return  Returns itself.
     */
    protected Piece promote() {
        return this;
    }

    protected abstract Piece demote();
}

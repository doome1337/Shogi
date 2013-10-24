package name.paramonov.doomep.shogi;

import java.util.List;

/** A Class representing a piece in a game of shogi.
 * This class defines the basic requirements for a piece: 
 * <ul>
 * <li> Has a given location.
 * <li> Has an allegiance to a side.
 * <li> Is promoted.
 * <li> Is located on the board or on in the drop table.
 * </ul>
 * All pieces extend this class, and implement different rules of checking for valid moves and promotion.
 * @author          Dmitry Andreevich Paramonon
 * @author          Jiayin Huang
 */
public abstract class Piece {
    /** The y-value of the first row on the board.
     * Used before the starting board value is finalized.
     */
    protected static final int STARTING_BOARD_Y = 1;

    /** The x-value of this piece on the gameboard.
     * Will be in a range from 1 to 9.
     */
    protected int x;
    
    /** The y-value of this piece on the gameboard.
     * Will be in a range from 1 to 9.
     */
    protected int y;
    
    /** This piece's allegiance.
     * Equivalent to a piece's color in chess.
     * The side that this piece belongs to.
     * Possible values are:
     * <ul>
     * <li> 1: "White" (Bottom side).
     * <li> 0: "Mercenary" (Neutral).
     * <li> -1: "Black" (Top side).
     * </ul>
     * Mercenary pieces can be captured by either side, and then dropped. They do not move, and cannot attack any pieces.
     */
    protected int allegiance;

    /** Returns whether a move can be undertaken by this Piece.
     * Each piece has different rules, so this one is to be implemented individually.
     * @param   board   The current state of the board at the time of verification. 
     * @param   x       The x-value to which this piece is trying to move.
     * @param   y       The y-value to which this piece is trying to move.
     * @return          Whether this Piece can move to the given x and y values.
     */
    public abstract boolean checkMove (List<List<Piece>> board, int x, int y); 
    
    /** Moves this piece to a different location. 
     * Changes the x and y-values of this Piece, and then returns a board style List&lt;List&lt;Piece&gt;&gt;
     * @param   board   The currect state of the board at the time of this method being run.
     * @param   x       The x-value to which this piece is trying to move.
     * @param   y       The y-value to which this piece is trying to move.
     * @return          The state of the board after this piece has been moved.
     */
    public abstract List<List<Piece>> move (List<List<Piece>> board, int x, int y);

    /** Verifies whether this piece can be promoted. 
     * This is placed here so that calling .getPromotable() can be done on any piece, 
     * which allows us to find out exactly which pieces can be promoted.
     * If it was implemented in a different class, that would cause trouble if we were to verify for any piece.
     * @return          Whether or not the piece can be promoted.
     */
    public abstract boolean isPromotable ();

    /** Returns the piece this piece is promoted to.
     * Each piece promotes to a different piece, 
     * and therefore is implemented individually in each subclass.
     * @return          The piece this piece promotes to.
     */
    public abstract Piece promote ();

    /** Returns this piece's x-value.
     * @return          This piece's x location on the board.
     */
    public int getX () {
        return x;
    }

    /** Returns this piece's y-value.
     * @return          This piece's y location on the board.
     */
    public int getY () {
        return y;
    }

    /** Returns this piece's allegiance.
     * Possible values are:
     * <ul>
     * <li> 1: "White" (Bottom side).
     * <li> 0: "Mercenary" (Neutral).
     * <li> -1: "Black" (Top side).
     * </ul>
     * Mercenary pieces can be captured by either side, and then dropped. They do not move, and cannot attack any pieces.
     * @return          The value of this piece's allegiance.
     */
    public int getAllegiance () {
        return allegiance;
    }

    /** Sets this piece's allegiance.
     * Use for capturing pieces.
     * Possible values listed above.
     * Method has a return in order to double-check whether the setting worked flawlessly.
     * @return          The new value of this piece's allegiance. 
     */
    public int setAllegiance (int allegiance) {
        this.allegiance = allegiance;
        return this.allegiance;
    }
}

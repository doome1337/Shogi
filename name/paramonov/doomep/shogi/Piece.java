package name.paramonov.doomep.shogi;

/** A Class representing a piece in a game of shogi.
 * This class defines the basic requirements for a piece: 
 * <ul>
 * <li> Has a given location.
 * <li> Has an allegiance to a side.
 * <li> Is promoted.
 * <li> Is located on the board or on in the drop table.
 * </ul>
 * All pieces extend this class, and implement different rules of checking for valid moves and promotion.
 * @author          Dmitry Andreevich Paramonov
 * @author          Jiayin Huang
 */
public abstract class Piece {
    /** The x-value of this piece on the game board.
     * Will be in a range from 1 to 9.
     */
    protected int x;
    
    /** The y-value of this piece on the game board.
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

    /** Name of this piece.
     * Implemented in each subclass individually.
     * Used to find out the name of the piece and to differentiate them.
     */
    protected String pieceName;

    /** Generates all possible moves for this piece.
     * Used to see which tiles it can go to, 
     * and returns an array of all possible (x, y)-value pairs 
     * where this piece can move.
     * @param   state       The current state of the game at the time of verification.
     * @return              The possible locations where this piece can move.
     */
    public int[][] generateMoves (GameState state) {
        int[][] results = new int[0][2];
        int[][] tempResults;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.checkMove(state, i, j)) {
                    tempResults = new int[results.length+1][2];
                    System.arraycopy(results, 0, tempResults, 0, results.length);
                    tempResults[results.length] = new int[] {i, j};
                    results = tempResults;
                }
            }
        }
        return results;
    }
    
    /** Returns whether a move can be undertaken by this Piece.
     * Each piece has different rules, so this one is to be implemented individually.
     * @param   state       The current state of the game at the time of verification. 
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @return              Whether this Piece can move to the given x and y values.
     */
    public abstract boolean checkMove (GameState state, int x, int y); 
    
    /** Moves this piece to a different location. 
     * Changes the x and y-values of this Piece, and then returns a board style List&lt;List&lt;Piece&gt;&gt;
     * @param   state       The currect state of the game at the time of this method being run.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @return              The state of the board after this piece has been moved.
     */
    public abstract GameState move (GameState state, int x, int y);

    /** Verifies whether this piece can be promoted. 
     * This is placed here so that calling .getPromotable() can be done on any piece, 
     * which allows us to find out exactly which pieces can be promoted.
     * If it was implemented in a different class, that would cause trouble if we were to verify for any piece.
     * @return              Whether or not the piece can be promoted.
     */
    public abstract boolean isPromotable ();

    /** Returns the piece this piece is promoted to.
     * Each piece promotes to a different piece, 
     * and therefore is implemented individually in each subclass.
     * @return              The piece this piece promotes to.
     */
    public abstract Piece promote ();

    /** Returns the piece this piece is demoted from.
     * Each piece demotes to a different piece,
     * and therefore is implemented individually in each subclass.
     * This method is mostly used only in capturing, as you cannot demote normally.
     * @return              The piece this piece demotes to.
     */
    public abstract Piece demote ();

    /** Sets this piece's x and 
     * y-values to certain values.
     * @param   x           The value to which the x-value is to be set.
     * @param   y           The value to which the y-value is to be set.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Returns this piece's x-value.
     * @return              This piece's x location on the board.
     */
    public int getX () {
        return x;
    }

    /** Sets the x-value of this piece to be a certain value.
     * @param   x           The value to which this x-value is to be set.
     */
    public void setX(int x) {
        this.x = x;
    }

    /** Returns this piece's y-value.
     * @return              This piece's y location on the board.
     */
    public int getY () {
        return y;
    }

    /** Sets the y-value of this piece to be a certain value.
     * @param   y           The value to which this y-value is to be set.
     */
    public void setY(int y) {
        this.y = y;
    }

    /** Returns this piece's allegiance.
     * Possible values are:
     * <ul>
     * <li> 1: "White" (Bottom side).
     * <li> 0: "Mercenary" (Neutral).
     * <li> -1: "Black" (Top side).
     * </ul>
     * Mercenary pieces can be captured by either side, 
     * and then dropped. 
     * They do not move, 
     * and cannot attack any pieces.
     * @return              The value of this piece's allegiance.
     */
    public int getAllegiance () {
        return allegiance;
    }

    /** Sets this piece's allegiance.
     * Use for capturing pieces.
     * Possible values listed above.
     * Method has a return in order to double-check whether the setting worked flawlessly.
     * @param   allegiance  The value to which this piece's allegiance is to be set to.
     * @return              The new value of this piece's allegiance. 
     */
    public int setAllegiance (int allegiance) {
        this.allegiance = allegiance;
        return this.allegiance;
    }
}

package mgci.jhdap.shogi;

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
     * Will be in a range from 0 to 8.
     */
    protected int x;
    
    /** The y-value of this piece on the game board.
     * Will be in a range from 0 to 8.
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

    /** This piece's 2 character representation.
     * Used for Text-based IO.
     * Stored as an array with 1 value for each possible allegiance.
     */
    protected String[] doubleCharRepresentation;
    
    /** Whether this piece can be checkmated.
     * Used for verification in check.
     */
    protected boolean checkmatable; 
    
    //TODO: IMPLEMENT EVERYWHERE! ALSO, rename all image files.
    /** The path to the image representing this piece. 
     * Note that it will not reference any folders, 
     * for the possibility of alternate textures. 
     */
    protected String[] imageNames;

    /** Constructs a piece at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this piece is located.
     * @param   y           The y-value at which this piece is located.
     * @param   allegiance  The allegiance of this piece.
     */
    public Piece(int x, int y, int allegiance) {
        this.x = x;
        this.y = y;
        this.allegiance = allegiance;
        this.checkmatable = false;
    }
    
    /** Generates all possible moves for this piece.
     * Used to see which tiles it can go to, 
     * and returns an array of all possible (x, y)-value pairs 
     * where this piece can move.
     * @param   state       The current state of the game at the time of verification.
     * @return              The possible locations where this piece can move.
     */
    public boolean[][] generateMoves (GameState state) {
        boolean[][] results = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                results[i][j] = this.isValidMove(state, i, j);
            }
        }
        return results;
    }
    
    /** Generates all possible drops for this piece.
     * Used to see which tiles it can go to, 
     * and returns an array of all possible (x, y)-value pairs 
     * where this piece can drop.
     * @param   state       The current state of the game at the time of verification.
     * @return              The possible locations where this piece can drop.
     */
    public boolean[][] generateDrops (GameState state) {
        boolean[][] results = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                results[i][j] = this.isValidDrop(state, i, j);
            }
        }
        return results;
    }
    
    /** Returns whether a move can be undertaken,
     * where move is either a move or a drop.
     * @param   state       The current state of the game at the time of verification. 
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @return              Whether this Piece can move to the given x and y values.
     */
    protected boolean isValidMove(GameState state, int x, int y) {
        if (this.x == -1 && this.y == -1) {
            int space = -1;
            for (int i = 0; i < state.getCorrectDropTable(this.getAllegiance()).size(); i++) {
                if (state.getCorrectDropTable(this.getAllegiance()).get(i) == this) {
                    space = i;
                }                
            }
            return this.isValidDrop(state, x, y) && !state.willKingBeInCheckAfterDrop(x, y, this.getAllegiance(), space);
        } else {
            return this.isValidNonDropMove(state, x, y) && !state.willKingBeInCheckAfterMove(x, y, this);
        }
    }
    
    /** Returns whether a move can be undertaken by this Piece.
     * Each piece has different rules, so this one is to be implemented individually.
     * @param   state       The current state of the game at the time of verification. 
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @return              Whether this Piece can move to the given x and y values.
     */
    protected abstract boolean isValidNonDropMove (GameState state, int x, int y); 
    
    /** Returns whether a move can be undertaken by this Piece without verifying for check.
     * Used in order to prevent King check feedback.
     * @param state         The current state of the game at the time of verification. 
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @return              Whether this Piece can move to the given x and y values.
     */
    protected boolean isUncheckedNonDropMove(GameState state, int x, int y) {
        return this.isValidNonDropMove(state, x, y);
    }
    
    /** Returns whether a drop can be undertaken by this Piece.
     * @param   state       The current state of the game at the time of verification. 
     * @param   x           The x-value to which this piece is trying to drop on.
     * @param   y           The y-value to which this piece is trying to drop on.
     * @return              Whether this Piece can be dropped on the given x and y values.
     */
    protected boolean isValidDrop(GameState state, int x, int y) {
        int space = -1;
        for (int i = 0; i < state.getCorrectDropTable(this.getAllegiance()).size(); i++) {
            if (state.getCorrectDropTable(this.getAllegiance()).get(i) == this) {
                space = i;
            }
        }        
        return state.getPieceAt(x, y) instanceof EmptyPiece && !state.willKingBeInCheckAfterDrop(x, y, this.getAllegiance(), space);
    }
    
    /** Moves the piece, and captures any pieces at the target tile.
     * Verifies if target tile is a valid move, 
     * and then moves the piece to that location.
     * If the target tile is occupied by an enemy tile,
     * it is captured, demoted, 
     * and placed in the drop table.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              The state of the game after the piece is moved.
     */
    protected GameState move (GameState state, int x, int y) {
        if (this.isValidMove(state, x, y)) {
            if (!(state.getPieceAt(x, y) instanceof EmptyPiece)) {
                state.addPieceToDropTable(this.allegiance, state.getPieceAt(x, y));
            } 
            state.setPieceAt(x, y, this);
            state.setPieceAt(this.x, this.y, new EmptyPiece(this.x, this.y));
            this.y = y;
            this.x = x;
        }
        return state;
    }

    /** Verifies whether this piece can be promoted. 
     * This is placed here so that calling .getPromotable() can be done on any piece, 
     * which allows us to find out exactly which pieces can be promoted.
     * If it was implemented in a different class, that would cause trouble if we were to verify for any piece.
     * @return              Whether or not the piece can be promoted.
     */
    protected abstract boolean isPromotable ();

    /** Returns whether this piece must promote 
     * after it moved into the given target tile.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this piece must promote after it gets to the target tile.
     */
    protected boolean mustPromoteIfMoved(GameState state, int x, int y) {
        return false;
    }
    
    /** Returns the piece this piece is promoted to.
     * Each piece promotes to a different piece, 
     * and therefore is implemented individually in each subclass.
     * @return              The piece this piece promotes to.
     */
    protected abstract Piece promote ();

    /** Returns the piece this piece is demoted from.
     * Each piece demotes to a different piece,
     * and therefore is implemented individually in each subclass.
     * This method is mostly used only in capturing, as you cannot demote normally.
     * @return              The piece this piece demotes to.
     */
    protected abstract Piece demote ();

    /** Sets this piece's x and 
     * y-values to certain values.
     * @param   x           The value to which the x-value is to be set.
     * @param   y           The value to which the y-value is to be set.
     */
    protected void setPosition(int x, int y) {
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
    protected void setX(int x) {
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
    protected void setY(int y) {
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
    protected int setAllegiance (int allegiance) {
        this.allegiance = allegiance;
        return this.allegiance;
    }

    /** Returns this piece's 2 character code. 
     * Used for ASCII IO.
     * @return              This piece's 2 character representation;
     */
    public String getDoubleCharRepresentation() {
        return this.doubleCharRepresentation[this.allegiance+1];
    }

    public boolean getCheckmatable() {
        return this.checkmatable;
    }
}

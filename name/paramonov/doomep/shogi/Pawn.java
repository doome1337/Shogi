package name.paramonov.doomep.shogi;

/** A Class representing a pawn in a game of shogi.
 */
public class Pawn extends Piece {
    /** Constructs a pawn at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this pawn is located.
     * @param   y           The y-value at which this pawn is located.
     * @param   allegiance  The allegiance of this pawn.
     */
    public Pawn(int x, int y, int allegiance) {
        this.x = x;
        this.y = y;
        this.allegiance = allegiance;
    }

    /** Returns whether a move can be untertaken by this Pawn.
     * Pieces with an allegiance of 1 move up the board, and up the ranks. 
     * Pieces with an allegiance of -1 move down the board, and down the ranks.
     * This method hecks to see if the target tile is on the next rank,
     * then checks to see if the target tile is on the same file as this Pawn.
     * Afterwards, this method checks to see if the target rank is 
     * within the range of permitted ranks,
     * followed by verifying if the target tile doesn't contain friendly units.
     * @return              Whether this Pawn can move to the given x and y values.
     */
    public boolean checkMove (GameState state, int x, int y) {
         return ((y == this.y + this.allegiance)
         && (x == this.x)
         && !((y < 0) || (y > 8))
         && (state.getBoard()
                  .get(y)
                  .get(x)
                  .getAllegiance() != this.allegiance)); 
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
    public GameState move (GameState state, int x, int y) {
        if (this.checkMove(state, x, y)) {
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

    /** Returns whether or not this pawn can promote.
     * Uses the formula:
     * (5+a)a &lt; ay,
     * where a is the allegiance number, 
     * and y is the y-value of this pawn.
     * If allegiance is +!, 
     * then pawns promote in ranks 7, 8 and 9.
     * If allegiance is -1, 
     * then pawns promote in ranks 1, 2 and 3.
     * If allegiance is +1, 
     * the formula is 6 &lt; y, 
     * which is true for ranks 7, 8 and 9.
     * If allegiance is -1,
     * the formula is -4 &lt; -y,
     * which is true for ranks 1, 2 and 3.
     * Therefore the function works 
     * for all necessary occasions.
     * @return              Whether the pawn can promote 
     * in its current location.
     */
    public boolean isPromotable () {
        return ((4 + this.allegiance) * this.allegiance 
                < this.y * this.allegiance);
    }

    /** Returns the Piece this pawn promotes to.
     * Creates a PromotedPawn with the same allegiance 
     * and location as this pawn.
     * @return              The PromotedPawn equivalent of this Pawn.
     */
    public Piece promote () {
        return new PromotedPawn (this.x, this.y, this.allegiance);
    }

    /** Returns the piece this pawn demotes to.
     * As pawns are not a promoted piece, 
     * demoting them has no effect.
     * @return              This pawn.
     */
    public Piece demote () {
        return this;
    }
    public asdf defee(){

    }
}

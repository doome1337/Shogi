package name.paramonov.doomep.shogi;

import java.util.List;

/** A Class representing a pawn in a game of shogi.
 */
public class Pawn extends Piece {
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
     * @return          Whether this Pawn can move to the given x and y values.
     */
    public boolean checkMove (List<List<Piece>> board, int x, int y) {
         return ((y == this.y + this.allegiance)
         && (x == this.x)
         && !((y < Piece.STARTING_BOARD_Y) || (y > Piece.STARTING_BOARD_Y+8))
         && (board.get(y)
                  .get(x)
                  .getAllegiance() != this.allegiance)); 
    }

    /** Moves the piece, and captures any pieces at the target tile.
     * Verifies if target tile is a valid move, 
     * and then moves the piece to that location.
     * If the target tile is occupied by an enemy tile,
     * it is captured, demoted, 
     * and placed in the drop table.
     * @param   x       The x-value to which this piece is trying to move.
     * @param   y       The y-value to which this piece is trying to move.
     * @param   board   The state of the board before the piece is moved.
     * @return          The state of the board after the piece is moved.
     */
    public List<List<Piece>> move (List<List<Piece>> board, int x, int y) {
        if (this.checkMove(board, x, y)) {
            if (!(board.get(y).get(x) instanceof EmptyPiece)) {
                board.get(((this.allegiance)?(Piece.STARTING_BOARD_Y*9):10)).append(board.get(y).get(x).setAllegiance(this.allegiance).demote());
            } 
            board.get(y).set(x, this);
            board.get(this.y).set(this.x, new EmptyPiece(this.y, this.x));
            this.y = y;
            this.x = x;
        }
        return board;
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
     * @return          Whether the pawn can promote 
     * in its current location.
     */
    public boolean getPromotable () {
        return ((4 + Piece.STARTING_BOARD_Y + this.allegiance) * this.allegiance 
                < this.y * this.allegiance);
    }

    public Piece promote () {
        return new PromotedPawn ();
    }
}

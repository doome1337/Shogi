package mgci.jhdap.shogi;

/** A Class representing a pawn in a game of shogi.
 */
public class Pawn extends PromotablePiece {
    /** Constructs a pawn at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this pawn is located.
     * @param   y           The y-value at which this pawn is located.
     * @param   allegiance  The allegiance of this pawn.
     */
    public Pawn(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Pawn";
        this.doubleCharRepresentation = new String[]{"p ","P#", "P "};
        this.imageNames = new String[]{"dPawn.png", "nPawn.png", "uPawn.png"};
    }

    /** Returns whether a move can be undertaken by this Pawn.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this Pawn can move to the given x and y values.
     */
    protected boolean isValidNonDropMove (GameState state, int x, int y) {
        /*
         * Pieces with an allegiance of 1 move up the board, and up the ranks. 
         * Pieces with an allegiance of -1 move down the board, and down the ranks.
         * This method checks to see if the target tile is on the next rank,
         * then checks to see if the target tile is on the same file as this Pawn.
         * Afterwards, this method checks to see if the target rank is 
         * within the range of permitted ranks,
         * followed by verifying if the target tile doesn't contain friendly units.
         */
         return ((y == this.y + this.allegiance)
         && (x == this.x)
         && !((y < 0) || (y > 8))
         && (state.getPieceAt(x, y)
                  .getAllegiance() != this.allegiance));
    }
    
    /** Returns whether a drop can be undertaken by this Pawn.
     * @param   state       The current state of the game at the time of verification. 
     * @param   x           The x-value to which this piece is trying to drop on.
     * @param   y           The y-value to which this piece is trying to drop on.
     * @return              Whether this Pawn can be dropped on the given x and y values.
     */
    protected boolean isValidDrop(GameState state, int x, int y) {
        boolean nifu = true;
        for (int i = 0; nifu && i < 9; i++) {
            nifu = !(state.getPieceAt(x, i) instanceof Pawn
                  && state.getPieceAt(x, i).getAllegiance() == this.getAllegiance()); 
        }
        Piece tempRep = state.getPieceAt(x, y);
        state.dropPieceFromTable(this.allegiance, x, y, this);
        boolean uchifuzume = !state.isKingCheckmated(-this.allegiance);
        state.addPieceToDropTable(this.allegiance, this);
        state.setPieceAt(x, y, tempRep);
        return nifu
            && uchifuzume
            && state.getPieceAt(x, y) instanceof EmptyPiece
            && y != 4+4*this.allegiance;
    }

    @Override
    protected boolean mustPromoteIfMoved(GameState state, int x, int y) {
        return y == 4+4*this.allegiance;
    }
    
    /** Returns the Piece this pawn promotes to.
     * Creates a PromotedPawn with the same allegiance 
     * and location as this pawn.
     * @return              The PromotedPawn equivalent of this Pawn.
     */
    protected Piece promote () {
        if (this.isPromotable()) {
            return new PromotedPawn (this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    @Override
    /** Returns the piece this pawn demotes to.
     * As pawns are not a promoted piece, 
     * demoting them has no effect.
     * @return              This pawn.
     */
    protected Piece demote () {
        return this;
    }
}

package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class GoldGeneral extends UnpromotablePiece {
    public GoldGeneral(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Gold General";
    }
    /** Returns whether a move can be undertaken by this PromotePawn.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this PromotedPawn can move to the given x and y values.
     */
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        /* Checks if the target tile is in front 
         * and in one of three horizontal directions, 
         * at the side, or directly behind.
         * Then verifies if the target tile is still in the board, 
         * and whether the target tile could be moved into.
         * */
        return ((((y == this.y+this.allegiance) 
                   && (x == this.x-1 || x == this.x || x == this.x+1)) 
                  || ((y == this.y) 
                   && (x == this.x-1 || x == this.x+1))
                  || ((y == this.y-this.allegiance) 
                   && (x == this.x))) 
                  && !(y < 0 || y > 8)
                  && !(x < 0 || x > 8)
                   && (state.getPieceAt(x, y).getAllegiance() != this.allegiance));
    }

    /** Returns the piece this gold general demotes to.
     * As gold generals are not a promoted piece, 
     * demoting them has no effect.
     * @return              This golden general.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

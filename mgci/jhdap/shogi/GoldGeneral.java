package mgci.jhdap.shogi;

/** A Class representing a gold general in a game of shogi.
 */
public class GoldGeneral extends UnpromotablePiece {
    /** Constructs a gold general at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this gold general is located.
     * @param   y           The y-value at which this gold general is located.
     * @param   allegiance  The allegiance of this gold general.
     */
    public GoldGeneral(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Gold General";
        this.doubleCharRepresentation = new String[]{"g ","G#", "G "};
        this.imageNames = new String[]{"dGold.png", "nGold.png", "uGold.png"};
    }
    
    /** Returns whether a move can be undertaken by this GoldGeneral.
     * @param   x           The x-value to which this piece is trying to move.
     * @param   y           The y-value to which this piece is trying to move.
     * @param   state       The state of the game before the piece is moved.
     * @return              Whether this GoldGeneral can move to the given x and y values.
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

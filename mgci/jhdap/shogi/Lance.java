package mgci.jhdap.shogi;

/** A Class representing a lance in a game of shogi.
 */
public class Lance extends PromotablePiece {
    /** Constructs a lance at a given x and y-value,
     * with the given allegiance.
     * @param   x           The x-value at which this lance is located.
     * @param   y           The y-value at which this lance is located.
     * @param   allegiance  The allegiance of this lance.
     */
    public Lance(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Lance";
        this.doubleCharRepresentation = new String[]{"l ","L#", "L "};
        this.imageNames = new String[]{"dLance.png", "nLance.png", "uLance.png"};
    }
        
    /** Checks whether this Lance can move to a given location.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this Lance can move to the target tile.
     */
    @Override
    protected boolean isValidNonDropMove(GameState state, int x, int y) {
        /* First verifies if the target value is on the board, 
         * and is on the same x-column.
         * Then, loops through the tiles, moving 1 forwards,
         * where forwards is determined by allegiance.
         * If the move at any point becomes illegal, 
         * the entire move is illegal.
         * */
        boolean validMove = (x == this.x 
                       && !((y < 0) || (y > 8)))
                         && (this.allegiance*this.y < this.allegiance*y);
        for (int i = this.y+this.allegiance; validMove && i*this.allegiance < y*this.allegiance; i+= this.allegiance) {
            validMove = state.getPieceAt(x, i) instanceof EmptyPiece;
        }
        if (validMove) {
            validMove = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        }
        return validMove;
    }
    
    /** Returns whether a drop can be undertaken by this Lance.
     * @param   state       The current state of the game at the time of verification. 
     * @param   x           The x-value to which this piece is trying to drop on.
     * @param   y           The y-value to which this piece is trying to drop on.
     * @return              Whether this Lance can be dropped on the given x and y values.
     */
    protected boolean isValidDrop(GameState state, int x, int y) {
        int space = -1;
        for (int i = 0; i < state.getCorrectDropTable(this.getAllegiance()).size(); i++) {
            if (state.getCorrectDropTable(this.getAllegiance()).get(i) == this) {
                space = i;
            }
        }
        return state.getPieceAt(x, y) instanceof EmptyPiece
            && y != 4+4*this.allegiance
           && !state.willKingBeInCheckAfterDrop(x, y, this.getAllegiance(), space);
    }
    
    @Override
    protected boolean mustPromoteIfMoved(GameState state, int x, int y) {
        return y == 4+4*this.allegiance;
    }
    
    /** Returns the Piece this lance promotes to.
     * Creates a PromotedLance with the same allegiance 
     * and location as this lance.
     * @return              The PromotedLance equivalent of this Lance.
     */
    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedLance(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    /** Returns the piece this Lance demotes to.
     * As lances are not a promoted piece, 
     * demoting them has no effect.
     * @return              This Lance.
     */
    @Override
    protected Piece demote() {
        return this;
    }
}

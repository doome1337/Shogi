package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class Lance extends PromotablePiece {
    public Lance(int x, int y, int allegiance) {
        this.x = x;
        this.y = y;
        this.allegiance = allegiance;
        this.pieceName = "Lance";
    }
    
    @Override
    /** Checks whether this Lance can move to a given location.
     * @param state         The state of the game at this time.
     * @param x             The x-value of the target tile.
     * @param y             The y-value of the target tile.
     * @return              Whether or not this Lance can move to the target tile.
     */ 
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: THIS.
        boolean validMove = true;
        for (int i = this.y+this.allegiance; validMove && i*this.allegiance <= y*this.allegiance; i+= this.allegiance) {
            
        }
        return false;
    }
    
    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedLance(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    @Override
    protected Piece demote() {
        return this;
    }

}

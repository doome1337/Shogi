package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class King extends UnpromotablePiece {
    public King(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "King";
    }
    
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        boolean validMove = Math.abs(this.x-x) < 2 && Math.abs(this.y-y) < 2 
                         && state.getPieceAt(x, y).getAllegiance() != this.allegiance 
                        && !state.isAttacked(x, y, -this.allegiance);
        return validMove;
    }
    
    @Override
    protected boolean isUncheckedMove(GameState state, int x, int y) {
        boolean validMove = Math.abs(this.x-x) < 2 && Math.abs(this.y-y) < 2 
                         && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        return validMove;    
    }

    @Override
    protected Piece demote() {
        return this;
    }
}

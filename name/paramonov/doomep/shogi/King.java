package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class King extends UnpromotablePiece {
    
    
    public King(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "King";
        this.doubleCharRepresentation = new String[]{"k ","K#", "K "};
        this.checkmatable = true;
    }
    
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        boolean validMove = Math.abs(this.x-x) < 2 && Math.abs(this.y-y) < 2 
                        && !(x < 0 || x > 8)
                        && !(y < 0 || y > 8)
                         && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        state.setPieceAt(this.x, this.y, new EmptyPiece(this.x, this.y));
        validMove = validMove && !state.isAttacked(x, y, -this.allegiance);
        state.setPieceAt(this.x, this.y, new King(this.x, this.y, this.allegiance));
        return validMove;
    }
    
    @Override
    protected boolean isUncheckedMove(GameState state, int x, int y) {
        boolean validMove = Math.abs(this.x-x) < 2 && Math.abs(this.y-y) < 2 
                        && !(x < 0 || x > 8)
                        && !(y < 0 || y > 8)
                         && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        return validMove;    
    }

    @Override
    protected Piece demote() {
        return this;
    }
}

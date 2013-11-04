package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class PromotedRook extends UnpromotablePiece {
    public PromotedRook(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Rook";
    }
    
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: Comments. Doublecheck this thing. Is messy.
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove1 = true;
        boolean validMove2 = true;
        if (dy == 0 && dx != 0){
            for (int i = 1; validMove1 && i*Math.signum(dx) < dx; i++) {
                validMove1 = state.getPieceAt(this.x+i*((int)Math.signum(dx)), y) instanceof EmptyPiece;
            }
            if (validMove1) {
                validMove1 = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else if (dx == 0 && dy != 0) {
            for (int i = 1; validMove1 && i*Math.signum(dy) < dy; i++) {
                validMove1 = state.getPieceAt(x, this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
            }
            if (validMove1) {
                validMove1 = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else {
            validMove1 = false;
        }
        validMove2 = Math.abs(dy) == 1 && Math.abs(dx) == 1 && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        return validMove1 || validMove2;
    }

    @Override
    protected Piece demote() {
        return new Rook(this.x, this.y, this.allegiance);
    }
}

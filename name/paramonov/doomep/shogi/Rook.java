package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class Rook extends PromotablePiece {
    public Rook(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Rook";
    }

    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: Comments.
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove = true;
        if (dy == 0 && dx != 0){
            for (int i = 1; validMove && i*Math.signum(dx) < dx; i++) {
                validMove = state.getPieceAt(this.x+i*((int)Math.signum(dx)), y) instanceof EmptyPiece;
            }
            if (validMove) {
                validMove = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else if (dx == 0 && dy != 0) {
            for (int i = 1; validMove && i*Math.signum(dy) < dy; i++) {
                validMove = state.getPieceAt(x, this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
            }
            if (validMove) {
                validMove = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else {
            validMove = false;
        }
        return validMove;
    }

    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedRook(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    @Override
    protected Piece demote() {
        return this;
    }
}

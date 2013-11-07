package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class Bishop extends PromotablePiece {
    public Bishop(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Bishop";
        this.doubleCharRepresentation = new String[]{"b ","B#", "B "};
    }

    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: Comments.
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove = !(x < 0 || x > 8) && !(y < 0 || y > 8);
        if (validMove && Math.abs(dx) == Math.abs(dy) && dy != 0) {
            for (int i = 1; validMove && i < Math.abs(dy); i++) {
                validMove = state.getPieceAt(this.x+i*((int)Math.signum(dx)), this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
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
            return new PromotedBishop(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    @Override
    protected Piece demote() {
        return this;
    }
}

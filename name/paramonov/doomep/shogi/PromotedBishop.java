package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class PromotedBishop extends UnpromotablePiece {
    public PromotedBishop(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Bishop";
        this.doubleCharRepresentation = new String[]{"b+","B*", "B+"};
    }

    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        //TODO: Comments. Doublecheck this thing. Is messy.
        int dx = x - this.x; 
        int dy = y - this.y;
        boolean validMove1 = true;
        boolean validMove2 = true;
        if (dx*Math.signum(dx) == dy*Math.signum(dy) && dy != 0) {
            for (int i = 1; validMove1 && i < Math.abs(dy); i++) {
                validMove1 = state.getPieceAt(this.x+i*((int)Math.signum(dx)), this.y+i*((int)Math.signum(dy))) instanceof EmptyPiece;
            }
            if (validMove1) {
                validMove1 = state.getPieceAt(x, y).getAllegiance() != this.allegiance;
            }
        } else {
            validMove1 = false;
        }
        validMove2 = ((Math.abs(dx) == 1 && dy == 0) || (Math.abs(dy) == 1 && dx == 0)) && state.getPieceAt(x, y).getAllegiance() != this.allegiance;
        return validMove1 || validMove2;
    }

    @Override
    protected Piece demote() {
        return new Bishop(this.x, this.y, this.allegiance);
    }

}

package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class PromotedPawn extends UnpromotablePiece {
    public PromotedPawn (int x, int y, int allegiance) {
        this.x = x;
        this.y = y;
        this.allegiance = allegiance;
        this.pieceName = "Promoted Pawn";
	}

	@Override
    protected boolean isValidMove(GameState state, int x, int y) {
	    // TODO Auto-generated method stub
	    return false;
	}

	@Override
    protected Piece demote() {
		return new Pawn(this.x, this.y, this.allegiance);
	}
}

package name.paramonov.doomep.shogi;

public class PromotedPawn extends Piece {
	public PromotedPawn (int x, int y, int allegiance) {
		this.x = x;
		this.y = y;
		this.allegiance = allegiance;
	}
	
	@Override
	public boolean checkMove(GameState state, int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public GameState move(GameState state, int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPromotable() {
		return false;
	}

	@Override
	public Piece promote() {
		return this;
	}

	@Override
	public Piece demote() {
		return new Pawn(this.x, this.y, this.allegiance);
	}
}

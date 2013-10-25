package name.paramonov.doomep.shogi;
//TODO: Documentation.
public abstract class UnpromotablePiece extends Piece {
    protected abstract boolean isValidMove(GameState state, int x, int y);

    @Override
    protected boolean isPromotable() {
        return false;
    }

    @Override
    protected Piece promote() {
        return this;
    }

    protected abstract Piece demote();
}

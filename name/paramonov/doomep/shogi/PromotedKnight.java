package name.paramonov.doomep.shogi;
//TODO: Copy all code from a finished PromotedX. 
public class PromotedKnight extends UnpromotablePiece {
    public PromotedKnight(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Promoted Knight";
    }

    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Piece demote() {
        // TODO Auto-generated method stub
        return null;
    }

}

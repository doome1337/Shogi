package name.paramonov.doomep.shogi;
//TODO: Documentation.
public class PromotedLance extends UnpromotablePiece {
    public PromotedLance(int x, int y, int allegiance) {
        this.x = x;
        this.y = y;
        this.allegiance = allegiance;
        this.pieceName = "Promoted Lance";
    }
    
    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected Piece demote() {
        return new Lance(this.x, this.y, this.allegiance);
    }
}

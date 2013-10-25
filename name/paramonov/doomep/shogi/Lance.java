package name.paramonov.doomep.shogi;

public class Lance extends Piece {
    public Lance(int x, int y, int allegiance) {
        this.x = x;
        this.y = y;
        this.allegiance = allegiance;
        this.pieceName = "Lance";
    }
    
    @Override
    public boolean checkMove(GameState state, int x, int y) {
        boolean validMove;
        for (int i = this.y; i <= y; i++) {
            
        }
        return false;
    }

    @Override
    public GameState move(GameState state, int x, int y) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPromotable() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Piece promote() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Piece demote() {
        // TODO Auto-generated method stub
        return null;
    }

}

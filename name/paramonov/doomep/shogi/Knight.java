package name.paramonov.doomep.shogi;
//TODO: Documentation.
/** A Class representing a knight in a game of shogi.
 */
public class Knight extends PromotablePiece {
    public Knight(int x, int y, int allegiance) {
        super(x, y, allegiance);
        this.pieceName = "Knight";
    }

    @Override
    protected boolean isValidMove(GameState state, int x, int y) {
        return (((y == this.y+2*this.allegiance) 
                && (x == this.x-1 || x == this.x+1))  
               && !(y < 0 || y > 8)
               && !(x < 0 || x > 8)
                && (state.getPieceAt(x, y).getAllegiance() != this.allegiance));
    }

    @Override
    protected Piece promote() {
        if (this.isPromotable()) {
            return new PromotedKnight(this.x, this.y, this.allegiance);
        } else {
            return this;
        }
    }

    @Override
    protected Piece demote() {
        return this;
    }

}

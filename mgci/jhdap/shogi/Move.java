package mgci.jhdap.shogi;

// TODO: The rest of this class.
// TODO: Annotate.

/** A that represents a piece move.
 * Meant to be later used with the legitimate shogi notation 
 * (e.g. P-8f or 7&#20845;&#27497)
 * 
 * @author Jiayin
 */
public class Move 
{
	public String pieceName;
	public Tile from;
	public Tile to;	
	public char modifier;
	public char promoted = ' ';
			
	public Move (GameState state, Piece piece, Tile to)
	{
		this (state, piece, new Tile (piece.x, piece.y), to);			
	}
	
	public Move (GameState state, Piece piece, Tile from, Tile to)
	{
		pieceName = piece.getDoubleCharRepresentation();
		pieceName = new StringBuilder (pieceName).reverse().toString();
		
		this.from = from;
		this.to = to;

		if (from.x != -1 && from.y != -1)				
			modifier = state.getPieceAt (to.x, to.y) instanceof EmptyPiece ? '-' : 'x';		
		else
			modifier = '*';			
	}
	
	public void promote ()
	{
		promoted = '+';
	}
	
	
	public String toString ()
	{		
		String full = pieceName;
		if (modifier != '*')
			full += from.getCode (Tile.SHOGI_NOTATION);
		full += modifier;
		full += to.getCode (Tile.SHOGI_NOTATION);
		full += promoted;
		return full;
	}
}

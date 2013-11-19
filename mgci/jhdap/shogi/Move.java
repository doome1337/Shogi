package mgci.jhdap.shogi;

/** A Class that represents a piece move.
 * Meant to be later used with the legitimate shogi notation 
 * (e.g. P-8f or 7&#20845;&#27497), but right now, no Japanese.
 * <br><br>
 * The current format is: [piece symbol][initial position]
 * [modifier][final position][promotion modifier]. For example,
 * moving a Pawn from 9g to 9f would be " P9g-9f", and having a
 * bishop move from 5e to capture a piece at 3g and then promote
 * would be " B5ex3g+".
 * 
 * @author       Jiayin Huang
 * @author       Dmitry Andreevich Paramonov
 */
public class Move 
{
	/** The name of the piece that was moved. 
	 */
	public String pieceName;
	/** Where the piece was moved from. 
	 */
	public Tile from;
	/** Where the piece was moved to. 
	 */
	public Tile to;	
	/** The modifier for this move. - indicates a normal move.
	 * * indicates a drop. x indicates a capture. 
	 */
	public char modifier;
	/** The modifier for promotion. An empty space indicates
	 * no promotion. A + indicates promotion. 
	 */
	public char promoted = ' ';
			
	/** Constructs a new Move.
	 * 
	 * @param state		the state of the board
	 * @param piece		the piece about to be moved
	 * @param to		the destination tile
	 */
	public Move (GameState state, Piece piece, Tile to)
	{
		this (state, piece, new Tile (piece.x, piece.y), to);			
	}
	
	/** Constructs a new move.
	 * 
	 * @param state		the state of the board
	 * @param piece		the piece that was moved
	 * @param from		where the piece was moved from
	 * @param to		where the piece was moved to
	 */
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
	
	/** Sets the promote modifier to +. 
	 */
	public void promote ()
	{
		promoted = '+';
	}
	
	/** Gets the full move description.
	 * 
	 * @return the String representation of this move
	 */
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

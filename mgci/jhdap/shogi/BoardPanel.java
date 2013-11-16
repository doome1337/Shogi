package mgci.jhdap.shogi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.io.File;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** The JPanel for the shogi board. Work in progress.
 * @author                  Jiayin Huang
 * @author                  Dmitry Andreevich Paramonov 
 */
public class BoardPanel extends JPanel 
{	
	/** Eclipse wants this variable for some reason.
	 */
	private static final long serialVersionUID = 1L;

	private Color backgroundColor = new Color (206, 192, 122);	
	public Color dropTableColor = new Color (211, 211, 223);	
	public Color boardColor = new Color (223, 180, 114);
	public Color highlightColor = Color.green;

	/** The x,y coordinates of the top left corner of the shogi board. 
	 */
	private Point boardOffset;	

	/** The x,y coordinates of the top left corner of the bottom drop table.
	 */
	private Point dropOffset1;

	/** The x,y coordinates of the top left corner of the top drop table.
	 */
	private Point dropOffset2;

	/** The dimensions of a shogi board square. 
	 */
	private Dimension tile;

	/** The dimensions of a drop table.
	 */
	private Dimension dropTable;	

	/** The board. 
	 */
	protected GameState state = new GameState ();

	/** The relationship between shogi pieces and their positioning
	 * on the drop table, with (0,0) being at the top left of the drop table. 
	 */
	protected static final Map<String,Point> pieceToDropTable;
	static
	{
		Map<String,Point> temp = new HashMap<String,Point>();
		temp.put("Pawn", new Point (0,0));		
		temp.put("Lance", new Point (0,1));
		temp.put("Knight", new Point (1,1));
		temp.put("Silver General", new Point (0,2));
		temp.put("Gold General", new Point (1,2));
		temp.put("Bishop", new Point (0,3));
		temp.put("Rook", new Point (1,3));
		pieceToDropTable = Collections.unmodifiableMap(temp);
	}	

	/** The relationship between shogi pieces and their positioning
	 * on the drop table, with (0,0) being at the top left of the drop table. 
	 */
	protected static final Map<Point,String> dropTableToPiece;
	static
	{
		Map<Point,String> temp1 = new HashMap<Point,String>();

		for (Entry<String, Point> entry : pieceToDropTable.entrySet()) 		
			temp1.put(entry.getValue(), entry.getKey());

		dropTableToPiece = Collections.unmodifiableMap(temp1);		
	}

	/** Whether a shogi piece is currently selected or not. 
	 */
	protected boolean pieceIsSelected = false;

	/** The selected shogi piece. 
	 */
	protected Piece selectedPiece;

	/** The location of the cursor when the corresponding mouse button was pressed. 
	 */
	protected Point [] click = new Point [4];

	/** The current location of the cursor.
	 */
	protected Point mouse = new Point ();

	/** Whether the mouse button is currently being pressed or not.
	 */	
	protected boolean [] mousePressed = new boolean [4];

	/** Whether hardcore mode is active or not. If hardcore mode is active,
	 * all of the possible moves of a piece are not displayed. 
	 */
	public boolean proMode = false;

	/** Whether to draw the board coordinate labels (e.g. 1a, 2a, 2b) or not. 
	 */
	public boolean showBoardLabels = true;

	public boolean log = false;

	/** The sound that plays when a piece is moved or promoted.
	 */
	private SoundEffect snap = new SoundEffect (new File ("../Shogi/resources/snap.wav"));

	protected ShogiConsole c = null;

	/** Creates a new BoardPanel. 
	 */
	public BoardPanel ()
	{			
		setBackground (backgroundColor);
		reset ();		

		boardOffset = new Point (30, 30);
		tile = new Dimension (40, 40);

		setBoardOffset (boardOffset);	
		setTileSize (tile);				
	}

	/** Draws everything.
	 * 
	 * @param g		the Graphics context in which to paint
	 */
	@Override	
	public void paintComponent (Graphics g)
	{       
		super.paintComponent(g);

		drawBoard (g);
		drawBoardLabels (g);
		drawDropTables (g);	
		drawHighlights (g);
		drawDropTablePieces (g);
	}

	/** Draws the shogi board and all of the pieces on it. Does not
	 * draw a piece if it is currently being selected because in this
	 * case, it would not be technically "on" the board.
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	protected void drawBoard (Graphics g)
	{
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{				
				Piece piece = state.getPieceAt(i, j);
				Point point = getBoardLocationOnPanel (i, j);		
				drawTile (g, boardColor, Color.black, point);					

				// Draw the piece if it is not selected

				if (!(pieceIsSelected && piece.equals(selectedPiece)))				
					drawPiece (g, piece, point);				
			}        
		}  	
	}

	/** Draws the board coordinate labels (e.g. 1a, 2a, 2b).
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	protected void drawBoardLabels (Graphics g)
	{
		if (showBoardLabels)
		{
			Point point;
			String file, rank;
			g.setColor(new Color (128, 128, 128));

			for (int i = 0; i < 9; i++)
			{
				// Rank (aka Row)
				rank = "" + (char)('i' - i);
				point = getBoardLocationOnPanel (0, i);
				point.translate(-12, 25);				
				g.drawString (rank, point.x, point.y);

				// File (aka Column)
				file = "" + (9 - i);

				point = getBoardLocationOnPanel (i, 8);
				point.translate (18, -5);				
				g.drawString (file, point.x, point.y);	

				point = getBoardLocationOnPanel (i, 0);
				point.translate (18, tile.height + 15);
				g.drawString (file, point.x, point.y);	
			}
		}
	}

	/** Redraws a shogi board square with a highlighted background color
	 * based on the state and positioning of the mouse. Highlights a square
	 * if (1) the mouse is hovering over the square, (2) the square is not empty, and
	 * (3) and the mouse is not being pressed. Also highlights all possible moves for
	 * a selected piece if (1) the mouse is being pressed, and (2) a piece is selected.
	 *  
	 * @param g 	the Graphics context in which to paint
	 */
	protected void drawHighlights (Graphics g)
	{			
		if (pieceIsSelected)
		{		
			drawPossibleMoves (g, selectedPiece);
			Point point = new Point (mouse);
			point.translate(-tile.width / 2, -tile.height / 2);
			drawPiece (g, selectedPiece, point);
		}
		else
		{
			if (mouseIsOnBoard ())			
				drawHighlightsOnBoard (g);

			else if (mouseIsOnDropTable ())
				drawHighlightsOnDropTable (g);			
		}			
	}

	private void drawHighlightsOnBoard (Graphics g)
	{
		Tile boardCoords = getLocationOnBoard (mouse);	
		Point point = getBoardLocationOnPanel (boardCoords);
		if (!(state.getPieceAt(boardCoords.x, boardCoords.y) instanceof EmptyPiece))
		{	
			Piece piece = state.getPieceAt(boardCoords.x, boardCoords.y);
			drawHighlightedTile (g, point);

			drawPiece (g, piece, point);
		}	
	}

	private void drawHighlightsOnDropTable (Graphics g)
	{
		int table = mouseIsOnDropTable (1) ? 1 : -1;
		Point tableCoords = getLocationOnDropTable (table, mouse);				
		Piece p = getDropTablePieceAt (table, tableCoords);	
		if (p != null)
		{
			Point point = getDropTableLocationOnPanel (table, tableCoords);
			drawTile (g, highlightColor, Color.black, point);

			// Pawns on the drop table take up twice as much space
			int width = p instanceof Pawn ? 3 * tile.width : (int) (1.5 * tile.width);
			drawTile (g, highlightColor, Color.black, point, width, tile.height);
		}
	}

	/** Draws a highlighted shogi board square at the indicated location
	 * 
	 * @param g			the Graphics context in which to paint
	 * @param p			the x,y coordinates at which to draw
	 */
	private void drawHighlightedTile (Graphics g, Point p)
	{
		drawTile (g, highlightColor, Color.black, p);
	}

	/** Returns the last occurring piece that matches the type 
	 * of piece that is located at the indicated coordinates
	 * of the indicated drop table.
	 * 
	 * @param table		the allegiance of the drop table
	 * @param sq		the location on the drop table
	 * @return			the matching piece in the drop table ; null if no match
	 */
	private Piece getDropTablePieceAt (int table, Point sq)
	{	
		if (sq.equals(new Point (1,0))) // pawns take up 2x the space
			sq.setLocation(0,0);
		String name = dropTableToPiece.get(sq); // access (dropTable location -> piece) hashmap

		return getDropTablePieceAt (table, name);		
	}

	/** Finds and returns the last piece on the indicated 
	 * drop table that matches the given name. Returns null
	 * if no match is found. 
	 * 
	 * @param table		the allegiance of the drop table
	 * @param name		the name of the piece
	 * @return			the matching piece in the drop table ; null if no match
	 */
	public Piece getDropTablePieceAt (int table, String name)
	{
		List<Piece> list = state.getCorrectDropTable (table);

		Piece piece = null;
		for (int i = list.size() - 1; i >= 0 && piece == null; i--)
		{		
			if (name.equalsIgnoreCase (list.get (i).pieceName) 
					|| name.equalsIgnoreCase(list.get(i).doubleCharRepresentation[0].substring (0,1)))
				piece = list.get(i);
		}
		return piece;		
	}	

	/** Redraws all of the shogi board squares to which it is
	 * legally valid for the given piece to move. Redraws 
	 * these squares with a highlighted background color. Redraws
	 * the shogi pieces on these squares.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param piece 	the shogi piece
	 */
	public void drawPossibleMoves (Graphics g, Piece piece)
	{
		boolean [][] moves = piece.generateMoves(state);

		if (!proMode)
		{
			for (int i = 0; i < 9; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					if (moves [j][i])
					{
						Point point = getBoardLocationOnPanel (new Tile (j, i));
						drawHighlightedTile (g, point);						
						drawPiece (g, state.getPieceAt(j, i), point);
					}
				}
			}
		}
	}

	/** Draws the two drop tables.
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	protected void drawDropTables (Graphics g)
	{	
		// Fill drop tables
		g.setColor(dropTableColor);
		g.fillRect (dropOffset1.x, dropOffset1.y, dropTable.width, dropTable.height);
		g.fillRect (dropOffset2.x, dropOffset2.y, dropTable.width, dropTable.height);

		// Outline drop tables
		g.setColor(Color.black);
		g.drawRect (dropOffset1.x, dropOffset1.y, dropTable.width, dropTable.height);
		g.drawRect (dropOffset2.x, dropOffset2.y, dropTable.width, dropTable.height);	
	}

	/** Draws all of the pieces in both drop tables.
	 * 
	 * @param g		the Graphics context in which to paint
	 */
	protected void drawDropTablePieces (Graphics g)
	{
		drawDropTablePieces (g, 1, state.getDropTable1());
		drawDropTablePieces (g, -1, state.getDropTable2());
	}

	/** Draws all of the pieces in the indicated order, based 
	 * on the given drop table ArrayList. The drop table is subdivided 
	 * with the following arrangement, with (0,0) being at the top left:
	 * <ul>
	 * <li> Pawns at (0,0) and (1,0)
	 * <li> Lances at (0,1)
	 * <li> Knights at (1,1)
	 * <li> Silver Generals at (0,2)
	 * <li> Gold Generals at (1,2)
	 * <li> Bishops at (0,3)
	 * <li> Rooks at (1,3)
	 * </ul>
	 *  
	 * @param g
	 * @param allegiance
	 * @param pieces
	 */	
	private void drawDropTablePieces (Graphics g, int allegiance, List<Piece> pieces)
	{
		int[][] pieceCounter = 
			{
				{0},
				{0,0},
				{0,0},
				{0,0}				
			};		

		for (int i = 0; i < pieces.size(); i++)
		{
			Point point = pieceToDropTable.get (pieces.get(i).pieceName.replace ("Promoted ", ""));

			if (point != null)
			{
				pieceCounter [point.y][point.x]++;			

				if (!(pieceIsSelected && pieces.get(i).equals(selectedPiece)))
				{
					Point location = getDropTableLocationOnPanel (allegiance, point);	
					location.x += pieceCounter [point.y][point.x] * tile.width / 5;
					drawPiece (g, pieces.get(i), location);	
				}
			}
			else
			{
				if (log)
					c.logError ("Warning: Can't draw king in drop table.");
			}
		}
	}

	/** Draws a shogi board square at the indicated location.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param back 		background color of square
	 * @param fore 		outline color of square
	 * @param p 		x,y coordinates at which to draw
	 */
	public void drawTile (Graphics g, Color back, Color fore, Point p)
	{
		drawTile (g, back, fore, p, tile.width, tile.height);
	}

	/** Draws a shogi board square at the indicated location.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param back 		background color of square
	 * @param fore 		outline color of square
	 * @param x 		x coordinate at which to draw
	 * @param y 		y coordinate at which to draw
	 */
	private void drawTile (Graphics g, Color back, Color fore, Point p, int width, int height)
	{
		// Fill
		g.setColor (back);
		g.fillRect (p.x, p.y, width, height);		

		// Outline
		g.setColor (fore);
		g.drawRect (p.x, p.y, width, height);		
	}	


	/** Draws the given shogi piece at the indicated location.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param p 		the piece
	 * @param point 	the x,y coordinates at which to draw
	 */
	public void drawPiece (Graphics g, Piece p, Point point)
	{
		int x = point.x + tile.width / 2;
		int y = point.y + tile.height / 2;
		g.drawString(p.getDoubleCharRepresentation(), x, y);
	}

	/** Converts the x,y coordinates of a point on the panel to the
	 * file,rank coordinates of a square on the shogi board. If the point
	 * is on a drop table, this method returns the point (-1, -1).
	 * If the point is on neither the board nor any drop table, this 
	 * method returns null.
	 * 
	 * @param location 		the x,y coordinates of the point to be converted
	 * 
	 * @return the file,rank coordinates of the square.
	 */
	private Tile getLocationOnBoard (Point location)
	{	
		Tile sq = null;
		if (mouseIsOnBoard ())
		{
			int x = (location.x - boardOffset.x) / tile.width;
			int y = 8 - ((location.y - boardOffset.y) / tile.height);
			sq = new Tile (x,y);
		}
		else if (mouseIsOnDropTable ())		
			sq = new Tile (-1, -1);	

		return sq;
	}

	/** Converts the file,rank coordinates of a square on the shogi board
	 * to x,y coordinates of a point on the Panel.
	 * 
	 * @param x 	x coordinate of square
	 * @param y 	y coordinate of square
	 * @return the x,y coordinates of the square relative to the source component
	 */
	private Point getBoardLocationOnPanel (int x, int y)
	{
		return getBoardLocationOnPanel (new Tile (x, y));
	}

	/** Converts the file,rank coordinates of a square on the shogi board
	 * to x,y coordinates of a point on the Panel.
	 * 
	 * @param sq 	the file,rank coordinates of the square to be converted
	 * @return the x,y coordinates of the square relative to the source component
	 */
	private Point getBoardLocationOnPanel (Tile sq)
	{
		int x = sq.x * tile.width + boardOffset.x;
		int y = (8 - sq.y) * tile.height + boardOffset.y ;	
		return new Point (x,y);
	}

	/** Converts the position coordinates of a square on the indicated
	 * drop table to x,y coordinates of a point on the Panel.
	 * 
	 * @param allegiance	the allegiance of the drop table
	 * @param sq			the position coordinates of the square to be converted
	 * @return the x,y coordinates of the square relative to the source component
	 */
	private Point getDropTableLocationOnPanel (int allegiance, Point sq)
	{			
		Point dropOffset = allegiance == 1 ? dropOffset1 : dropOffset2;			

		int x = (int)(sq.x * tile.width * 1.5) + dropOffset.x;
		int y = sq.y * tile.height + dropOffset.y;

		return new Point (x,y);
	}

	/** Converts the x,y coordinates of a point on the panel to
	 * position coordinates of a square on the indicated drop table. 
	 * Returns null if the point is not on the indicated drop table.
	 * 
	 * @param allegiance	the allegiance of the drop table
	 * @param location		the x,y coordinates of the point to be converted
	 * @return the position coordinates of a square on the drop table
	 */
	private Point getLocationOnDropTable (int allegiance, Point location)
	{
		Point sq = null;

		if (mouseIsOnDropTable (allegiance))
		{
			Point dropOffset = allegiance == 1 ? dropOffset1 : dropOffset2;
			int x = (location.x - dropOffset.x) / (int)(tile.width * 1.5);
			int y = ((location.y - dropOffset.y) / tile.height);
			sq = new Point (x,y);
		}
		return sq;
	}


	/** Checks if the mouse is currently hovering
	 * over the shogi board.
	 * 
	 * @return true if the mouse is on the board; false otherwise
	 */
	private boolean mouseIsOnBoard ()
	{
		boolean mouseIsOnBoard = false;		

		if (boardOffset.x <= mouse.x && mouse.x < boardOffset.x + tile.width * 9)		
			if (boardOffset.y <= mouse.y && mouse.y < boardOffset.y + tile.height * 9)			
				mouseIsOnBoard = true;		

		return mouseIsOnBoard;
	}

	/** Checks if the mouse is hovering over any drop table.
	 * 
	 * @return true if the mouse is on a drop table; false otherwise
	 */
	private boolean mouseIsOnDropTable ()
	{
		return mouseIsOnDropTable (-1) || mouseIsOnDropTable (1);
	}

	/** Checks if the mouse is hovering over a drop table 
	 * of the indicated allegiance. An allegiance of 1 means the
	 * bottom player; An allegiance of -1 means the top player.
	 *  
	 * @param allegiance 	The allegiance of the drop table.
	 * @return true if the mouse is on the indicated drop table; false otherwise
	 */
	private boolean mouseIsOnDropTable (int allegiance)
	{		
		Point dropOffset = allegiance == 1 ? dropOffset1 : dropOffset2;
		boolean mouseIsOnDropTable = false;

		if (dropOffset.x <= mouse.x && mouse.x < dropOffset.x + dropTable.width)
			if (dropOffset.y <= mouse.y && mouse.y < dropOffset.y + dropTable.height)
				mouseIsOnDropTable = true;		

		return mouseIsOnDropTable;
	}


	/** Updates the cursor's current location on the panel.
	 * 
	 * @param location 		the x,y coordinates of the cursor
	 */
	protected void moveMouse (Point location)
	{		
		mouse = location; 		
		repaint ();
	}

	/** Updates the mousePressed boolean array.
	 * 
	 * @param button 		the button that was pressed
	 * @param location 		the x,y coordinates of the cursor when the button was pressed
	 */
	protected void pressMouse (int button, Point location)
	{
		click [button] = location;
		mousePressed [button] = true;	

		if (button == 1)					
			selectPiece ();	
		repaint ();
	}

	/** Updates the mousePressed boolean array.
	 * 
	 * @param button 	the button that was released
	 */
	protected void releaseMouse (int button)
	{
		mousePressed [button] = false;

		if (button == 1 && pieceIsSelected)				
			releasePiece ();

		repaint ();
	}

	/** Selects the piece at the location of mouse 
	 * on its last click, if a valid piece is there.
	 */
	private void selectPiece ()
	{		
		Point location = click [1];

		if (mouseIsOnBoard ())
		{
			Tile tile = getLocationOnBoard (location);			
			Piece piece = state.getPieceAt(tile.x, tile.y);

			if (! (piece instanceof EmptyPiece))
			{
				pieceIsSelected = true;		
				selectedPiece = piece;
				if (log)
					c.logSelectPiece(piece);
			}
		}
		else if (mouseIsOnDropTable ())
		{
			int table = mouseIsOnDropTable (1) ? 1 : -1;
			Point point = getLocationOnDropTable (table, location);
			Piece piece = getDropTablePieceAt (table, point);

			if (piece != null)
			{
				pieceIsSelected = true;
				selectedPiece = piece;
				if (log)
					c.logSelectPiece(piece);
			}
		}
	}

	/** Releases the selected piece.
	 */
	private void releasePiece ()
	{
		pieceIsSelected = false;				
		Tile newPoint = getLocationOnBoard (mouse);

		if (!newPoint.equals(new Point (selectedPiece.x, selectedPiece.y)))		
			move (selectedPiece, newPoint);		

		if (log)
			c.logDeselectPiece(selectedPiece);
	}

	/** Moves the indicated piece to the indicated square on the board
	 * if it is a valid move.
	 * 
	 * @param piece 	the piece to be moved
	 * @param sq  		the file,rank coordinates of the new square 
	 */
	public boolean move (Piece piece, Tile sq)
	{	
		boolean successful = pieceIsSelected = false;

		if (piece.isValidMove (state, sq.x, sq.y))
		{		
			snap.play ();
			if (log)
				c.logValidMove(piece, new Tile (piece.x, piece.y), sq);
			piece.move(state, sq.x, sq.y);					
			promote (piece);

			successful = true;
		}		
		else
		{
			if (piece.x == -1 && piece.y == -1)
			{
				snap.play ();
				state.dropPieceFromTable(piece.allegiance, sq.x, sq.y, piece);
				if (log)
					c.logValidDrop(piece.allegiance, piece, sq);
			}
		}

		repaint ();	
		return successful;
	}

	/** Promotes the given piece, if it is valid.
	 * 
	 * @param piece		the piece to be promoted
	 */
	private void promote (Piece piece)
	{
		repaint ();		
		if (piece.isPromotable())
		{
			int result = JOptionPane.showConfirmDialog(this, "Promote piece?", "", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
			{
				snap.play();
				state.promotePieceAt (piece.x, piece.y);
				repaint ();	
				if (log)
					c.logPromote(piece);
			}
		}
	}

	/** Sets the size of squares on the shogi board to the given
	 * dimensions. Updates the positions of the drop tables accordingly.
	 * 
	 * @param width		the new width
	 * @param height	the new height
	 */
	public void setTileSize (int width, int height)
	{
		setTileSize (new Dimension (width, height));
	}

	/** Sets the size of squares on the shogi board to the given
	 * dimensions. Updates the positions of the drop tables accordingly.
	 * 
	 * @param sq	the new dimensions
	 */
	public void setTileSize (Dimension sq)
	{		
		dropOffset1 = new Point (boardOffset.x + 9 * sq.width, boardOffset.y + 5 * sq.height);
		dropOffset2 = new Point (boardOffset.x + 9 * sq.width, boardOffset.y);
		dropTable = new Dimension (sq.width * 3, sq.height * 4);
		tile = new Dimension (sq);
	}

	/** Sets the board offset to the given x,y coordinates. Updates the
	 * positions of the drop tables accordingly.
	 * 
	 * @param x		the new x offset
	 * @param y		the new y offset
	 */
	public void setBoardOffset (int x, int y)
	{
		setBoardOffset (new Point (x,y));
	}

	/** Sets the board offset to the given x,y coordinates. Updates the
	 * positions of the drop tables accordingly.
	 * 
	 * @param bo	the x,y coordinates
	 */
	public void setBoardOffset (Point bo)
	{				
		dropOffset1 = new Point (bo.x + 9 * tile.width, bo.y + 5 * tile.height);
		dropOffset2 = new Point (bo.x + 9 * tile.width, bo.y);
		boardOffset = new Point (bo.x, bo.y);
	}	

	/** Resets the board to its default configuration.
	 */
	protected void reset ()
	{	
		snap.play();
		state = new GameState ();
		state.defaultBoardConfigure();		
		repaint ();
		if (log)
			c.logReset();
	}	
}

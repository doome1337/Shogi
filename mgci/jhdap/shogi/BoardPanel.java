package mgci.jhdap.shogi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** The JPanel for the shogi board. Handles all of the drawing and sound.
 * Updates based on invocations of its moveMouse (Point), pressMouse (int, Point),
 * and releaseMouse (int) methods.
 * 
 * @author                  Jiayin Huang
 * @author                  Dmitry Andreevich Paramonov 
 */
public class BoardPanel extends JPanel 
{	
	/** Eclipse wants this variable for some reason.
	 */
	private static final long serialVersionUID = 1L;

	/** The default background color for the entire panel. 
	 */
	public Color backgroundColor = new Color (206, 192, 122);	


	// Offset and size fields

	/** The x,y coordinates of the top left corner of the shogi board. 
	 */
	private Point boardOffset = new Point ();
	/** The x,y coordinates of the top left corner of the bottom drop table.
	 */
	private Point dropTableOffset1 = new Point ();
	/** The x,y coordinates of the top left corner of the top drop table.
	 */
	private Point dropTableOffset2 = new Point ();
	/** The offset for drawing a piece in x,y coordinates relative to the
	 * top left corner of a tile. This is to ensure that the piece is always
	 * drawn at the center of a shogi board square.
	 */
	private Point pieceOffset = new Point ();	
	/** The width,height dimensions of a drop table. 
	 */
	private Dimension dropTableSize = new Dimension ();
	/** The width,height dimensions of a shogi piece. 
	 */
	private Dimension pieceSize = new Dimension ();
	/** The width,height dimensions of a square on the board. 
	 */
	private Dimension tileSize = new Dimension ();	


	// Mouse fields

	/** The location of the cursor when the corresponding mouse button was pressed. 
	 */
	protected Point [] click = new Point [4];

	/** The current location of the cursor.
	 */
	protected Point mouse = new Point ();

	/** Whether the mouse button is currently being pressed or not.
	 */	
	protected boolean [] mousePressed = new boolean [4];


	// Configuration (boolean) fields

	/** If true, pro_mode mode is active. This would mean that
	 *  all of the possible moves of a piece are not displayed. 
	 */
	public boolean proMode = false;

	/** If true, this panel draws the board coordinate labels (e.g. 1a, 2a, 2b). 
	 */
	public boolean showBoardLabels = true;

	/** If true, the previous move is indicated. 
	 */
	public boolean showLastMove = true;

	/** If true, the console (if open) logs every single little action 
	 */
	public boolean log = false;

	/** If true, the players take turns. 
	 */
	public boolean takeTurns = false;

	/** If true, the cursor is currently holding a piece. 
	 */
	private boolean pieceIsSelected = false;	

	/** The selected piece. 
	 */
	protected Piece selectedPiece;

	/** The location of the last move. 
	 */
	protected Tile lastMoved = null;

	/** The current player. 
	 */
	protected int turn = 1;

	/** The winner of the game. 1 indicates the bottom player as winner.
	 * -1 indicates top player as winner. 0 indicates no winner; 
	 */
	protected int winner = 0;


	// Other fields

	/** The relationship between shogi pieces and their positioning
	 * on the drop table, with (0,0) being at the top left of the drop table. 
	 */
	public static final Map<String,Point> pieceToDropTable;
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
	public static final Map<Point,String> dropTableToPiece;
	static
	{
		Map<Point,String> temp1 = new HashMap<Point,String>();

		for (Entry<String, Point> entry : pieceToDropTable.entrySet()) 		
			temp1.put(entry.getValue(), entry.getKey());

		dropTableToPiece = Collections.unmodifiableMap(temp1);		
	}


	/** The path to the active texture pack. 
	 */
	public String texturePath = "resources/stdTextures/";

	/** The HashMap storing the image IDs with their equivalent BufferedImages. 
	 */
	private static Map<String,BufferedImage> imageHash;	

	/** The sound that plays whenever a piece is moved or promoted.
	 */
	private SoundEffect snap = new SoundEffect (new File ("resources/snap.wav"));

	protected GraphicUI gui;

	/** The debug console. 
	 */
	protected ShogiConsole c = null;	

	/** The statistics panel. 
	 */
	protected StatsPanel s = null;

	/** The GameState object representing the shogi board. 
	 */
	protected GameState state = new GameState ();


	/** Creates a new BoardPanel. 
	 */
	public BoardPanel (GraphicUI parent)
	{			
		gui = parent;
		setBackground (backgroundColor);
		loadImages ();	
		reset ();			
	}

	/** Draws everything on the BoardPanel in the following order:
	 * <ul>
	 * <li> The board, including tiles and pieces.
	 * <li> The board labels. (e.g. 1b, 5i)
	 * <li> The drop tables.
	 * <li> The tile highlights. This includes all possible moves for the piece
	 * 		if a piece is selected, and the square the mouse is hovering over
	 * 		if a piece is not selected.
	 * <li> The pieces on the drop tables.
	 * <li> The selected piece.
	 * </ul>
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
		drawSelectedPiece (g);
	}

	/** Draws all of the shogi board tiles and all of the pieces on the 
	 * shogi board. Does not draw a piece if it is currently being selected 
	 * because in this case, it would not be technically "on" the board.
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
				drawTile (g, point);
				if (new Tile (i,j).equals(lastMoved))
					drawLastMovedTile (g, point);

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
				point.translate (18, -5);		//TODO		
				g.drawString (file, point.x, point.y);	

				point = getBoardLocationOnPanel (i, 0);
				point.translate (18, tileSize.height + 15); //TODO
				g.drawString (file, point.x, point.y);	
			}
		}
	}

	/** Redraws a highlighted shogi board square on top of a normal  
	 * shogi square based on the state and positioning of the mouse. 
	 * Highlights a square if (1) the mouse is hovering over the 
	 * square, (2) the square is not empty, and (3) and the mouse 
	 * is not being pressed. Highlights all possible moves for
	 * a selected piece if (1) the mouse is being pressed, and (2) 
	 * a piece is selected.
	 *  
	 * @param g 	the Graphics context in which to paint
	 */
	protected void drawHighlights (Graphics g)
	{			
		if (pieceIsSelected)			
			drawPossibleMoves (g, selectedPiece);		
		else
		{
			if (mouseIsOnBoard ())			
				drawHighlightsOnBoard (g);
			else if (mouseIsOnDropTable ())
				drawHighlightsOnDropTable (g);			
		}			
	}

	/** Automatically called by the drawHighlights (Graphics) method.
	 * Handles the drawing of highlighted squares on the main board.
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	private void drawHighlightsOnBoard (Graphics g)
	{
		Tile boardCoords = getLocationOnBoard (mouse);	
		Point point = getBoardLocationOnPanel (boardCoords);
		Piece piece = state.getPieceAt(boardCoords.x, boardCoords.y);
		if (!(piece instanceof EmptyPiece))
		{	
			if (takeTurns && piece.allegiance == turn || !takeTurns)
			{			
				drawHighlightedTile (g, point);
				if (boardCoords.equals(lastMoved))
					drawLastMovedTile (g, point);
				drawPiece (g, piece, point);
			}
		}	
	}

	/** Automatically called by the drawHighlights (Graphics) method.
	 * Handles the drawing of highlighted squares on the drop tables.
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	private void drawHighlightsOnDropTable (Graphics g) //TODO
	{
		int table = mouseIsOnDropTable (1) ? 1 : -1;
		if (takeTurns && table == turn || !takeTurns)
		{
			Point tableCoords = getLocationOnDropTable (table, mouse);				
			Piece p = getDropTablePieceAt (table, tableCoords);	
			if (p != null)
			{
				Point point = getDropTableLocationOnPanel (table, tableCoords);			

				// Pawns on the drop table take up twice as much space
				int width = p instanceof Pawn ? dropTableSize.width : dropTableSize.width / 2;
				drawHighlightedTile (g, point, width, dropTableSize.height / 4);
			}
		}
	}

	/** Draws a highlighted shogi board square at the indicated location
	 * 
	 * @param g			the Graphics context in which to paint
	 * @param p			the x,y coordinates at which to draw
	 */
	private void drawHighlightedTile (Graphics g, Point p)
	{
		drawHighlightedTile (g, p, tileSize.width, tileSize.height);
	}

	private void drawHighlightedTile (Graphics g, Point p, int width, int height) // TODO annotate
	{
		BufferedImage img = getImageHash ("TileSelected");
		g.drawImage (img, p.x, p.y, width, height, null);
	}

	private void drawLastMovedTile (Graphics g, Point p)
	{
		BufferedImage img = getImageHash ("TileMoved");
		g.drawImage (img, p.x, p.y, tileSize.width, tileSize.height, null);
	}

	private void drawSelectedPiece (Graphics g)
	{
		if (pieceIsSelected)
		{
			Point point = new Point (mouse);
			point.translate(-pieceOffset.x - pieceSize.width / 2, -pieceOffset.y - pieceSize.height / 2);
			drawPiece (g, selectedPiece, point);
		}
	}


	/** Returns the last occurring piece that matches the type 
	 * of piece that is located at the indicated coordinates
	 * of the indicated drop table allegiance. An allegiance of
	 * 1 indicates the bottom table; -1 indicates the top table.
	 * 
	 * @param table		the allegiance of the drop table
	 * @param sq		the location on the drop table
	 * @return the matching piece in the drop table ; null if no match
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
	 * if no match is found. (An allegiance of 1 indicates
	 * the bottom table; -1 indicates the top table.)
	 * 
	 * @param table		the allegiance of the drop table
	 * @param name		the name of the piece
	 * @return the matching piece in the drop table ; null if no match
	 */
	protected Piece getDropTablePieceAt (int table, String name)
	{
		List<Piece> list = state.getCorrectDropTable (table);

		Piece piece = null;
		for (int i = list.size() - 1; i >= 0 && piece == null; i--)
		{		
			if (name.equalsIgnoreCase (list.get (i).pieceName) ||
					name.equalsIgnoreCase(list.get(i).doubleCharRepresentation[0].substring (0,1)))
				piece = list.get(i);
		}
		return piece;		
	}	

	/** Redraws all of the shogi board squares to which it is
	 * legally valid for the given piece to move. Redraws 
	 * these squares as a highlighted square. Also redraws 
	 * the shogi pieces on these squares.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param piece 	the shogi piece
	 */
	public void drawPossibleMoves (Graphics g, Piece piece)
	{	
		if (!proMode)
		{
			boolean [][] moves = piece.generateMoves(state);

			for (int i = 0; i < 9; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					if (moves [j][i])
					{
						Point point = getBoardLocationOnPanel (new Tile (j, i));
						drawHighlightedTile (g, point);
						if (new Tile (j, i).equals(lastMoved))
							drawLastMovedTile (g, point);
						drawPiece (g, state.getPieceAt(j, i), point);
					}
				}
			}
		}
	}

	/** Draws both drop tables.
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	protected void drawDropTables (Graphics g)
	{			
		drawDropTable (g, 1);
		drawDropTable (g, -1);
	}

	/** Draws the drop table of the indicated allegiance.
	 * An allegiance of 1 indicates the bottom player; -1
	 * indicates the top player.
	 * 
	 * @param g 			the Graphics context in which to paint
	 * @param allegiance	the allegiance of the drop table to draw
	 */
	protected void drawDropTable (Graphics g, int allegiance)
	{
		Point p = allegiance == 1 ? dropTableOffset1 : dropTableOffset2;
		Dimension s = dropTableSize;

		BufferedImage img = imageHash.get("DropTable");
		g.drawImage (img, p.x, p.y, s.width, s.height, null);		
	}

	/** Draws all of the pieces in both drop tables.
	 * 
	 * @param g		the Graphics context in which to paint
	 */
	protected void drawDropTablePieces (Graphics g)
	{
		drawDropTablePieces (g, 1);
		drawDropTablePieces (g, -1);
	}

	/** Draws all of the pieces in the indicated order, based 
	 * on the drop table of the given allegiance. An allegiance of 1
	 * indicates the bottom table; -1 indicates the top table. 
	 * The drop table is subdivided with the following arrangement, 
	 * with (0,0) being at the top left:
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
	 * @param g				the Graphics context in which to paint
	 * @param allegiance	the allegiance of the drop table
	 * @param pieces		the ArrayList containing the pieces on the drop table
	 */	
	private void drawDropTablePieces (Graphics g, int allegiance)
	{
		List<Piece> pieces = state.getCorrectDropTable (allegiance);
		int[][] pieceCounter = 
			{
				{0},
				{0,0},
				{0,0},
				{0,0}				
			};		

		for (int i = 0; i < pieces.size(); i++)
		{
			Point point = pieceToDropTable.get (pieces.get(i).pieceName);

			if (point != null)
			{
				pieceCounter [point.y][point.x]++;			

				if (!(pieceIsSelected && pieces.get(i).equals(selectedPiece)))
				{
					Point location = getDropTableLocationOnPanel (allegiance, point);	
					location.x += pieceCounter [point.y][point.x] * dropTableSize.width / 15;
					drawPiece (g, pieces.get(i), location);	
				}
			}
			else			
				if (log)
					c.logError ("Warning: Can't draw king in drop table.");			
		}
	}

	/** Draws a shogi board square at the indicated location.
	 * 
	 * @param g 		the Graphics context in which to paint	
	 * @param p 		x,y coordinates at which to draw
	 */
	public void drawTile (Graphics g, Point p)
	{	
		BufferedImage img = getImageHash ("Tile");
		g.drawImage (img, p.x, p.y, tileSize.width, tileSize.height, null);
	}	

	/** Draws the given shogi piece at the indicated location. This method
	 * takes into account the pieceOffset.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param p 		the piece
	 * @param point 	the x,y coordinates at which to draw
	 */
	public void drawPiece (Graphics g, Piece p, Point point)
	{	
		BufferedImage img = getImageHash (p.getClass().getSimpleName() + p.allegiance);
		int x = point.x + pieceOffset.x;
		int y = point.y + pieceOffset.y;
		g.drawImage (img, x, y, pieceSize.width, pieceSize.height, null);
	}

	/** Converts the x,y coordinates of a point on the panel to the
	 * GameState file,rank coordinates of a square on the 
	 * shogi board. If the point is on a drop table, this method 
	 * returns the point -1,-1. If the point is on neither the 
	 * board nor any drop table, this method returns null.
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
			int x = (location.x - boardOffset.x) / tileSize.width;
			int y = 8 - ((location.y - boardOffset.y) / tileSize.height);
			sq = new Tile (x,y);
		}
		else if (mouseIsOnDropTable ())		
			sq = new Tile (-1, -1);	

		return sq;
	}

	/** Converts the GameState file,rank coordinates of a square 
	 * on the shogi board to x,y coordinates of a point on the Panel.
	 * 
	 * @param x 	GameState file coordinate of square
	 * @param y 	GameState rank coordinate of square
	 * @return the x,y coordinates of the square relative to the source component
	 */
	private Point getBoardLocationOnPanel (int x, int y)
	{
		return getBoardLocationOnPanel (new Tile (x, y));
	}

	/** Converts the GameState file,rank coordinates of a square 
	 * on the shogi board to x,y coordinates of a point on the Panel.
	 * 
	 * @param sq 	the file,rank coordinates of the square to be converted
	 * @return the x,y coordinates of the square relative to the source component
	 */
	private Point getBoardLocationOnPanel (Tile sq)
	{
		int x = sq.x * tileSize.width + boardOffset.x;
		int y = (8 - sq.y) * tileSize.height + boardOffset.y ;	
		return new Point (x,y);
	}

	/** Converts the position coordinates of a tile on the indicated
	 * drop table to x,y coordinates of a point on the Panel.
	 * 
	 * @param allegiance	the allegiance of the drop table
	 * @param sq			the drop table position coordinates to be converted
	 * @return the x,y coordinates of the tile relative to the source component
	 */
	private Point getDropTableLocationOnPanel (int allegiance, Point sq)
	{			
		Point dropOffset = allegiance == 1 ? dropTableOffset1 : dropTableOffset2;			

		int x = sq.x * dropTableSize.width / 2 + dropOffset.x;
		int y = sq.y * dropTableSize.height / 4 + dropOffset.y;

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
			Point dropOffset = allegiance == 1 ? dropTableOffset1 : dropTableOffset2;
			int x = (location.x - dropOffset.x) / (dropTableSize.width / 2);
			int y = (location.y - dropOffset.y) / (dropTableSize.height / 4);
			sq = new Point (x,y);
		}
		return sq;
	}

	/** Finds the BufferedImage in the imageHash that matches
	 * the given identifier. Returns null if no match is found.
	 * Also automatically prints a log message in the debug
	 * console if no match is found, if logging is enabled.
	 * 
	 * @param id	the image identifier
	 * @return		the matching BufferedImage
	 */
	private BufferedImage getImageHash (String id)
	{
		BufferedImage img = null;
		try
		{
			img = imageHash.get (id);
		}
		catch (Exception e)
		{
			if (log)
				c.logError ("Could not find a match for \"" + id + "\" in preloaded images.");
		}

		return img;
	}


	/** Checks if the mouse is currently
	 * hovering over the shogi board.
	 * 
	 * @return true if the mouse is on the board; false otherwise
	 */
	private boolean mouseIsOnBoard ()
	{
		boolean mouseIsOnBoard = false;		

		if (boardOffset.x <= mouse.x && mouse.x < boardOffset.x + tileSize.width * 9)		
			if (boardOffset.y <= mouse.y && mouse.y < boardOffset.y + tileSize.height * 9)			
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
	 * bottom player; -1 means the top player.
	 *  
	 * @param allegiance 	The allegiance of the drop table.
	 * @return true if the mouse is on the indicated drop table; false otherwise
	 */
	private boolean mouseIsOnDropTable (int allegiance)
	{		
		Point dropOffset = allegiance == 1 ? dropTableOffset1 : dropTableOffset2;
		boolean mouseIsOnDropTable = false;

		if (dropOffset.x <= mouse.x && mouse.x < dropOffset.x + dropTableSize.width)
			if (dropOffset.y <= mouse.y && mouse.y < dropOffset.y + dropTableSize.height)
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

			if (takeTurns && piece.allegiance == turn || !takeTurns)
			{
				if (! (piece instanceof EmptyPiece))
				{				
					pieceIsSelected = true;		
					selectedPiece = piece;
					if (log)
						c.logSelectPiece(piece);
				}
			}
		}
		else if (mouseIsOnDropTable ())
		{
			int table = mouseIsOnDropTable (1) ? 1 : -1;
			if (takeTurns && table == turn || !takeTurns)
			{
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
	}

	/** Releases the selected piece.
	 */
	private void releasePiece ()
	{
		pieceIsSelected = false;	
		if (mouseIsOnBoard ())
		{
			Tile newPoint = getLocationOnBoard (mouse);

			if (!newPoint.equals(new Point (selectedPiece.x, selectedPiece.y)))		
				move (selectedPiece, newPoint);	
		}
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
		Move move = null;		
		boolean successful = pieceIsSelected = false;

		if (winner == 0)
		{
			if (piece.isValidMove (state, sq.x, sq.y))
			{		
				snap.play ();
				move = new Move (state, piece, sq);
				if (piece.x == -1 && piece.y == -1)
				{						
					state.dropPieceFromTable(piece.allegiance, sq.x, sq.y, piece);
					if (showLastMove)
						lastMoved = sq;
					if (log)
						c.logValidDrop(piece.allegiance, piece, sq);
				}
				else
				{				
					if (log)
						c.logValidMove(piece, new Tile (piece.x, piece.y), sq);
					if (showLastMove)
						lastMoved = sq;
					piece.move(state, sq.x, sq.y);

					if (promote (piece))
						move.promote();
				}
				successful = true;	
				turn *= -1;
				s.switchTurn ();
				s.addMove(move);

				if (state.isKingCheckmated(-piece.allegiance))									
					gui.putWinner(piece.allegiance);				
			}
			else if (proMode)
				gui.putWinner(-piece.allegiance);				
		}
		repaint ();	
		return successful;
	}



	/** Promotes the given piece, if it is valid.
	 * 
	 * @param piece		the piece to be promoted
	 */
	private boolean promote (Piece piece)
	{
		boolean promoted = false;
		repaint ();		
		if (piece.isPromotable())
		{
			int result = JOptionPane.YES_OPTION;			 
			if (!piece.mustPromoteIfMoved (state, piece.x, piece.y))
				result = JOptionPane.showConfirmDialog(this, "Promote piece?", "", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
			{
				snap.play();
				state.promotePieceAt (piece.x, piece.y);
				promoted = true;
				repaint ();	
				if (log)
					c.logPromote(piece);
			}
		}
		return promoted;
	}


	public Point getBoardOffset ()
	{
		return boardOffset;
	}

	public Dimension getDropTableSize ()
	{
		return dropTableSize;
	}

	public Point getPieceOffset ()
	{
		return pieceOffset;
	}

	public Dimension getPieceSize ()
	{
		return pieceSize;
	}

	public Dimension getTileSize ()
	{
		return tileSize;
	}


	/** Sets the board offset to the given x,y coordinates.
	 * Recalculates the drop table offsets accordingly.
	 * 
	 * @param x		x coordinate of new board offset
	 * @param y		y coordinate of new board offset
	 */
	public void setBoardOffset (int x, int y)
	{	
		boardOffset = new Point (x, y);
		recalculateDropTableOffsets ();
		repaint ();
	}

	/** Sets the size of the drop tables to the given
	 * dimensions. Recalculates the drop table offsets 
	 * accordingly.
	 * 
	 * @param width		new width of drop table
	 * @param height	new height of drop table
	 */
	public void setDropTableSize (int width, int height)
	{
		dropTableSize = new Dimension (width, height);
		recalculateDropTableOffsets ();
		repaint ();
	}	


	/** Sets the size of a shogi piece to the given dimensions.
	 * Recalculates the piece offset accordingly.
	 * 
	 * @param width		new width of shogi piece
	 * @param height	new height of shogi piece
	 */
	public void setPieceSize (int width, int height)
	{						
		pieceSize = new Dimension (width, height);
		recalculatePieceOffset ();
		repaint ();
	}

	/** Sets the size of squares on the shogi board to the given
	 * dimensions. Recalculates the drop table and piece offsets
	 * accordingly.
	 * 
	 * @param width		new width of shogi square
	 * @param height	new height of shogi square
	 */
	public void setTileSize (int width, int height)
	{	
		tileSize = new Dimension (width, height);
		recalculateDropTableOffsets ();
		recalculatePieceOffset ();
		repaint ();
	}	

	/** Recalculates the drop table offsets based on the 
	 * board offset, tile size, and drop table size. Ensures that 
	 * the top drop table meets the top edge of the board, the
	 * bottom drop table meets the bottom edge of the board, and 
	 * both drop tables meet the right edge of the board. 
	 */
	private void recalculateDropTableOffsets ()
	{
		if (boardOffset != null && tileSize != null && dropTableSize != null)
		{
			int x = boardOffset.x + 9 * tileSize.width;
			int y = boardOffset.y + 9 * tileSize.height - dropTableSize.height;
			dropTableOffset1 = new Point (x, y);
			dropTableOffset2 = new Point (x, boardOffset.y);
		}
		else		
			if (log)
				c.logError("Attempted to recalculate dropTableOffset with null values.");
	}

	/** Recalculates the piece offset so that whenever a piece
	 * is drawn, it will always be centered on the shogi square. 
	 */
	private void recalculatePieceOffset ()
	{
		if (tileSize != null)
		{
			int x = (tileSize.width - pieceSize.width) / 2;
			int y = (tileSize.height - pieceSize.height) / 2;
			pieceOffset = new Point (x,y);	
		}
		else		
			if (log)
				c.logError("Attempted to recalculate pieceOffset with null tileSize.");		
	}

	/** Resets the board to its default configuration.
	 */
	protected void reset ()
	{	
		snap.play();
		state = new GameState ();
		state.defaultBoardConfigure();	
		lastMoved = null;	
		winner = 0;
		repaint ();
		if (log)
			c.logReset();
	}	

	/** Loads all of the images from the indicated
	 * texture pack path into the local HashMap imageHash 
	 * field. Initializes the dropTableSize, tileSize,
	 * and pieceSize fields based on the sizes of the
	 * loaded images.
	 */
	public void loadImages ()
	{
		imageHash = new HashMap<String,BufferedImage>();
		BufferedImage pieceImg = null;
		BufferedImage tileImg = null;
		BufferedImage dropImg = null;
		String id;

		// Load piece images

		Piece[] pieces = 
			{
				new Bishop (0,0,0),
				new EmptyPiece (0,0),
				new GoldGeneral (0,0,0),
				new King (0,0,0),
				new Knight (0,0,0),
				new Lance (0,0,0),
				new Pawn (0,0,0),				
				new PromotedBishop (0,0,0),
				new PromotedKnight (0,0,0),
				new PromotedLance (0,0,0),
				new PromotedPawn (0,0,0),
				new PromotedRook (0,0,0),
				new PromotedSilverGeneral (0,0,0),
				new Rook (0,0,0),
				new SilverGeneral (0,0,0)		
			}
		;

		for (int i = 0; i < pieces.length; i++)
		{	
			for (int j = 0; j < 3; j++)
			{
				pieceImg = readImage (pieces[i].imageNames[j]);
				id = pieces[i].getClass().getSimpleName() + (j - 1);
				imageHash.put(id, pieceImg);
			}
		}

		// Load tiles

		tileImg = readImage ("tileMoved.png");
		id = "TileMoved";
		imageHash.put(id, tileImg);	

		tileImg = readImage ("tileSelected.png");
		id = "TileSelected";
		imageHash.put(id, tileImg);	

		tileImg = readImage ("tile.png");
		id = "Tile";
		imageHash.put(id, tileImg);	

		// Load drop table

		dropImg = readImage ("dropTable.png");
		id = "DropTable";
		imageHash.put(id, dropImg);

		// Initializing sizes

		try
		{
			setPieceSize (pieceImg.getWidth(), pieceImg.getHeight());
			setTileSize (tileImg.getWidth(), tileImg.getHeight());
			setDropTableSize (dropImg.getWidth(), dropImg.getHeight());

			int x = tileImg.getWidth() * 5 / 8;
			int y = tileImg.getHeight() * 5 / 8;
			setBoardOffset (x, y);
		}
		catch (Exception e)
		{
			if (log)
			{
				c.logError ("Could not initialize pieceSize, tileSize and dropTableSize.");
				c.logError("Piece images, tile images, or drop table images are missing from texture pack.");
			}
		}
	}

	public BufferedImage readImage (String imageName)
	{
		File path = new File (texturePath + imageName);		
		BufferedImage img = null;
		try
		{			
			img = ImageIO.read(path);
		}
		catch (Exception e)
		{			
			if (log)
				if (!imageName.startsWith ("n"))
					if (!imageName.contains ("Empty"))
						c.logError("Texture file for " + path + " not found.");
		}
		return img;
	}
}

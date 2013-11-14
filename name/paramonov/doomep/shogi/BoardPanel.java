package name.paramonov.doomep.shogi;

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

	/** The color of the drop tables. 
	 */
	public static Color dropTableColor = new Color (211, 211, 223);

	/** The color of the shogi board squares. 
	 */
	public static Color boardColor = new Color (223, 180, 114);

	/** The color of a highlighted shogi board square. 
	 */
	public static Color highlightColor = Color.green;

	/** The x,y coordinates of the top left corner of the shogi board. 
	 */
	private static Point boardOffset;	

	/** The x,y coordinates of the top left corner of the bottom drop table.
	 */
	private static Point dropOffset1;

	/** The x,y coordinates of the top left corner of the top drop table.
	 */
	private static Point dropOffset2;

	/** The dimensions of a shogi board square. 
	 */
	private static Dimension square;

	/** The dimensions of a drop table.
	 */
	private static Dimension dropTable;	

	/** The board. 
	 */
	protected static GameState state = new GameState ();

	private static final Map<String,Point> pieceToDropTable;
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

	private static final Map<Point,String> dropTableToPiece;
	static
	{
		Map<Point,String> temp1 = new HashMap<Point,String>();

		for (Entry<String, Point> entry : pieceToDropTable.entrySet()) 		
			temp1.put(entry.getValue(), entry.getKey());

		dropTableToPiece = Collections.unmodifiableMap(temp1);		
	}

	/** Whether a shogi piece is selected or not. 
	 */
	protected static boolean isPieceSelected = false;

	/** The selected shogi piece. 
	 */
	protected static Piece selectedPiece;

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
	public boolean hardcoreMode = false;
	
	private SoundEffects se = new SoundEffects (new File ("../Shogi/resources/snap.wav"));
		

	/** Creates a new BoardPanel. 
	 */
	public BoardPanel ()
	{
		reset ();

		boardOffset = new Point (30, 30);
		square = new Dimension (40, 40);

		setBoardOffset (boardOffset);	
		setSquareSize (square);				
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
		drawDropTables (g);	
		drawHighlight (g);
		drawDropTablePieces (g);
	}

	/** Draws the shogi board and all of the pieces on it.
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	private void drawBoard (Graphics g)
	{
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{				
				Piece piece = state.getPieceAt(i, j);
				Point point = getBoardLocationOnPanel (i, j);

				drawSquare (g, boardColor, Color.black, point);	

				if (!(isPieceSelected && piece.equals(selectedPiece)))
				{
					point.translate (square.width / 2, square.height / 2);
					drawPiece (g, piece, point);
				}
			}        
		}  	
	}

	/** Redraws a shogi board square with a highlighted background color
	 * based on the state and positioning of the mouse. Highlights a square
	 * if the mouse (1) is hovering over the square, (2) the square is not empty, 
	 * (3) and the mouse is not pressed. Or, if (1) the mouse is pressed and (2) a piece 
	 * is selected, this method highlights all possible moves for the selected piece.
	 *  
	 * @param g 	the Graphics context in which to paint
	 */
	private void drawHighlight (Graphics g)
	{		
		// Draw hover outline

		if (isPieceSelected)
		{		
			drawPossibleMoves (g, selectedPiece);
			drawPiece (g, selectedPiece, mouse.x, mouse.y);
		}
		else
		{
			if (mouseIsOnBoard ())
			{
				Point boardCoords = getLocationOnBoard (mouse);	
				Point point = getBoardLocationOnPanel (boardCoords);
				if (!(state.getPieceAt(boardCoords.x, boardCoords.y) instanceof EmptyPiece))
				{	
					Piece piece = state.getPieceAt(boardCoords.x, boardCoords.y);

					drawSquare (g, highlightColor, Color.black, point);

					point.translate (square.width / 2, square.height / 2);
					drawPiece (g, piece, point);
				}		
			}
			else if (mouseIsOnDropTable ())
			{
				int table = mouseIsOnDropTable (1) ? 1 : -1;
				Point boardCoords = getLocationOnDropTable (table, mouse);				
				Piece p = getDropTablePieceAt (table, boardCoords);	
				if (p != null)
				{
					Point point = getDropTableLocationOnPanel (table, boardCoords);
					drawSquare (g, highlightColor, Color.black, point);
					int width = p instanceof Pawn ? 3 * square.width : (int) (1.5 * square.width);

					drawSquare (g, highlightColor, Color.black, point, width, square.height);
				}
			}
		}			
	}

	private Piece getDropTablePieceAt (int table, Point sq)
	{	
		if (sq.equals(new Point (1,0)))
			sq.setLocation(0,0);
		String name = dropTableToPiece.get(sq);
		List<Piece> list = state.getCorrectDropTable (table);

		Piece piece = null;
		for (Piece p : list)		
			if (p.pieceName.equals (name))
				piece = p;	

		return piece;
	}

	/** Redraws all of the shogi board squares to which it is
	 * legally valid for the given piece to move. Redraws 
	 * these squares with a highlighted background color.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param piece 	the shogi piece
	 */
	private void drawPossibleMoves (Graphics g, Piece piece)
	{
		boolean [][] moves = piece.generateMoves(state);

		if (!hardcoreMode)
		{
			for (int i = 0; i < 9; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					if (moves [j][i])
					{
						Point point = getBoardLocationOnPanel (new Point (j, i));
						drawSquare (g, highlightColor, Color.black, point);
						drawPiece (g, state.getPieceAt(j, i), point.x + square.width / 2, point.y + square.height / 2);
					}
				}
			}
		}
	}

	/** Draws the drop tables and all of the pieces on them.
	 * 
	 * @param g 	the Graphics context in which to paint
	 */
	private void drawDropTables (Graphics g)
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

	private void drawDropTablePieces (Graphics g)
	{
		drawDropTablePieces (g, 1, state.getDropTable1());
		drawDropTablePieces (g, -1, state.getDropTable2());
	}

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
				pieceCounter [point.y][point.x]++;
			else 
				System.out.println (new Exception ("InvalidCapturePieceException"));

			if (!(isPieceSelected && pieces.get(i).equals(selectedPiece)))
			{
				Point location = getDropTableLocationOnPanel (allegiance, point);			
				location.translate (square.width / 2, square.height / 2);
				location.x += pieceCounter [point.y][point.x] * square.width / 5;
				drawPiece (g, pieces.get(i), location);	
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
	private void drawSquare (Graphics g, Color back, Color fore, Point p)
	{
		drawSquare (g, back, fore, p, square.width, square.height);
	}

	/** Draws a shogi board square at the indicated location.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param back 		background color of square
	 * @param fore 		outline color of square
	 * @param x 		x coordinate at which to draw
	 * @param y 		y coordinate at which to draw
	 */
	private void drawSquare (Graphics g, Color back, Color fore, Point p, int width, int height)
	{
		// Fill
		g.setColor (back);
		g.fillRect (p.x, p.y, width, height);
		g.setColor (Color.black);

		// Outline
		g.setColor (fore);
		g.drawRect (p.x, p.y, width, height);
		g.setColor (Color.black);
	}	

	/** Draws the given shogi piece at the indicated location.
	 * 
	 * @param g 		the Graphics context in which to paint
	 * @param p 		the piece
	 * @param point 	the x,y coordinates at which to draw
	 */
	private void drawPiece (Graphics g, Piece p, Point point)
	{
		drawPiece (g, p, point.x, point.y);
	}

	/** Draws the given shogi piece at the indicated location on the Panel.
	 * 
	 * @param g 	the Graphics context in which to paint
	 * @param p 	the piece
	 * @param x 	the x coordinate at which to draw
	 * @param y 	the y coordinate at which to draw
	 */
	private void drawPiece (Graphics g, Piece p, int x, int y)
	{		
		g.drawString(p.getDoubleCharRepresentation(), x, y);
	}


	/** Converts the x,y coordinates of a point on the panel to the
	 * file,rank coordinates of a square on the shogi board. If the point
	 * is on a drop table, this method returns the point (-1, -1).
	 * If the point is on neither the board nor any drop table, this 
	 * method returns the point (-9999, -9999).
	 * 
	 * @param location 		the x,y coordinates of the point to be converted
	 * 
	 * @return the file,rank coordinates of the square.
	 */
	private Point getLocationOnBoard (Point location)
	{
		Point sq = new Point (-9999, -9999);

		if (mouseIsOnBoard ())
		{
			sq.x = (location.x - boardOffset.x) / square.width;
			sq.y = 8 - ((location.y - boardOffset.y) / square.height);			
		}
		else if (mouseIsOnDropTable ())
		{
			sq = new Point (-1, -1);
		}

		return sq;
	}

	/** Converts the file,rank coordinates of a square on the shogi board
	 * to x,y coordinates of a point on the Panel.
	 * 
	 * @param x 	file coordinate of square
	 * @param y 	rank coordinate of square
	 * @return the x,y coordinates of the square relative to the source component
	 */
	private Point getBoardLocationOnPanel (int x, int y)
	{
		return getBoardLocationOnPanel (new Point (x, y));
	}

	/** Converts the file,rank coordinates of a square on the shogi board
	 * to x,y coordinates of a point on the Panel.
	 * 
	 * @param sq 	the file,rank coordinates of the square
	 * @return the x,y coordinates of the square relative to the source component
	 */
	private Point getBoardLocationOnPanel (Point sq)
	{
		Point location = new Point ();

		location.x = sq.x * square.width + boardOffset.x;
		location.y = (8 - sq.y) * square.height + boardOffset.y ;

		return location;
	}

	private Point getDropTableLocationOnPanel (int allegiance, Point sq)
	{	
		Point location = new Point ();
		Point dropOffset = allegiance == 1 ? dropOffset1 : dropOffset2;			

		location.x = (int)(sq.x * square.width * 1.5) + dropOffset.x;
		location.y = sq.y * square.height + dropOffset.y;

		return location;
	}

	private Point getLocationOnDropTable (int allegiance, Point location)
	{
		Point sq = new Point (-9999, -9999);

		if (mouseIsOnDropTable (allegiance))
		{
			Point dropOffset = allegiance == 1 ? dropOffset1 : dropOffset2;
			sq.x = (location.x - dropOffset.x) / (int)(square.width * 1.5);
			sq.y = ((location.y - dropOffset.y) / square.height);
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

		if (mouse.x >= boardOffset.x && mouse.x < boardOffset.x + square.width * 9)		
			if (mouse.y >= boardOffset.y && mouse.y < boardOffset.y + square.height * 9)			
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
	 * of the given allegiance. An allegiance of 1 means the
	 * bottom player; An allegiance of -1 means the top player.
	 *  
	 * @param allegiance 	The allegiance of the drop table.
	 * @return true if the mouse is on the indicated drop table; false otherwise
	 */
	private boolean mouseIsOnDropTable (int allegiance)
	{
		boolean mouseIsOnDropTable = false;

		if (allegiance == 1)
		{
			if (mouse.x >= dropOffset1.x && mouse.x <= dropOffset1.x + dropTable.width)
				if (mouse.y >= dropOffset1.y && mouse.y <= dropOffset1.y + dropTable.height)
					mouseIsOnDropTable = true;
		}
		else if (allegiance == -1)
		{
			if (mouse.x >= dropOffset2.x && mouse.x <= dropOffset2.x + dropTable.width)
				if (mouse.y >= dropOffset2.y && mouse.y <= dropOffset2.y + dropTable.height)
					mouseIsOnDropTable = true;
		}

		return mouseIsOnDropTable;
	}


	/** Updates the cursor's current location on the panel.
	 * 
	 * @param location 		the x,y coordinates of the cursor
	 */
	protected void moveMouse (Point location)
	{		
		mouse = location; 		
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
	}

	/** Updates the mousePressed boolean array.
	 * 
	 * @param button 	the button that was released
	 */
	protected void releaseMouse (int button)
	{
		mousePressed [button] = false;

		if (button == 1 && isPieceSelected)				
			releasePiece ();				
	}

	/** Selects the piece at the location of mouse 
	 * on its last click, if a valid piece is there.
	 */
	private void selectPiece ()
	{		
		Point location = click [1];

		if (mouseIsOnBoard ())
		{
			Point point = getLocationOnBoard (location);			
			Piece piece = state.getPieceAt(point.x, point.y);

			if (! (piece instanceof EmptyPiece))
			{
				isPieceSelected = true;		
				selectedPiece = piece;
			}
		}
		else if (mouseIsOnDropTable ())
		{
			int table = mouseIsOnDropTable (1) ? 1 : -1;
			Point point = getLocationOnDropTable (table, location);
			Piece piece = getDropTablePieceAt (table, point);

			if (piece != null)
			{
				isPieceSelected = true;
				selectedPiece = piece;
			}
		}
	}

	/** Releases the selected piece.
	 */
	private void releasePiece ()
	{
		isPieceSelected = false;
		Point newPoint = getLocationOnBoard (mouse);

		if (!newPoint.equals(new Point (selectedPiece.x, selectedPiece.y)))
		{
			move (selectedPiece, newPoint);		
		}
	}

	/** Moves the given piece to the indicated square on the board
	 * if it is a valid move.
	 * 
	 * @param piece 	the piece to be moved
	 * @param sq  		the file,rank coordinates of the new square 
	 */
	private void move (Piece piece, Point sq)
	{
		if (piece.isValidMove (state, sq.x, sq.y))
		{		
			se.snap ();
			if (piece.x == -1 && piece.y == -1)
				state.dropPieceFromTable(piece.allegiance, sq.x, sq.y, piece);
			else
			{			
				piece.move(state, sq.x, sq.y);			
				promote (piece);
			}
		}		
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
				se.snap();
				state.promotePieceAt (piece.x, piece.y);
			}
		}
	}

	/** Sets the size of squares on the shogi board to the given
	 * dimensions. Updates the positions of the drop tables accordingly.
	 * 
	 * @param width		the new width
	 * @param height	the new height
	 */
	public void setSquareSize (int width, int height)
	{
		setSquareSize (new Dimension (width, height));
	}

	/** Sets the size of squares on the shogi board to the given
	 * dimensions. Updates the positions of the drop tables accordingly.
	 * 
	 * @param sq	the new dimensions
	 */
	public void setSquareSize (Dimension sq)
	{		
		dropOffset1 = new Point (boardOffset.x + 9 * sq.width, boardOffset.y + 5 * sq.height);
		dropOffset2 = new Point (boardOffset.x + 9 * sq.width, boardOffset.y);

		dropTable = new Dimension (sq.width * 3, sq.height * 4);

		square = new Dimension (sq);
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
		dropOffset1 = new Point (bo.x + 9 * square.width, bo.y + 5 * square.height);
		dropOffset2 = new Point (bo.x + 9 * square.width, bo.y);
		boardOffset = new Point (bo.x, bo.y);
	}	


	/** Resets the board to its default configuration.
	 */
	protected void reset ()
	{
		state = new GameState ();
		state.defaultBoardConfigure();
	}
}

package name.paramonov.doomep.shogi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/** The JPanel for the Shogi board. Work in progress.
 * @author                  Jiayin Huang
 * @author                  Dmitry Andreevich Paramonov 
 */
public class BoardPanel extends JPanel 
{	
	/**
	 * I don't know why Eclipse wants this.
	 */
	private static final long serialVersionUID = 1L;

	private static final Color dropTableColor = new Color (192, 192, 192);
	private static final Color boardColor = new Color (196, 144, 0);	
	private static final Color highlightColor = Color.green;

	private static final Point boardOffset = new Point (30, 30);	
	private static final Point dropOffset1 = new Point (390, 30);
	private static final Point dropOffset2 = new Point (390, 230);
	private static final Dimension zone = new Dimension (40, 40);	
	private static final Dimension dropTable = new Dimension (120, 160);	

	/**
	 * The board. 
	 */
	static GameState state = new GameState ();

	//TODO: Annotation.
	//TODO: Clean up code.
	//TODO: Add drop table, player turns, and all of the other functions.

	static boolean isPieceSelected = false;
	static Piece selectedPiece;

	/**
	 * The location of the cursor when the mouse button was pressed. 
	 * 
	 */
	private Point [] click = new Point [4];

	/**
	 * The current location of the cursor.
	 */
	private Point mouse = new Point ();


	/**
	 * Whether the mouse button is currently being pressed or not.
	 */	
	private boolean [] mousePressed = new boolean [4];


	public BoardPanel ()
	{
		reset ();
	}

	@Override	
	public void paintComponent (Graphics g)
	{       
		super.paintComponent(g);

		drawBoard (g);
		drawDropTables (g);	
		drawHighlight (g);
	}

	private void drawBoard (Graphics g)
	{
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{				
				Piece piece = state.getPieceAt(i, j);
				Point point = getLocationOnPanel (i, j);

				drawZone (g, boardColor, Color.black, point);								
				drawPiece (g, piece, point);		
			}        
		}  	
	}

	private void drawHighlight (Graphics g)
	{		
		// Draw hover outline

		if (mouseIsOnBoard ())
		{
			Point boardCoords = getLocationOnBoard (mouse);	
			Point point = getLocationOnPanel (boardCoords);

			if (isPieceSelected)
			{				
				Point start = new Point (selectedPiece.x, selectedPiece.y);
				start = getLocationOnPanel (start);
				drawZone (g, boardColor, Color.black, start);

				drawPossibleMoves (g, selectedPiece);
				drawPiece (g, selectedPiece, mouse.x - zone.width / 2, mouse.y - zone.height / 2);
			}
			else
			{
				if (!(state.getPieceAt(boardCoords.x, boardCoords.y) instanceof EmptyPiece))
				{	
					Piece piece = state.getPieceAt(boardCoords.x, boardCoords.y);

					drawZone (g, highlightColor, Color.black, point);
					drawPiece (g, piece, point);
				}				
			}
		}	
		else if (mouseIsOnDropTable ())
		{
			
		}
	}

	private void drawPossibleMoves (Graphics g, Piece piece)
	{
		boolean [][] moves = piece.generateMoves(state);

		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				if (moves [j][i])
				{
					Point point = getLocationOnPanel (new Point (j, i));
					drawZone (g, highlightColor, Color.black, point.x, point.y);
					drawPiece (g, state.getPieceAt(j, i), point.x, point.y);
				}
			}
		}
	}

	private void drawDropTables (Graphics g)
	{
		g.setColor(dropTableColor);
		g.fillRect (dropOffset1.x, dropOffset1.y, dropTable.width, dropTable.height);
		g.fillRect (dropOffset2.x, dropOffset2.y, dropTable.width, dropTable.height);

		g.setColor(Color.black);
		g.drawRect (dropOffset1.x, dropOffset1.y, dropTable.width, dropTable.height);
		g.drawRect (dropOffset2.x, dropOffset2.y, dropTable.width, dropTable.height);
	}

	private void drawZone (Graphics g, Color back, Color fore, Point p)
	{
		drawZone (g, back, fore, p.x, p.y);
	}

	private void drawZone (Graphics g, Color back, Color fore, int x, int y)
	{
		// Fill
		g.setColor (back);
		g.fillRect (x, y, zone.width, zone.height);
		g.setColor (Color.black);

		// Outline
		g.setColor (fore);
		g.drawRect (x, y, zone.width, zone.height);
		g.setColor (Color.black);
	}	

	private void drawPiece (Graphics g, Piece p, Point point)
	{
		drawPiece (g, p, point.x, point.y);
	}

	private void drawPiece (Graphics g, Piece p, int x, int y)
	{
		g.drawString(p.getDoubleCharRepresentation(), x + 20, y + 20);
	}


	private Point getLocationOnBoard (Point mouse)
	{
		Point coordinates = new Point ();

		coordinates.x = (int) Math.floor (((mouse.getX () - boardOffset.x) / zone.width));
		coordinates.y = (int) ((8 - Math.floor ((mouse.getY () - boardOffset.y) / zone.height)));

		return coordinates;
	}

	private Point getLocationOnPanel (int x, int y)
	{
		return getLocationOnPanel (new Point (x, y));
	}

	private Point getLocationOnPanel (Point board)
	{
		Point coordinates = new Point ();

		coordinates.x = board.x * zone.width + boardOffset.x;
		coordinates.y = (8 - board.y) * zone.height + boardOffset.y ;

		return coordinates;
	}

	private boolean mouseIsOnBoard ()
	{
		boolean mouseIsOnBoard = false;

		Point point = getLocationOnBoard (mouse);

		if (point.x >= 0 && point.x <= 8)		
			if (point.y >= 0 && point.y <= 8)			
				mouseIsOnBoard = true;					

		return mouseIsOnBoard;
	}

	private boolean mouseIsOnDropTable ()
	{
		return mouseIsOnDropTable (-1) || mouseIsOnDropTable (1);
	}

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


	/**
	 * Updates the cursor's current location.
	 * @param location - The position (x,y) of the cursor.
	 */
	protected void moveMouse (Point location)
	{		
		mouse = location; 		
	}

	/**
	 * Updates the mousePressed boolean array.
	 * 
	 * @param button - The button that was pressed.
	 * @param location - The position (x,y) of the cursor when the button was pressed.
	 */
	protected void pressMouse (int button, Point location)
	{
		click [button] = location;
		mousePressed [button] = true;	

		if (button == MouseEvent.BUTTON1)
		{			
			selectPieceAt (location);			
		}
	}

	/**
	 * Updates the mousePressed boolean array.
	 * 
	 * @param button - The button that was released.
	 */
	protected void releaseMouse (int button)
	{
		mousePressed [button] = false;

		if (button == MouseEvent.BUTTON1 && isPieceSelected)
		{		
			releasePiece (selectedPiece);					
		}
	}

	private void selectPieceAt (Point location)
	{				
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
	}

	private void releasePiece (Piece piece)
	{
		isPieceSelected = false;
		Point newPoint = getLocationOnBoard (mouse);

		if (!newPoint.equals(new Point (selectedPiece.x, selectedPiece.y)))
		{
			if (selectedPiece.isValidMove (state, newPoint.x, newPoint.y))
			{
				move (selectedPiece, newPoint);
				promote (selectedPiece);
			}
		}
	}

	private void move (Piece piece, Point newPoint)
	{
		selectedPiece.move(state, newPoint.x, newPoint.y);
	}

	private void promote (Piece piece)
	{
		if (selectedPiece.isPromotable())
		{
			int result = JOptionPane.showConfirmDialog(this, "Promote piece?", "", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
				state.promotePieceAt (selectedPiece.x, selectedPiece.y);					
		}
	}

	protected void reset ()
	{
		state = new GameState ();
		state.defaultBoardConfigure();
	}

}

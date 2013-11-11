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

	/**
	 * The board. 
	 */
	static GameState state = new GameState ();

	//TODO: Annotation.
	//TODO: Clean up code.
	//TODO: Add drop table, player turns, and all of the other functions.

	static final Color boardColor = new Color (196, 144, 0);	
	static final Color highlightColor = Color.green;

	static int xOffset = 30;	
	static int yOffset = 30;	
	static Dimension zone = new Dimension (40, 40);	

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

		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
				Point point = getLocationOnPanel (new Point (i, j));
				int x = point.x;
				int y = point.y;

				drawZone (g, boardColor, Color.black, x, y);

				if (!(isPieceSelected && state.getPieceAt (i,j).equals(selectedPiece)))				
					drawPiece (g, state.getPieceAt(i, j), x, y);				

			}        
		}  		

		// Draw hover outline

		if (mouseIsOnBoard ())
		{
			Point point = getLocationOnBoard (mouse);

			if (!(state.getPieceAt(point.x, point.y) instanceof EmptyPiece))
			{			
				point = getLocationOnPanel (point);
				outlineZone (g, highlightColor, point.x, point.y);
			}
		}

		// Draw selected piece

		if (isPieceSelected)
			drawPiece (g, selectedPiece, mouse.x - zone.width / 2, mouse.y - zone.height / 2);		

	}

	private void drawZone (Graphics g, Color back, Color fore, int x, int y)
	{
		fillZone (g, back, x, y);
		outlineZone (g, fore, x, y);		
	}

	private void fillZone (Graphics g, Color back, int x, int y)
	{
		g.setColor (back);
		g.fillRect (x, y, zone.width, zone.height);
		g.setColor (Color.black);
	}

	private void outlineZone (Graphics g, Color fore, int x, int y)
	{
		g.setColor (fore);
		g.drawRect (x, y, zone.width, zone.height);
		g.setColor (Color.black);
	}

	private void drawPiece (Graphics g, Piece p, int x, int y)
	{
		g.drawString(p.getDoubleCharRepresentation(), x + 10, y + 30);
	}

	private Point getLocationOnBoard (Point mouse)
	{
		Point coordinates = new Point ();

		coordinates.x = (int) Math.floor (((mouse.getX () - xOffset) / zone.width));
		coordinates.y = (int) ((8 - Math.floor ((mouse.getY () - yOffset) / zone.height)));

		return coordinates;
	}

	private Point getLocationOnPanel (Point board)
	{
		Point coordinates = new Point ();

		coordinates.x = board.x * zone.width + xOffset;
		coordinates.y = (8 - board.y) * zone.height + yOffset;

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
				state.promotedPieceAt (selectedPiece.x, selectedPiece.y);					
		}
	}

	protected void reset ()
	{
		state = new GameState ();
		state.defaultBoardConfigure();
	}

}

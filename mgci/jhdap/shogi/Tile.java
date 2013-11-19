package mgci.jhdap.shogi;

import java.awt.Point;

/** The Class representing the position of a
 * tile or shogi board square.
 * 
 * @author 			Jiayin Huang
 * @author			Dmitry Andreevich Paramonov 
 */
public class Tile extends Point
{	
	private static final long serialVersionUID = 1L;
	
	/** The notation used by the GameState class, where 00
	 * is at the bottom left and 88 is at the top right. 
	 */
	public static final int XY_NOTATION = 0;
	/** The typical shogi notation, where 9i is at the bottom left
	 * and 1a is at the top right. 
	 */
	public static final int SHOGI_NOTATION = 1;

	/** The file or column of the square.	 
	 */
	public int f;
	/** The rank or row of the square. 
	 */
	public char r;
	
	/** Constructs a new Tile of the given location. Accepts
	 * GameState xy notation arguments. Automatically
	 * sets the equivalent shogi standard notation coordinates. 
	 * 
	 * @param p		the GameState coordinates of the tile 
	 */	
	public Tile (Point p)
	{
		this (p.x, p.y);
	}
	
	/** Constructs a new Tile of the given location. Accepts 
	 * either GameState xy notation or shogi standard notation 
	 * arguments. Automatically sets the equivalent coordinates
	 * of the other notation format.
	 * 
	 * @param sq	the coordinates of the tile
	 */
	public Tile (String sq)
	{
		this (sq.charAt(0), sq.charAt(1));
	}
	/** Constructs a new Tile of the given location. Accepts 
	 * either GameState xy notation or shogi standard notation 
	 * arguments. Automatically sets the equivalent coordinates
	 * of the other notation format.
	 * 
	 * @param xOrFile	the x coordinate of the tile
	 * @param yOrRank	the y coordinate of the tile
	 */
	public Tile (char xOrFile, char yOrRank)
	{							
		if (Character.isAlphabetic(yOrRank))
		{
			f = Character.getNumericValue(xOrFile);
			r = yOrRank;
			determineOther (f, r);
		}
		else
		{
			x = Character.getNumericValue(xOrFile);
			y = Character.getNumericValue (yOrRank);
			determineOther (x,y);
		}				
	}		
	/** Constructs a new Tile of the given location.
	 * Accepts shogi standard notation arguments.
	 * Automatically sets the equivalent GameState xy
	 * notation coordinates.
	 * 
	 * @param file		the column of the tile
	 * @param rank		the row of the tile
	 */
	public Tile (int file, char rank)
	{
		this.f = file;
		this.r = rank;
		determineOther (file, rank);
	}
	/** Constructs a new Tile of the given location.
	 * Accepts GameState xy notation arguments.
	 * Automatically sets the equivalent shogi standard
	 * notation coordinates.
	 * 
	 * @param x		the x coordinate of the tile
	 * @param y		the y coordinate of the tile
	 */
	public Tile (int x, int y)
	{
		this.x = x;
		this.y = y;
		determineOther (x, y);
	}	
	
	/**	Accepts shogi standard notation coordinates.
	 * Calculates and sets the equivalent GameState xy
	 * notation coordinates.
	 * 
	 * @param file		the column of the tile
	 * @param rank		the row of the tile
	 */
	public void determineOther (int file, char rank)
	{
		x = 9 - file;
		y = 'i' - rank;
	}
	
	/**	Accepts GameState xy notation coordinates.
	 * Calculates and sets the equivalent shogi
	 * standard notation coordinates.
	 * 
	 * @param x		the column of the tile
	 * @param y		the row of the tile
	 */
	public void determineOther (int x, int y)
	{
		f = 9 - x;
		r = (char) ('i' - y);
	}	
	
	/** Calculates whether this tile is on
	 * the 9x9 shogi board. 
	 * 
	 * @return true if on board ; false otherwise
	 */
	public boolean isValid ()
	{
		return (0 <= x && x <= 8 && 0 <= y && y <= 8);
	}
	
	/** Returns the column of the tile.
	 * 
	 * @return	the file value
	 */
	public int getFile ()
	{
		return f;
	}
	
	/** Returns the row of the tile.
	 * 
	 * @return	the rank value
	 */
	public char getRank ()
	{
		return r;
	}
	
	/** Returns a 2 character String representation
	 * of the coordinates of this tile. 
	 * 
	 * @return		the coordinates as a String
	 */
	public String getCode ()
	{
		return "" + x + y;
	}
	
	/** Returns the 2 character String representation
	 * of the coordinates of this tile with the given
	 * notation format. 0 indicates GameState xy notation;
	 * 1 indicates shogi standard notation.
	 * 
	 * @param format	the format of the coordinates
	 * @return		the coordinates as a String
	 */
	public String getCode (int format)
	{
		if (format == 0)
			return "" + x + y;				
		else if (format == 1)
			return "" + f + r;
		else
			return null;				
	}
	
	/** Sets the location of this tile to the
	 * given GameState XY coordinates. Updates
	 * the file,rank coordinates accordingly.
	 * 
	 *  @param x	the new x coordinate
	 *  @param y	the new y coordinate
	 */
	@Override
	public void move (int x, int y)
	{
		super.move (x,y);
		determineOther (x, y);
	}
	
	/** Sets the location of this tile to the
	 * given GameState XY coordinates. Updates
	 * the file,rank coordinates accordingly.
	 * 
	 *  @param x	the new x coordinate
	 *  @param y	the new y coordinate
	 */
	@Override
	public void setLocation (int x, int y)
	{
		super.setLocation(x,y);
		determineOther (x, y);
	}
	/** Sets the location of this tile to the
	 * given GameState XY coordinates. Updates
	 * the file,rank coordinates accordingly.
	 * 
	 *  @param p the new x,y coordinates
	 */
	@Override
	public void setLocation (Point p)
	{
		super.setLocation (p);
		determineOther (x, y);
	}
	
	/** Translates this point, at location (x,y), 
	 * by dx along the x axis and dy along the y axis 
	 * so that it now represents the point (x+dx,y+dy).
	 * Updates the file,rank coordinates accordingly.
	 * 
	 *  @param dx	the distance to move this point along the X axis
	 *  @param dy   the distance to move this point along the Y axis
	 */
	@Override
	public void translate (int dx, int dy)
	{
		super.translate (dx, dy);
		determineOther (x, y);
	}
}

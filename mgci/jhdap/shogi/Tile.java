package mgci.jhdap.shogi;

import java.awt.Point;

//TODO: Finish annotation

/** This class represents the position of a
 * tile or shogi board square.
 * 
 * @author 			Jiayin
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
	
	/** Constructs a new Tile of the given coordinates. Automatically
	 * calculates the equivalent shogi standard coordinates. 
	 * 
	 * @param p		the GameState coordinates of the tile 
	 */	
	public Tile (Point p)
	{
		this (p.x, p.y);
	}
	
	/**
	 * 
	 * @param square
	 */
	public Tile (String square)
	{
		this (square.charAt(0), square.charAt(1));
	}
	/**
	 * 
	 * @param xOrFile
	 * @param yOrRank
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
	/**
	 * 
	 * @param file
	 * @param rank
	 */
	public Tile (int file, char rank)
	{
		this.f = file;
		this.r = rank;
		determineOther (file, rank);
	}
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Tile (int x, int y)
	{
		this.x = x;
		this.y = y;
		determineOther (x, y);
	}	
	
	/**
	 * 
	 * @param file
	 * @param rank
	 */
	private void determineOther (int file, char rank)
	{
		x = 9 - file;
		y = 'i' - rank;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	private void determineOther (int x, int y)
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
	
	/**
	 * 
	 * @return
	 */
	public int getFile ()
	{
		return f;
	}
	
	/**
	 * 
	 * @return
	 */
	public char getRank ()
	{
		return r;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCode ()
	{
		return "" + x + y;
	}
	
	/**
	 * 
	 * @param format
	 * @return
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
	
	/**
	 * 
	 */
	@Override
	public void move (int x, int y)
	{
		super.move (x,y);
		determineOther (x, y);
	}
	
	/**
	 * 
	 */
	@Override
	public void setLocation (int x, int y)
	{
		super.setLocation(x,y);
		determineOther (x, y);
	}
	/**
	 * 
	 */
	@Override
	public void setLocation (Point p)
	{
		super.setLocation (p);
		determineOther (x, y);
	}
	
	/**
	 * 
	 */
	@Override
	public void translate (int dx, int dy)
	{
		super.translate (dx, dy);
		determineOther (x, y);
	}
}

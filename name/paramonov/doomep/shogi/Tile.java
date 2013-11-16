package name.paramonov.doomep.shogi;

import java.awt.Point;

// TODO: Annotate

/**
 * 
 * @author Jiayin
 */
public class Tile extends Point
{	
	private static final long serialVersionUID = 1L;
	
	public static final int XY_NOTATION = 0;
	public static final int SHOGI_NOTATION = 1;

	/** The file or column of the square.	 
	 */
	public int f;
	/** The rank or row of the square. 
	 */
	public char r;	

	public int inversion = 1;
	
	public Tile (Point p)
	{
		this (p.x, p.y);
	}
	
	public Tile (String square)
	{
		this (square.charAt(0), square.charAt(1));
	}
	
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

	public Tile (int file, char rank)
	{
		this.f = file;
		this.r = rank;
		determineOther (file, rank);
	}

	public Tile (int x, int y)
	{
		this.x = x;
		this.y = y;
		determineOther (x, y);
	}	
	

	private void determineOther (int file, char rank)
	{
		x = 9 - file;
		y = 'i' - rank;
	}

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

	public int getFile ()
	{
		return f;
	}

	public char getRank ()
	{
		return r;
	}
	
	public String getCode ()
	{
		return "" + x + y;
	}
	
	public String getCode (int format)
	{
		if (format == 0)
			return "" + x + y;				
		else if (format == 1)
			return "" + f + r;
		else
			return null;				
	}
	
	@Override
	public void move (int x, int y)
	{
		super.move (x,y);
		determineOther (x, y);
	}
	
	@Override
	public void setLocation (int x, int y)
	{
		super.setLocation(x,y);
		determineOther (x, y);
	}
	
	@Override
	public void setLocation (Point p)
	{
		super.setLocation (p);
		determineOther (x, y);
	}
	
	@Override
	public void translate (int dx, int dy)
	{
		super.translate (dx, dy);
		determineOther (x, y);
	}
}

package name.paramonov.doomep.shogi;

import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;

/** The JPanel for the Shogi board. Work in progress.
 * @author                  Jiayin Huang
 * @author                  Dmitry Andreevich Paramonov 
 */
public class BoardPanel extends JPanel 
{	
	/**
	 * The current location of the cursor.
	 */
	private Point mouse = new Point ();
	
	/**
	 * The location of the cursor when the mouse button was pressed. 
	 * 
     */
	private Point [] click = new Point [4];
	
	/**
	 * Whether the mouse button is currently being pressed or not.
	 */	
	private boolean [] mousePressed = new boolean [4];
				
	@Override	
	public void paintComponent (Graphics g)
    {       
        super.paintComponent(g);      
        
        g.drawString ("Current mouse location: (" + mouse.x + "," + mouse.y + ")", 50, 50); 
        
        for (int i = 0; i < mousePressed.length; i++)
        {                
        	if (mousePressed [i])        
        		g.drawString ("Mouse clicked at: (" + click[i].x + "," + click[i].y + ")", 50, 50 + i * 20);        
        }
    }
	
	/**
	 * Updates the cursor's current location.
	 * @param location - The position (x,y) of the cursor.
	 */
	public void moveMouse (Point location)
	{		
		mouse = location; 	
	}
	
	/**
	 * Updates the mousePressed boolean array.
	 * 
	 * @param button - The button that was pressed.
	 * @param location - The position (x,y) of the cursor when the button was pressed.
	 */
	public void pressMouse (int button, Point location)
	{
		click [button] = location;
		mousePressed [button] = true;		
	}
	
	/**
	 * Updates the mousePressed boolean array.
	 * 
	 * @param button - The button that was released.
	 */
	public void releaseMouse (int button)
	{
		mousePressed [button] = false;
	}

}

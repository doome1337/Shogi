package name.paramonov.doomep.shogi;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Color;
import java.io.*;
import javax.imageio.*;

public class BoardPanel extends JPanel 
{
	public static Color backgroundColor;
	
	public BoardPanel ()
	{
		setBackground (backgroundColor);
	}
	
	public void paintComponent (Graphics g)
    {
        super.paintComponent (g);
        
        g.setColor(Color.black);
        g.drawRect (10, 10, 270, 270);
    }
	

}

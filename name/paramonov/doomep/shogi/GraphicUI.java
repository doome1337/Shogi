package name.paramonov.doomep.shogi;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

/** GUI for shogi game. Work in progress.
 * @author                  Dmitry Andreevich Paramonov
 * @author                  Jiayin Huang
 */
public class GraphicUI
{	
	protected static JFrame window;
	protected static BoardPanel board = new BoardPanel ();
	public int width, height;
	
	public static void main(String[] args) 
	{
		GraphicUI game = new GraphicUI (640, 480);
	}
	
	public GraphicUI (int width, int height)
	{
		window = new JFrame ("GUI Test");
		
		window.setJMenuBar (createMenuBar());
		window.setContentPane (createContent ());
		
		window.setSize(width, height);
		window.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	private static JMenuBar createMenuBar ()
    {		
        JMenuBar menuBar = new JMenuBar ();
        JMenu file;
        JMenuItem button;
        
        // "File" Menu        
        file = new JMenu ("File");
        
        button = new JMenuItem ("Quit");
        button.addActionListener (new QuitListener ());
        file.add(button);
        
        // Add All Menus        
        menuBar.add (file);
        
        // Return        
        return menuBar;
    }
	
	private static JPanel createContent ()
	{
		JPanel content = new JPanel (new BorderLayout ());
		return content;
	}
	
	static class QuitListener implements ActionListener
	{
		public void actionPerformed (ActionEvent e)
		{
			window.setVisible (false);
			window.dispose ();
		}
	}

}

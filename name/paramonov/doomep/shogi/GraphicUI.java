package name.paramonov.doomep.shogi;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.*;

/** GUI for shogi game. Work in progress.
 * @author                  Jiayin Huang
 * @author                  Dmitry Andreevich Paramonov 
 */
public class GraphicUI extends JFrame
{		
	/**
	 * I don't know why Eclipse wants this variable.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The component representing the shogi board.
	 */
	protected BoardPanel board = new BoardPanel ();


	public static void main(String[] args) 
	{
		new GraphicUI (640, 480);
	}

	/**
	 * Constructs a new GraphicUI.
	 * 
	 * @param width - The width of the JFrame window.
	 * @param height - The height of the JFrame window.
	 */
	public GraphicUI (int width, int height)
	{
		super ("GUI Test");

		setJMenuBar (createMenuBar());
		setContentPane (createContent ());

		setSize(width, height);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Creates the menu bar for the GUI.
	 * 
	 * @return The JMenuBar with everything added to it.
	 */
	private JMenuBar createMenuBar ()
	{		
		JMenuBar menuBar = new JMenuBar ();
		JMenu file;
		JMenuItem button;

		// "File" Menu        
		file = new JMenu ("File");

		button = new JMenuItem ("New Game");
		button.addActionListener(this.new NewGameListener ());
		file.add(button);

		button = new JMenuItem ("Quit");
		button.addActionListener (this.new QuitListener ());
		file.add(button);

		// Add All Menus        
		menuBar.add (file);

		// Return        
		return menuBar;
	}

	/**
	 * Creates the content to go inside the JFrame.
	 * 
	 * @return The JPanel with everything added to it.
	 */
	private JPanel createContent ()
	{
		JPanel content = new JPanel (new BorderLayout ());

		content.add(board);
		board.addMouseListener(this.new CursorAdapter ());
		board.addMouseMotionListener(this.new CursorAdapter ());

		return content;
	}

	private class QuitListener implements ActionListener
	{
		@Override
		public void actionPerformed (ActionEvent e)
		{
			setVisible (false);
			dispose ();
		}
	}

	private class NewGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			board.reset();			
		}	
	}

	private class CursorAdapter extends MouseAdapter 
	{
		@Override
		public void mouseMoved (MouseEvent e)
		{
			board.moveMouse (e.getPoint ());
			GraphicUI.this.repaint ();
		}

		@Override
		public void mousePressed (MouseEvent e)
		{
			board.pressMouse (e.getButton(), e.getPoint ());
			GraphicUI.this.repaint ();
		}

		@Override
		public void mouseReleased (MouseEvent e)
		{
			board.releaseMouse (e.getButton());
			GraphicUI.this.repaint ();
		}

		@Override
		public void mouseDragged (MouseEvent e)
		{
			board.moveMouse (e.getPoint ());
			GraphicUI.this.repaint ();
		}			
	}
}

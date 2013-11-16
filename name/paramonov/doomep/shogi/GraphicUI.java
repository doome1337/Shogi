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
	/** Eclipse wants this variable for some reason. 
	 */
	private static final long serialVersionUID = 1L;

	/** The component representing the shogi board.
	 */
	protected BoardPanel board = new BoardPanel ();


	public static void main(String[] args) 
	{
		new GraphicUI (640, 480);		
	}
	
	protected boolean consoleIsOpen = false;

	/** Constructs a new GraphicUI.
	 * 
	 * @param width 	the width of the JFrame window
	 * @param height	the height of the JFrame window
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

	/** Creates the menu bar for the GUI.
	 * 
	 * @return the JMenuBar with everything added to it
	 */
	private JMenuBar createMenuBar ()
	{		
		JMenuBar menuBar = new JMenuBar ();
		JMenu file;
		JMenuItem button;

		// "File" Menu        
		file = new JMenu ("File");

		button = new JMenuItem ("New Game");
		button.addActionListener(this.new MenuListener ());
		file.add(button);		

		button = new JMenuItem ("Debug Console");
		button.addActionListener(this.new MenuListener ());
		file.add (button);		

		button = new JMenuItem ("Exit");
		button.addActionListener (this.new MenuListener ());
		file.add(button);		

		// Add All Menus        
		menuBar.add (file);

		// Return        
		return menuBar;
	}

	/** Creates the content to go inside the JFrame. 
	 * 
	 * @return the JPanel with everything added to it
	 */
	private JPanel createContent ()
	{
		JPanel content = new JPanel (new BorderLayout ());

		content.add(board, "Center");
		board.addMouseListener(this.new CursorAdapter ());
		board.addMouseMotionListener(this.new CursorAdapter ());		

		return content;
	}

	/** Listener for menu buttons	 
	 */
	private class MenuListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object parent = e.getSource();

			if (parent instanceof JMenuItem)
			{
				JMenuItem button = (JMenuItem) parent;
				String name = button.getText ();

				if (name.equals("New Game"))
				{
					board.reset();					
				}
				else if (name.equals ("Debug Console"))
				{
					if (!consoleIsOpen)
					{
						board.c = new ShogiConsole (GraphicUI.this, "Console");
						board.log = true;
						consoleIsOpen = true;					
					}
				}
				else if (name.equals("Exit"))
				{
					setVisible (false);
					if (consoleIsOpen)
						board.c.dispose ();
					dispose ();
				}			
			}			
		}		
	}


	/** Mouse adapter for shogi board.
	 */
	private class CursorAdapter extends MouseAdapter 
	{
		@Override
		public void mouseMoved (MouseEvent e)
		{
			board.moveMouse (e.getPoint ());			
		}

		@Override
		public void mousePressed (MouseEvent e)
		{
			board.pressMouse (e.getButton(), e.getPoint ());			
		}

		@Override
		public void mouseReleased (MouseEvent e)
		{
			board.releaseMouse (e.getButton());			
		}

		@Override
		public void mouseDragged (MouseEvent e)
		{
			board.moveMouse (e.getPoint ());			
		}			
	}
}

package mgci.jhdap.shogi;

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
	protected BoardPanel board;
	
	protected StatsPanel stats;


	public static void main(String[] args) 
	{
		new GraphicUI (800, 535);		
	}
	
	protected boolean consoleIsOpen = false;

	/** Constructs a new GraphicUI.
	 * 
	 * @param width 	the width of the JFrame window
	 * @param height	the height of the JFrame window
	 */
	public GraphicUI (int width, int height)
	{
		super ("Shogi");	
		
		board = new BoardPanel ();		
		stats = new StatsPanel (this);				
			
		setContentPane (createContent ());	
		setJMenuBar (createMenuBar());	
		
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);	
		setSize(width, height);	
		setVisible(true);			
	}

	/** Creates the menu bar for the GUI.
	 * 
	 * @return the JMenuBar with everything added to it
	 */
	private JMenuBar createMenuBar ()
	{		
		JMenuBar menuBar = new JMenuBar ();
		JMenu file, help;
		JMenuItem button;

		// "File" Menu        
		file = new JMenu ("File");
		file.setMnemonic('f');

		button = new JMenuItem ("New Game"); // new game button
		button.setMnemonic('n');
		button.setAccelerator(KeyStroke.getKeyStroke (
				KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		button.addActionListener(this.new MenuListener ());
		file.add(button);	
		
		button = new JMenuItem ("Developer Console"); // developer console button
		button.setMnemonic('d');
		button.setAccelerator(KeyStroke.getKeyStroke ('`'));
		button.addActionListener(this.new MenuListener ());
		file.add (button);		

		button = new JMenuItem ("Exit"); // exit button
		button.setMnemonic('x');
		button.setAccelerator(KeyStroke.getKeyStroke (
				KeyEvent.VK_X, KeyEvent.CTRL_MASK));
		button.addActionListener (this.new MenuListener ());
		file.add(button);
		
		// "Help" Menu
		help = new JMenu ("Help");
		help.setMnemonic('h');
		
		button = new JMenuItem ("Ha ha, no help yet.");
		help.add (button);
				
		button = new JMenuItem ("No User's Guide at all.");
		help.add (button);
						
		button = new JMenuItem ("About"); // about button
		button.setMnemonic('a');
		button.addActionListener (this.new MenuListener ());
		help.add(button);
		

		// Add All Menus        
		menuBar.add (file);
		menuBar.add (help);

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
		
		content.add (stats, "East");
		
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
				else if (name.equals ("Developer Console"))
				{
					if (!consoleIsOpen)
					{
						GraphicUI.this.setTitle("Opening Console...");
						board.c = new ShogiConsole (GraphicUI.this, "Console", 640, 535);
						board.log = true;
						consoleIsOpen = true;
						GraphicUI.this.setTitle("Shogi");
					}
					else
						board.c._input.requestFocus();
				}
				else if (name.equals("Exit"))
				{
					setVisible (false);
					if (consoleIsOpen)
						board.c.dispose ();
					dispose ();
				}	
				else if (name.equals("About"))
				{
					if (consoleIsOpen)
						board.c.println ("A JDialog box would open at this point.");				
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

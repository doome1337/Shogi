package mgci.jhdap.shogi;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;


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
	
	/** The component representing the statistics panel. 
	 */
	protected StatsPanel stats;
		
	protected boolean consoleIsOpen = false;	
	

	/** Constructs a new GraphicUI.
	 * 
	 * @param width 	the width of the JFrame window
	 * @param height	the height of the JFrame window
	 */
	public GraphicUI (int width, int height)
	{
		super ("Shogi");	
		
		board = new BoardPanel (this);		
		stats = new StatsPanel (this);	
		board.s = stats;
			
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
		JMenu game, help;
		JMenuItem button;

		// "File" Menu        
		game = new JMenu ("Game");
		game.setMnemonic('g');

		button = new JMenuItem ("New Game"); // new game button
		button.setMnemonic('n');
		button.setAccelerator(KeyStroke.getKeyStroke (
				KeyEvent.VK_N, KeyEvent.CTRL_MASK));
		button.addActionListener(this.new MenuListener ());
		game.add(button);	
		
		button = new JMenuItem ("Developer Console"); // developer console button
		button.setMnemonic('d');
		button.setAccelerator(KeyStroke.getKeyStroke (
				KeyEvent.VK_D, KeyEvent.CTRL_MASK));
		button.addActionListener(this.new MenuListener ());
		game.add (button);		

		button = new JMenuItem ("Exit"); // exit button
		button.setMnemonic('x');
		button.setAccelerator(KeyStroke.getKeyStroke (
				KeyEvent.VK_X, KeyEvent.CTRL_MASK));
		button.addActionListener (this.new MenuListener ());
		game.add(button);
		
		// "Help" Menu
		help = new JMenu ("Help");
		help.setMnemonic('h');	
				
		button = new JMenuItem ("User's Guide");
		button.addActionListener (this.new MenuListener ());
		help.add (button);
		
		button = new JMenuItem ("Javadoc");
		button.addActionListener (this.new MenuListener ());
		help.add (button);
		
		help.addSeparator ();
						
		button = new JMenuItem ("About"); // about button
		button.setMnemonic('a');
		button.addActionListener (this.new MenuListener ());
		help.add(button);
		

		// Add All Menus        
		menuBar.add (game);
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
	
	public void putWinner (int winner)
	{
		stats.stopTimer();		
		String name = winner == 1 ? stats.player1.name : stats.player2.name;
		String message;
		if (board.winner == 0)
		{
			message = name + " wins! Congratulations! +1 rep!";
			board.winner = winner;
			stats.historyText.append(" WINNER " + name);
		}
		else
			message = "Hey, the game's already over.";
		
		JOptionPane.showMessageDialog(GraphicUI.this, message, "Winner!", JOptionPane.INFORMATION_MESSAGE);		
	}
	
	public void reset ()
	{
		board.reset ();
		stats.reset ();
	}
	
	public void close ()
	{
		stats.stopTimer();
		if (consoleIsOpen)
			board.c.dispose ();
		dispose ();
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
					GraphicUI.this.reset ();					
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
					GraphicUI.this.close ();
				}
				else if (name.equals ("User's Guide"))
				{
					String message = "You can find the user's guide at:\n";
					message += "                  Shogi/UsersGuideMark2.odt\n";
					message += "                  Shogi/UsersGuideMark2.pdf\n";
					message += "It doesn't matter which file you choose. They're the same thing.\n";
					JOptionPane.showMessageDialog(GraphicUI.this, message, 
							"Ceci n'est pas un User's Guide", JOptionPane.INFORMATION_MESSAGE);					
				}
				else if (name.equals("Javadoc"))
				{
					String message = "You can find the documentation for this program at:\n";
					message += "                  Shogi/doc/index.html";
					JOptionPane.showMessageDialog(GraphicUI.this, message, 
							"Ceci n'est pas un Javadoc", JOptionPane.INFORMATION_MESSAGE);	
				}
				else if (name.equals("About"))
				{
					String message = "Shogi version 2013.11.18\n";					
					message += "Program by: Dmitry Andreevich Paramonov\n";
					message += "                        Jiayin Huang\n";
					JOptionPane.showMessageDialog(GraphicUI.this, message, "About", JOptionPane.PLAIN_MESSAGE);			
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

package mgci.jhdap.shogi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** The JPanel containing all of the statistics
 * for the shogi GUI, including the move history,
 * and the PlayerPanes with the timers and player names.
 * 
 * @author      Jiayin Huang
 * @author      Dmitry Andreevich Paramonov
 */
public class StatsPanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;

	/** The player pane for player1 (the bottom player). 
	 */
	protected PlayerPane player1;
	/** The player pane for player2 (the top player) 
	 */
	protected PlayerPane player2;

	/** The JTextArea containing the move history. 
	 */
	protected JTextArea historyText;
	/** The JScrollPane containing the move history text pane. 
	 */
	protected JScrollPane history;
	
	/** Whose turn it is: 1 = player 1 ; -1 = player 2.  
	 */
	private int turn = 1;

	/** The thread that runs the timers. 
	 */
	private Thread thread;
	
	/** The pause button. It has to be out here
	 * as a field variable because its text changes. 
	 */
	private JButton pauseButton;

	/** If false, then the current player's timer is paused.
	 */
	public boolean timing = false;
	
	/** The parent GraphicUI object. 
	 */
	protected GraphicUI gui;

	/** Creates a new StatsPanel.
	 * 
	 * @param parent	the parent GraphicUI object
	 */
	public StatsPanel (GraphicUI parent) 
	{			
		gui = parent;
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout (new GridBagLayout ());			
		GridBagConstraints c = new GridBagConstraints();	
		setPreferredSize (new Dimension (160, 160));

		initComponents ();

		c.insets = new Insets(2, 2, 1, 0); // insets for all components	
		c.gridwidth = 2;

		// Adding player 1		

		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;		
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;		

		add (player1.txt, c);		

		// Adding player 2

		c.gridy = 1;		
		add (player2.txt, c);		

		// Add history pane

		c.gridy = 2;	
		c.ipadx = 100;		
		c.fill = GridBagConstraints.BOTH;		
		c.weighty = 0.5;		

		add (history, c);	 

		// Add buttons
		
		JButton button;

		c.ipadx = 0;
		c.ipady = 0;	
		c.weightx = 0.5;
		c.weighty = 0;			
		c.fill = GridBagConstraints.HORIZONTAL;	
		
		button = new JButton ("New Game");
		button.addActionListener(new MyButtonListener ());
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 3;			
		add (button, c);
		
		pauseButton = new JButton ("Pause");
		pauseButton.addActionListener(new MyButtonListener ());
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 4;			
		add (pauseButton, c);	

		button = new JButton ("Resign");
		button.addActionListener(new MyButtonListener ());		
		c.gridx = 1;
		c.gridy = 4;		
		add (button, c);
		
		button = new JButton ("Exit");
		button.addActionListener(new MyButtonListener ());
		c.weightx = 0;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 5;		
		add (button, c);

		startTimer ();
	}	

	/** Initializes the move history text area,
	 * and the two player panes.
	 */
	private void initComponents ()
	{
		historyText = new JTextArea ();
		historyText.setEditable(false);			
		history = new JScrollPane (historyText);
		
		player1 = new PlayerPane ("Player 1");
		player2 = new PlayerPane ("Player 2");

		player1.setPlaying ();
	}
	
	/** Switches the current player. 
	 */
	public void switchTurn ()
	{
		PlayerPane playing;
		PlayerPane waiting;
		if (turn == 1)
		{
			playing = player1;
			waiting = player2;		
		}
		else
		{
			playing = player2;
			waiting = player1;	
		}
		playing.setNotPlaying ();
		waiting.setPlaying ();
		turn *= -1;
	}

	/** Adds the given move to the move history text area.
	 * 
	 * @param move		the Move to be added.
	 */
	public void addMove (Move move)
	{
		historyText.append (move + "\n");
	}

	/** Starts the timer for the current player. 
	 */
	public void startTimer ()
	{
		if (!timing)
		{			
			timing = true;
			pauseButton.setText ("Pause");
			PlayerPane player = turn == 1 ? player1 : player2;
			player.setPlaying ();
			thread = new Thread (this);	
			thread.start ();
		}
	}	

	/** Pauses the timer for the current player.
	 */
	public void stopTimer ()
	{
		timing = false;
		pauseButton.setText ("Resume");
		PlayerPane player = turn == 1 ? player1 : player2;
		player.setNotPlaying ();
	}
	
	/** Resets the timers and the move history. 
	 */
	public void reset ()
	{
		stopTimer ();
		player1.reset();
		player2.reset();
		historyText.setText ("");
		player1.setPlaying ();
		turn = 1;
		startTimer ();
	}

	/** Updates the timer of the current player. 
	 */
	@Override
	public void run() 
	{			
		while (timing)
		{			
			PlayerPane playing = turn == 1 ? player1 : player2;
			playing.updateTime ();		
			try
			{
				Thread.sleep (100);
			}
			catch (Exception e)
			{					
			}						
		}
	}			

	private class MyButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Object parent = e.getSource();

			if (parent instanceof JButton)
			{
				JButton button = (JButton) parent; 
				String name = button.getText();
				
				if (name.equals ("New Game"))
					gui.reset();
				else if (name.equals("Pause"))								
					StatsPanel.this.stopTimer ();				
				else if (name.equals ("Resume"))
				{
					if (gui.board.winner == 0)
						StatsPanel.this.startTimer ();
					else
						gui.putWinner(-turn);
				}
				else if (name.equals("Resign"))
					gui.putWinner (-turn);
				else if (name.equals("Exit"))				
					gui.close ();							
			}
		}
	}
	
}

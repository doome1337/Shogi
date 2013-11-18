package mgci.jhdap.shogi;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StatsPanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;

	PlayerPane player1;
	PlayerPane player2;

	protected JTextArea historyText;
	protected JScrollPane history;

	private int turn = 1;

	private Thread thread;

	public boolean timing = false;

	public StatsPanel (GraphicUI parent) 
	{	
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout (new GridBagLayout ());			
		GridBagConstraints c = new GridBagConstraints();		

		initComponents ();

		c.insets = new Insets(2, 2, 2, 2); // insets for all components	
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
		c.ipadx = 150;
		c.ipady = 150;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.5;		

		add (history, c);	 

		// Add buttons

		c.ipadx = 10;
		c.ipady = 0;
		c.weighty = 0;
		c.gridwidth = 1;

		JButton button = new JButton ("Save");
		c.gridy = 3;
		add (button, c);	

		button = new JButton ("Load");
		c.gridx = 1;
		c.gridy = 3;
		add (button, c);

		button = new JButton ("Undo");
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		add (button, c);	

		button = new JButton ("Resign");
		c.gridy = 5;
		add (button, c);

		startTimer ();
	}	

	private void initComponents ()
	{
		historyText = new JTextArea ();
		historyText.setEditable(false);			
		history = new JScrollPane (historyText);	
		historyText.setFocusable(false);

		player1 = new PlayerPane ("Player 1");
		player2 = new PlayerPane ("Player 2");
		
		player1.setPlaying ();
	}

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
	}

	public void startTimer ()
	{
		if (!timing)
		{
			timing = true;
			thread = new Thread (this);	
			thread.start ();
		}
	}	

	public void stopTimer ()
	{
		timing = false;
	}

	@Override
	public void run() 
	{			
		while (timing) // TODO
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
}

package mgci.jhdap.shogi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
//import javax.swing.JFileChooser;
//import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.filechooser.FileNameExtensionFilter;

public class StatsPanel extends JPanel implements Runnable
{
	private static final long serialVersionUID = 1L;

	protected PlayerPane player1;
	protected PlayerPane player2;

	protected JTextArea historyText;
	protected JScrollPane history;

	private int turn = 1;

	private Thread thread;

	public boolean timing = false;

	protected File defaultDirectory = new File (".");
	
	protected GraphicUI gui;

	public StatsPanel (GraphicUI parent) 
	{			
		gui = parent;
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout (new GridBagLayout ());			
		GridBagConstraints c = new GridBagConstraints();	
		setPreferredSize (new Dimension (160, 160));

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
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;		
		
		button = new JButton ("Pause");
		button.addActionListener(new MyButtonListener ());
		c.gridx = 0;
		c.gridy = 3;	
		button.setMargin(new Insets(2, 0, 2, 0));
		add (button, c);	

		button = new JButton ("Resign");
		button.addActionListener(new MyButtonListener ());		
		c.gridx = 1;
		c.gridy = 3;
		button.setMargin(new Insets(2, 0, 2, 0));
		add (button, c);
		
		button = new JButton ("Exit");
		button.addActionListener(new MyButtonListener ());
		c.weightx = 0;
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy = 4;
		button.setMargin(new Insets(2, 0, 2, 0));
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
		turn *= -1;
	}

	public void addMove (Move move)
	{
		historyText.append (move + "\n");
	}

	public void startTimer ()
	{
		if (!timing)
		{
			timing = true;
			PlayerPane player = turn == 1 ? player1 : player2;
			player.setPlaying ();
			thread = new Thread (this);	
			thread.start ();
		}
	}	

	public void stopTimer ()
	{
		timing = false;
		PlayerPane player = turn == 1 ? player1 : player2;
		player.setNotPlaying ();
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
				
				if (name.equals("Pause"))
				{
					button.setText ("Resume");
					StatsPanel.this.stopTimer ();
				}
				else if (name.equals ("Resume"))
				{
					button.setText ("Pause");
					StatsPanel.this.startTimer ();
				}
				else if (name.equals("Exit"))
				{
					gui.close ();
				}
				/*
				else if (name.equals("Save"))
				{
					save ();					
				}
				else if (name.equals("Load"))
				{					
					load ();					
				}
				*/
			}
		}

	}
	
	/*
	public void save ()
	{
		JFileChooser fc = new JFileChooser ("Save Game");	
		fc.setFileFilter(new FileNameExtensionFilter ("Text", "txt"));
		fc.setCurrentDirectory (defaultDirectory);
		int result = fc.showSaveDialog (this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			File path = fc.getSelectedFile();
			defaultDirectory = path;
			System.out.println (path);

			JTextArea history = historyText;
		}
	}

	public void load ()
	{		
		JFileChooser fc = new JFileChooser ("Load Game");		
		fc.setFileFilter(new FileNameExtensionFilter ("Text", "txt"));	
		fc.setAcceptAllFileFilterUsed(false);
		fc.setCurrentDirectory (defaultDirectory);
		int result = fc.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			File path = fc.getSelectedFile();
			defaultDirectory = path;
			System.out.println (path);			
		}
	}*/
}

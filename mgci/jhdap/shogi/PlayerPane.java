package mgci.jhdap.shogi;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

public class PlayerPane 
{
	public static final Font playingFont = new Font ("Tahoma", Font.BOLD, 14);
	public static final Font notPlayingFont = new Font ("Tahoma", Font.PLAIN, 14);
	
	public boolean playing = false;
	public JTextArea txt = new JTextArea ();
	public String name;
	public long dt;
	public long localStart;
	
	public PlayerPane(String name) 
	{
		this.name = name;
		localStart = System.currentTimeMillis ();	
		updateTime ();
		
		txt.setBorder(BorderFactory.createLineBorder(Color.black));
		txt.setFont(notPlayingFont);
		txt.setAlignmentY (JTextArea.CENTER_ALIGNMENT);
		txt.setEditable(false);
		txt.setFocusable(false);
		dt = 0;			
	}
	
	public void setPlaying ()
	{
		localStart = System.currentTimeMillis ();
		txt.setFont(playingFont);
		txt.setBorder(BorderFactory.createLineBorder(Color.red));
		playing = true;
	}
	
	public void setNotPlaying ()
	{
		txt.setFont(notPlayingFont);
		txt.setBorder(BorderFactory.createLineBorder(Color.black));
		playing = false;		
	}
	
	public void updateTime ()
	{
		txt.setText(" " + name + "\n    ");		
		
		long now = System.currentTimeMillis();
		long elapsed = now - localStart;		
		
		dt += elapsed;
		localStart = now;
		
		int minutes = (int) (dt / 1000 / 60);
		int seconds = (int) (dt / 1000) - (60 * minutes);
		
		txt.append (minutes + ":");
		if (seconds < 10)
			txt.append ("0");
		txt.append("" + seconds);
	}
	
	public void reset ()
	{
		setNotPlaying ();
		localStart = System.currentTimeMillis();
		dt = 0;		
		updateTime ();
	}
}

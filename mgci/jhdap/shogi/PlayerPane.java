package mgci.jhdap.shogi;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;

/** This class represents the info box of one player
 * in the statistics panel present in the shogi GUI.
 * 
 * @author 			Jiayin Huang
 * @author          Dmitry Andreevich Paramonov
 */
public class PlayerPane 
{
	/** The font of the player pane if it is their turn to play. 
	 */
	public static final Font playingFont = new Font ("Tahoma", Font.BOLD, 14);
	/** The font of the player pane if it is not their turn to play. 
	 */
	public static final Font notPlayingFont = new Font ("Tahoma", Font.PLAIN, 14);
	
	/** If true, then it is their turn to play. 
	 */
	public boolean playing = false;
	
	/** The JTextArea object that contains all of the text for this 
	 * player pane. 
	 */
	public JTextArea txt;
	
	/** The name of this player.
	 */
	public String name;
	
	/** Delta time; the total amount of time that this player
	 * has used up. 
	 */
	public long dt;
	/** The System.currentTimeMillis() value of the last time update. 
	 */
	protected long localStart;
	
	/** Constructs a new player pane of the given player name.
	 *  
	 * @param name	the name of the player
	 */
	public PlayerPane(String name) 
	{
		this.name = name;
		localStart = System.currentTimeMillis ();
		txt = new JTextArea  ();
		updateTime ();
		
		txt.setBorder(BorderFactory.createLineBorder(Color.black));
		txt.setFont(notPlayingFont);
		txt.setAlignmentY (JTextArea.CENTER_ALIGNMENT);
		txt.setEditable(false);
		txt.setFocusable(false);
		dt = 0;			
	}
	
	/** Sets it so that this player is playing. This resumes
	 * the timer, sets the pane outline to red, and sets the
	 * text to the playingFont. 
	 */
	public void setPlaying ()
	{
		localStart = System.currentTimeMillis ();
		txt.setFont(playingFont);
		txt.setBorder(BorderFactory.createLineBorder(Color.red));
		playing = true;
	}
	
	/** Sets it so that this player is not playing. This
	 * pauses the timer, sets the pane outline to black, and
	 * sets the text to the notPlayingFont.
	 */
	public void setNotPlaying ()
	{
		txt.setFont(notPlayingFont);
		txt.setBorder(BorderFactory.createLineBorder(Color.black));
		playing = false;		
	}
	
	/** Updates the JTextArea with the total elapsed time. 
	 */
	public void updateTime ()
	{
		// Reset text area		
		txt.setText(" " + name + "\n    ");		
		
		// Calculate elapsed time
		long now = System.currentTimeMillis();
		long elapsed = now - localStart;		
		
		dt += elapsed;
		localStart = now;
		
		// Convert milliseconds to minutes and seconds		
		int minutes = (int) (dt / 1000 / 60);
		int seconds = (int) (dt / 1000) - (60 * minutes);
		
		// Add the elapsed time to the text area.
		txt.append (minutes + ":");
		if (seconds < 10)
			txt.append ("0"); // leading 0 if necessary
		txt.append("" + seconds);
	}
	
	/** Resets this player plane. This calls the setNotPlaying 
	 * method, and sets dt to 0. 
	 */
	public void reset ()
	{
		setNotPlaying ();
		localStart = System.currentTimeMillis();
		dt = 0;		
		updateTime ();
	}
}

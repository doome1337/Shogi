package mgci.jhdap.shogi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

/** A Class that represents a sound effect. 
 * Comes with its own play() method. 
 * Most of this code was pilfered from: 
 * http://www.oracle.com/technetwork/java/index-139508.html ; 
 * 
 * We don't actually understand how most of this
 * code does what it does. It just does. Like magic.
 * 
 * @author mostly Oracle
 */
public class SoundEffect implements Runnable
{		
	/** The path to the sound file. 
	 */
	protected File sound;
	/** The current sound. 
	 */
	protected Object currentSound;
	/** The sequencer, which is used 
	 * when loading the sound file.  
	 */
	protected Sequencer sequencer;
	/** The thread that runs the playing
	 * of the sound effect. 
	 */
	protected Thread thread;

	/** Creates a new SoundEffect with the
	 * given sound file.
	 * 
	 * @param sound		the path to the sound file
	 */
	public SoundEffect (File sound)
	{
		this.sound = sound;
	}
	
	/** Plays the sound effect in a new thread.  
	 */
	public void play ()
	{
		thread = new Thread (this);	
		thread.start ();		
	}
	
	/** Loads and plays the sound.
	 */
	@Override
	public void run() 
	{	
		loadSound (sound);
		playSound ();		
	}
	
	/** Loads the sound into the given object.
	 * 
	 * @param object	the object in which to load the sound.
	 * @return true successful ; false otherwise
	 */
	private boolean loadSound (Object object) 
	{		
		if (object instanceof URL) 
		{				
			try 
			{
				currentSound = AudioSystem.getAudioInputStream((URL) object);
			} 
			catch(Exception e) 
			{
				try 
				{ 
					currentSound = MidiSystem.getSequence((URL) object);
				} 
				catch (InvalidMidiDataException imde) 
				{
					System.out.println("Unsupported audio file.");
					return false;
				} 
				catch (Exception ex) 
				{ 
					ex.printStackTrace(); 
					currentSound = null;
					return false;
				}
			}
		} 
		else if (object instanceof File) 
		{	
			try 
			{
				currentSound = AudioSystem.getAudioInputStream((File) object);
			} 
			catch(Exception e1) 
			{				
				try 
				{ 
					FileInputStream is = new FileInputStream((File) object);
					currentSound = new BufferedInputStream(is, 1024);
				} 
				catch (Exception e3) 
				{ 
					e3.printStackTrace(); 
					currentSound = null;
					return false;
				}				
			}
		}

		if (currentSound instanceof AudioInputStream) 
		{			
			try 
			{
				AudioInputStream stream = (AudioInputStream) currentSound;
				AudioFormat format = stream.getFormat();

				DataLine.Info info = new DataLine.Info(
						Clip.class, 
						stream.getFormat(), 
						((int) stream.getFrameLength() *
								format.getFrameSize()));

				Clip clip = (Clip) AudioSystem.getLine(info);				
				clip.open(stream);
				currentSound = clip;				
			} 
			catch (Exception ex) 
			{ 
				ex.printStackTrace(); 
				currentSound = null;
				return false;
			}
		} 
		else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) 
		{			
			try 
			{
				sequencer.open();
				if (currentSound instanceof Sequence) 
				{
					sequencer.setSequence((Sequence) currentSound);
				} 
				else 
				{
					sequencer.setSequence((BufferedInputStream) currentSound);
				}			
			} 
			catch (InvalidMidiDataException imde) 
			{ 
				System.out.println("Unsupported audio file.");
				currentSound = null;
				return false;
			} 
			catch (Exception ex) 
			{ 
				ex.printStackTrace(); 
				currentSound = null;
				return false;		
			}
		}

		return true;
	}
	
	/** Plays the sound. 
	 */
	private void playSound()
	{	
		if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream && thread != null) 
		{			
			sequencer.start();
			while (thread != null) 
			{
				try {Thread.sleep(99); } catch (Exception e) {break;}
			}
			sequencer.stop();
			sequencer.close();
		} 
		else if (currentSound instanceof Clip && thread != null) 
		{			
			Clip clip = (Clip) currentSound;
			clip.start();
			try {Thread.sleep(99); } catch (Exception e) { }
			while (clip.isActive() && thread != null) {
				try {Thread.sleep(99); } catch (Exception e) {break;}
			}
			clip.stop();
			clip.close();
		}	      
	}
}
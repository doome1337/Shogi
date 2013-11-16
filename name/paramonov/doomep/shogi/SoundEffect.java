package name.paramonov.doomep.shogi;

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

/**
 * Test class for sound effects. Most of it is from <http://www.oracle.com/technetwork/java/index-139508.html>
 */
public class SoundEffect implements Runnable
{		
	File sound;
	Object currentSound;
	Sequencer sequencer;	
	Thread thread;

	public SoundEffect (File sound)
	{
		this.sound = sound;
	}
	
	public void play ()
	{
		thread = new Thread (this);	
		thread.start ();		
	}
	
	@Override
	public void run() 
	{	
		loadSound (sound);
		playSound ();		
	}
	
	private boolean loadSound(Object object) 
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
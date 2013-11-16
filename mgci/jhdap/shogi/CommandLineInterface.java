package mgci.jhdap.shogi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * An experimental command line interface.
 * 
 * @author Jiayin Huang
 */
public abstract class CommandLineInterface extends JFrame 
{	
	private static final long serialVersionUID = 1L;

	/** The default background color. 
	 */
	private Color bgColor = Color.black;

	/** The default text color.	 
	 */
	private Color fgColor = Color.white;

	/** The default font. 
	 */
	private Font font = new Font ("Courier", Font.PLAIN, 12);	

	/** The large JTextPane for displaying all messages. 
	 */			
	protected JTextPane txt = new JTextPane ();

	/** The JTextField for input. 
	 */
	protected JTextField _input = new JTextField ();

	/** The SimpleAttributeSet for determining the style of the test in the JTextPane. 
	 */
	protected SimpleAttributeSet keyWord = new SimpleAttributeSet ();

	/** The list of commands in the form of Command objects. Each instance 
	 * has an execute method that corresponds with its regex and detail 
	 * String fields. Override the initCommands() method to add custom commands.
	 */
	public List<Command> commands = new ArrayList <> (0);

	/** Creates a default CommandLineInterface with dimensions 640 x 480.	 
	 */
	public CommandLineInterface ()
	{		
		this ("", 640, 480);
	}

	/** Creates a CommandLineInterface with dimensions 640 x 480
	 * and the indicated title.
	 *  
	 * @param title		name of interface
	 */
	public CommandLineInterface (String title)
	{		
		this (title, 640, 480);
	}

	/** Creates a CommandLineInterface with the indicated dimensions.
	 * 
	 * @param width		width of interface in pixels
	 * @param height	height of interface in pixels
	 */
	public CommandLineInterface (int width, int height)
	{		
		this ("", width, height);
	}

	/** Creates a CommandLineInterface with the indicated title and dimensions.
	 * 
	 * @param title		name of interface
	 * @param width		width of interface in pixels
	 * @param height	height of interface in pixels
	 */
	public CommandLineInterface (String title, int width, int height)
	{
		super (title);
		setContentPane (createContent());
		initTextPanes ();	
		initCommands ();

		setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
		setSize (width, height);		
		setVisible (true);	

		_input.requestFocusInWindow ();
	}

	/** Creates the content meant to be added to the JFrame. This
	 * includes the JTextPane and the JTextField.
	 * 
	 * @return the JPanel containing the text components
	 */
	private JPanel createContent ()
	{					
		JPanel noWrapPanel = new JPanel (new BorderLayout ());
		noWrapPanel.add (txt);
		JScrollPane pane = new JScrollPane (noWrapPanel);

		_input.addKeyListener(new MyKeyListener ());

		JPanel content = new JPanel (new BorderLayout());
		content.add(pane, "Center");
		content.add(_input, "South");

		return content;
	}

	/** Initializes the JTextPane and JTextField to their default states:
	 * Black background, white text, and Courier 14 pt font. 
	 */
	private void initTextPanes ()
	{
		txt.setFont(font);		
		txt.setEditable (false);
		txt.setBackground (bgColor);
		txt.setForeground(fgColor);
		txt.setCaretColor (fgColor);

		_input.setFont (font);
		_input.setBackground(bgColor);
		_input.setForeground(fgColor);
		_input.setCaretColor (fgColor);
	}

	/** Initializes the ArrayList containing all of the Command objects.
	 * This method is automatically called in all CommandLineInterface 
	 * constructors. Override this method to add custom commands. 
	 * To add a command, put something in this format:
	 * commands.add(new YourCustomCommand ("regexHere", " descriptionHere")); 
	 */
	public abstract void initCommands ();		

	/** Sets the background color of the entire panel to the indicated color. 
	 * Any text in the panel with a custom background color will remain that way.
	 * 
	 * @param bg the new background color
	 */
	public void setPaneBackground (Color bg)
	{
		bgColor = bg;
		txt.setBackground (bgColor);
		_input.setBackground (bgColor);
	}

	/** Sets the text color of the entire panel to the indicated color. 
	 * Any text in the panel with a custom text color will remain that way.
	 * 
	 * @param fg the new text color
	 */
	public void setPaneForeground (Color fg)
	{
		fgColor = fg;
		txt.setForeground (fgColor);
		txt.setCaretColor(fgColor);
		_input.setForeground (fgColor);
		_input.setCaretColor (fgColor);
	}

	/** Sets the text color for all subsequent text operations. 
	 * This overrides the panel's default text color. Putting null 
	 * as an argument resets the text color to default.
	 * 
	 * @param color	the new text color
	 */
	public void setTextColor (Color color)
	{
		if (color == null)
			color = fgColor;
		StyleConstants.setForeground(keyWord, color);
	}

	/** Sets the text background color for all subsequent text operations.
	 * This overrides the panel's default background color. 
	 * Putting null as an argument resets the text background color to default.
	 * 
	 * @param color	the new text color
	 */
	public void setTextBackground (Color color)
	{		
		if (color == null)
			color = bgColor;
		StyleConstants.setBackground(keyWord, color);
	}

	/** Sets the bold attribute for all subsequent text operations. 
	 * 
	 * @param b		specifies true/false for setting the attribute
	 */
	public void setBold (boolean b)
	{
		StyleConstants.setBold (keyWord, b);
	}

	/** Sets the italics attribute for all subsequent text operations. 
	 * 
	 * @param i		specifies true/false for setting the attribute
	 */
	public void setItalic (boolean i)
	{
		StyleConstants.setItalic(keyWord, i);
	}

	/** Sets the underline attribute for all subsequent text operations. 
	 * 
	 * @param u		specifies true/false for setting the attribute
	 */
	public void setUnderLine (boolean u)
	{
		StyleConstants.setUnderline(keyWord, u);
	}

	/** Sets the text font type for all subsequent text operations. 
	 * 
	 * @param name		the name of the font family
	 */
	public void setFontFamily (String name)
	{
		StyleConstants.setFontFamily(keyWord, name);				
	}

	/** Sets the text font size for all subsequent text operations.
	 * 
	 * @param size		the font size
	 */
	public void setSize (int size)
	{
		StyleConstants.setFontSize(keyWord, size);
	}

	/** Prints a String.
	 * 
	 * @param str	the String to be printed
	 */
	public void print (String str)
	{
		StyledDocument doc = txt.getStyledDocument ();
		try {			
			doc.insertString(doc.getLength(), str, keyWord);
		} catch(Exception e) {	 
			System.out.println(e); 
		}		
	}

	/** Prints a String and then a line break.
	 * 
	 * @param str	the String to be printed.
	 */
	public void println (String str)
	{
		print (str + "\n");
	}

	/** Prints a line break. 
	 */
	public void println ()
	{
		print ("\n");
	}

	/** Prints a message. This method is called when
	 * a command is not found.
	 */
	protected void printNotFoundMessage (String command)
	{
		println ("Unknown command \"" + command + "\"");
	}

	/** Searches through the commands list for a regex match
	 * with the inputed String. Invokes the Command's execute()
	 * method if a match is found. Does nothing otherwise.
	 * 
	 * @param input		the inputed String to act upon
	 */
	protected void parseInput (String input)
	{
		boolean found = false;
		for (int i = 0; i < commands.size (); i++)
			if (input.matches(commands.get(i).regex))
			{
				commands.get(i).execute (input);
				found = true;
			}

		if (!found)
			printNotFoundMessage (input);	
	}

	/** Listens to the JTextField at the bottom of the CommandLineInterface.
	 * Calls the parseInput method with the text in the JTextField as the argument
	 * if a KeyEvent.VK_ENTER key is inputed. Clears the JTextField after this.
	 */
	private class MyKeyListener extends KeyAdapter
	{
		@Override
		public void keyTyped (KeyEvent e)
		{						
			JTextField _input = CommandLineInterface.this._input;		
			char input = e.getKeyChar ();

			if (input == KeyEvent.VK_ENTER)
			{					
				String command = _input.getText().trim();
				_input.setText ("");				
				parseInput (command);				
			}					
		}
	}
}

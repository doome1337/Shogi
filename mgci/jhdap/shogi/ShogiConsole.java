package mgci.jhdap.shogi;

import java.awt.Color;

/** Experimental debug console for shogi GUI.
 * 
 * @author 			Jiayin
 * @author			Dmitry Andreevich Paramonov 
 */
public class ShogiConsole extends CommandLineInterface 
{	
	/** All of the Strings in this array will be
	 * automatically run in the constructor of this
	 * ShogiConsole.
	 */
	public static String [] startup = 
		{				
		}
	;
	
	private static final long serialVersionUID = 1L;
	
	/** The GraphicUI object to which this console is connected. 
	 */
	protected GraphicUI gui;
	
	/** The current notation format that the
	 * console uses for outputting coordinates.
	 * <ul>
	 * <li>	0 = the GameState x,y coordinates.
	 * <li> 1 = the standard shogi file,rank coordinates.
	 * </ul>
	 */
	protected int notation = 0;	
	
	/** Creates a new shogi console object.
	 * 
	 * @param parent	the GraphicUI object that created
	 * 					this ShogiConsole. 				
	 * @param title		the title of this console
	 * @param width		the width of this console
	 * @param height	the height of this console
	 */
	public ShogiConsole(GraphicUI parent, String title, int width, int height) 
	{
		super(title, width, height);
		gui = parent;
		setLocationRelativeTo (gui);
		setLocation (gui.getWidth(), 0);		
		printInit ();
		for (int i = 0; i < startup.length; i++)
			parseInput (startup[i]);
	}
	
	/** Initializes all of the commands 
	 * for this ShogiConsole. Called
	 * automatically by the constructor. 
	 */
	@Override
	public void initCommands() 
	{	
		commands.add(new BoardLabels ("^b labels( [01])?$", 
				"b labels\t\tenables or disables board labels"));
		
		commands.add(new BoardOffset ("^b os( \\d+ \\d+)?$", 
				"b os\t\tsets the board offset relative to the top left corner of JPanel"));
		
		commands.add(new Choices ("^choices \\d[a-i\\d]$", 
				"choices\t\tdraws all possible moves of the piece at XY"
						+"\n\t\tNote: there is currently a strange bug where" 
						+"\n\t\tthe squares don't show up the first time."));

		commands.add(new Close ("^close$", 
				"close\t\tcloses console"));				

		commands.add(new ConfigLog ("^config log( [01])?$", 
				"config log\tenables or disables GUI log"
						+"\n\t\t0 = disabled"
						+"\n\t\t1 = enabled"));

		commands.add(new ConfigNotation ("^config notation( [01])?$", 
				"config notation\tsets notation format for console"
						+"\n\t\t0 = GameState xy"
						+"\n\t\t1 = shogi standard"));				
		
		commands.add(new Drop ("^drop -?1 \\w+ \\d[a-i\\d]$", 
				"drop\t\tdrops indicated piece from indicated drop table onto board at XY"));
		
		commands.add(new DropTableSize ("^dt size( \\d+ \\d+)?$", 
				"dt size\t\tsets the size of the drop tables"));		

		commands.add(new DumpPiece ("^dump piece (-?1 (\\w+)|\\d[a-i\\d])$", 
				"dump piece\tcalls the toString() method of the Piece at the given location"));

		commands.add(new DumpState ("^dump state$", 
				"dump state\tcalls the toString() method of this GameState"));	

		commands.add(new Exit ("^exit$", 
				"exit\t\texits the program"));

		commands.add(new Help ("^help$", 
				"help\t\tlists all commands"));

		commands.add(new HelpDetail ("^help( [\\w\\p{Punct}]+)+$", 
				"help <command>\tprints specific details about the given command"));	

		commands.add(new Move ("^move \\d[a-i\\d] \\d[a-i\\d]$", 
				"move\t\tmoves piece at xy to XY"));

		commands.add(new Param ("^param$", 
				"param\t\tlists the regex format of all valid commands"));
		
		commands.add(new PieceSize ("^p size( \\d+ \\d+)?$", 
				"p size\t\tsets the size of the shogi pieces"));
		
		commands.add(new Player ("^player( -?1)?$", 
				"player\t\tsets the current player"					
						+"\n\t\t1 = bottom player"
						+"\n\t\t-1 = top player"));

		commands.add(new ProMode ("^pro_mode( [01])?$", 
				"pro_mode\t\tenables or disables pro mode"						
						+ "\n\t\tDisables tile highlighting."
						+ "\n\t\tInstant loss if an invalid move is attempted."
						+"\n\t\t0 = disabled"
						+"\n\t\t1 = enabled"));

		commands.add(new ReloadTextures ("^reload textures$", 
				"reload textures\treloads the textures"
						+"\n\t\tboardOffset, dropTableOffsets, dropTableSize, pieceSize,"
						+"\n\t\tpieceOffset, and tileSize are all calculated based on the"
						+"\n\t\timages, so running this command would reset those fields."
						+"\n\t\tto their default values."));

		commands.add(new Reset ("^reset$", 
				"reset\t\tresets shogi board"));	
		
		commands.add(new ShowLast ("^show last( [01])?$", 
				"show last\t\tenables or disables the indication of the last move"));
				
		commands.add(new TileSize ("^t size( \\d+ \\d+)?$", 
				"t size\t\tsets the size of the 9x9 tiles"));
		
		commands.add(new Textures ("^textures( [\\w\\p{Punct}]+)?$", 
				"textures\t\tsets the path of the texture pack"
						+ "\n\t\tpath is relative to the parent directory (\"Shogi/\")"));
		
		commands.add(new Turns ("^turns( [01])?$", 
				"turns\t\tenables or disables turn taking"					
						+"\n\t\t0 = disabled"
						+"\n\t\t1 = enabled"));
	}

	/** Prints the welcome message. Automatically
	 * called by the constructor. 
	 */
	public void printInit ()
	{
		println ("Welcome to Shogi console.");		
		println ("Run 'help' to list commands.");
	}


	/** Prints a configuration message.
	 * 
	 * @param config		the name of the changed field
	 * @param newValue		the new value of the field
	 */
	public void logConfig (String config, String newValue)
	{
		println ("Set " + config + " to " + newValue + ".");
	}

	/** Called when a piece is deselected. Prints a deselected message.
	 * 
	 * @param p		the piece that was deselected
	 */
	public void logDeselectPiece (Piece p)
	{		
		Tile location = new Tile (p.x, p.y);
		println ("Deselected piece \"" + p + "\" at " + location.getCode (notation));
	}

	/** Called if a piece of the given pieceName could
	 * not be found in the drop table of the given allegiance.
	 * Prints a "could not find" message.
	 * 
	 * @param pieceName		the piece name that could not be found
	 * @param allegiance	the allegiance of the drop table
	 */
	public void logDropTable404 (String pieceName, int allegiance)
	{
		println ("Could not find \"" + pieceName + "\" piece in drop table " + allegiance + ".");
	}	

	/** Prints an exception in red text.
	 * 
	 * @param e		the Exception
	 */
	public void logError (Exception e)
	{
		logError (e.toString ());
	}

	/** Prints the given error message in red text.
	 * 
	 * @param e		the error message
	 */
	public void logError (String e)
	{
		setTextColor (Color.red);
		println (e);
		setTextColor (null);		
	}

	/** Called if coordinates are invalid. Prints 
	 * an "invalid coordinates" message.
	 * 
	 * @param tile		the invalid coordinates
	 */
	public void logInvalidCoordinates (String tile)
	{
		println ("Invalid coordinates \"" + tile + "\".");
	}

	/** Called if an invalid drop was attempted. Prints
	 * an invalid drop message.
	 * 
	 * @param allegiance	the allegiance of the drop table
	 * @param p				the piece that was dropped
	 * @param location		the location of the drop attempt
	 */
	public void logInvalidDrop (int allegiance, Piece p, Tile location)
	{
		println ("Invalid drop location for drop from table " + allegiance 
				+ " a " + p + " at " + location.getCode (notation) + " ");
	}

	/** Called if an invalid move was attempted. Prints
	 * an invalid move message.
	 * 
	 * @param p			the piece that was moved
	 * @param from		the initial position of the piece
	 * @param to		the location of the move attempt
	 */
	public void logInvalidMove (Piece p, Tile from, Tile to)
	{	
		println ("Could not move piece \"" + p + "\" at " + from.getCode (notation)
				+ " to " + to.getCode (notation));
	}	

	/** Called when possible moves were drawn.
	 * Prints an acknowledgment message.
	 * 
	 * @param p		the piece of which the possible moves were drawn.
	 */
	public void logPossibleMoves (Piece p)
	{
		Tile location = new Tile (p.x, p.y);
		println ("Drew possible moves for piece \"" + p + "\" at " + location.getCode (notation));
	}

	/** Called when a piece is promoted. Prints acknowledgment message.
	 * 
	 * @param p		the piece that was promoted
	 */
	public void logPromote (Piece p)
	{
		Tile location = new Tile (p.x, p.y);
		println ("Promoted piece \"" + p + "\" at " + location.getCode (notation));
	}

	/** Called when the board is reset. Prints acknowledgment message. 
	 */
	public void logReset ()
	{
		println ("Board reset.");
	}

	/** Called when a piece is selected. Prints acknowledgment message.
	 * 
	 * @param p	the piece that was selected
	 */
	public void logSelectPiece (Piece p)
	{		
		Tile location = new Tile (p.x, p.y);
		println ("Selected piece \"" + p + "\" at " + location.getCode (notation));
	}

	/** Called when a variable is toggled. Prints configuration message. 
	 * 
	 * @param enable	"1" if enabled ; "0" if disabled
	 * @param toggle	the name of the variable
	 */
	public void logToggle (String enable, String toggle)
	{
		String action = enable.startsWith ("1") ? "Enabled" : "Disabled";
		println (action + " " + toggle + ".");
	}

	/** Called when a piece is dropped. Prints acknowledgment message.
	 * 
	 * @param allegiance		the allegiance of the drop table
	 * @param p					the piece that was dropped
	 * @param location			the location of the dropping
	 */
	public void logValidDrop (int allegiance, Piece p, Tile location)
	{
		println ("From table " + allegiance + ": Dropped a \"" + p 
				+ "\" piece at " + location.getCode (notation) + ".");
	}

	/** Called when a piece is moved. Prints acknowledgment message.
	 * 
	 * @param p			the piece that was moved
	 * @param from		the initial position of the piece
	 * @param to		where the piece was moved to
	 */
	public void logValidMove (Piece p, Tile from, Tile to)
	{		
		println ("Moved piece \"" + p + "\" at " + from.getCode (notation)
				+ " to " + to.getCode (notation));
	}

	// The Command Classes	
	
	private class BoardLabels extends Command
	{
		public BoardLabels(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			if (parameters.length == 3)
			{				
				gui.board.showBoardLabels = parameters[2].equals("1") ? true : false;
				gui.board.repaint();
				logConfig ("board labels", parameters[2]);
			}
			else			
				println ("show board labels = " + gui.board.showBoardLabels);
		}		
	}

	
	private class BoardOffset extends Command
	{
		public BoardOffset(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			if (parameters.length == 4)
			{
				int x = Integer.parseInt(parameters[2]);
				int y = Integer.parseInt(parameters[3]);
				gui.board.setBoardOffset (x, y);
				logConfig("board offset", parameters[2] + "," + parameters[3]);
			}
			else
				println ("board offset = " + gui.board.getBoardOffset());
		}		
	}
	

	private class Choices extends Command 
	{
		public Choices(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			Tile at = new Tile (parameters[1]);

			if (at.isValid())
			{
				Piece p = gui.board.state.getPieceAt(at.x, at.y);
				gui.board.drawPossibleMoves (gui.board.getGraphics(), p);	
				logPossibleMoves (p);
				//TODO: bug where it doesn't draw properly the first time
			}
			else
				logInvalidCoordinates (parameters[1]);				
		}		
	}


	/** Closes this CommandLineInterface 
	 */
	private class Close extends Command
	{
		public Close(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			println ("Closing...");
			ShogiConsole.this.dispose ();			
		}		
	}
		

	private class ConfigLog extends Command
	{
		public ConfigLog(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			if (parameters.length == 3)
			{
				if (parameters[2].equals("1"))
					gui.board.log = true;
				else
					gui.board.log = false;
				logToggle (parameters[2], "GUI logging");
			}
			else			
				println ("log = " + gui.board.log);
		}		
	}

	private class ConfigNotation extends Command
	{
		public ConfigNotation(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			if (parameters.length == 3)
			{
				notation = Integer.parseInt(parameters[2]);
				logConfig ("notation format", parameters[2]);
			}
			else			
				println ("notation = " + notation);
		}		
	}
			

	private class Drop extends Command
	{
		public Drop(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			int allegiance = Integer.parseInt(parameters[1]);			
			String pieceName = parameters[2];			
			Tile to = new Tile (parameters[3]);			
			Piece p = gui.board.getDropTablePieceAt (allegiance, pieceName);
			if (p != null)
			{
				if (to.isValid())	
				{
					try
					{
						gui.board.move(p, to);
					}
					catch (Exception e)
					{
						logError (e);
						logError ("It's because right now, piece dropping is implemented"
								+ " as moving a piece from -1,-1 to x,y. This should be fixed.");
					}
				}
				else				
					logInvalidDrop (allegiance, p, to);				
			}
			else			
				logDropTable404 (pieceName, allegiance);			
		}		
	}	
	
	private class DropTableSize extends Command
	{
		public DropTableSize(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split(" ");

			if (parameters.length == 4)
			{
				int w = Integer.parseInt (parameters[2]);
				int h = Integer.parseInt (parameters[3]);
				gui.board.setDropTableSize(w, h);
				logConfig("drop table size", parameters[2] + "," + parameters[3]);
			}
			else
				println ("drop table size = " + gui.board.getDropTableSize());
		}		
	}
	
	private class DumpPiece extends Command
	{
		public DumpPiece(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");
			Piece p = null;

			if (parameters.length == 4) // piece is in drop table
			{
				int allegiance = Integer.parseInt (parameters[2]);	

				p = gui.board.getDropTablePieceAt (allegiance, parameters[3]);	

				if (p == null)
					logDropTable404 (parameters[3], allegiance);
			}
			else // piece is on board
			{
				Tile tile = new Tile (parameters[2]);
				if (tile.isValid())
					p = gui.board.state.getPieceAt(tile.x, tile.y);
				else
					logInvalidCoordinates (parameters[2]);
			}

			if (p != null)
			{
				setTextColor (Color.yellow);
				println (p.toString ());
				setTextColor (null);
			}
		}		
	}

	private class DumpState extends Command
	{
		public DumpState (String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			setTextColor (Color.yellow);
			println (gui.board.state.toString ());
			setTextColor (null);
		}		
	}
	

	private class Exit extends Command
	{
		public Exit(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			println ("Exiting...");
			ShogiConsole.this.dispose ();
			gui.dispose ();
		}		
	}

	private class Help extends Command
	{
		public Help(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			setTextColor (Color.green);
			for (int i = 0; i < commands.size (); i++)
				println (" " + commands.get(i).detail.split("\n")[0]);
			setTextColor (null);
		}
	}

	private class HelpDetail extends Command
	{
		public HelpDetail(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");
			command = parameters [1];
			boolean found = false;

			for (int i = 2; i < parameters.length; i++)
				command += " " + parameters[i];

			setTextColor (new Color (192, 253, 11));

			for (int i = 0; i < commands.size (); i++)
				if (commands.get(i).detail.split ("\t")[0].equals(command))
				{
					println (" " + commands.get(i).detail);
					println ("\t      " + commands.get(i).regex);
					found = true;
				}

			setTextColor (null);

			if (!found)
				printNotFoundMessage (command);
		}
	}

	private class Move extends Command
	{
		public Move(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split(" ");		

			Tile from = new Tile (parameters[1]);
			Tile to = new Tile (parameters[2]);	

			if (!from.isValid ())			
				logInvalidCoordinates (parameters[1]);
			else if (!to.isValid ())
				logInvalidCoordinates (parameters[2]);
			else
			{						
				Piece p = gui.board.state.getPieceAt(from.x, from.y);
				if (!gui.board.move(p, new Tile (to.x, to.y)))		
					logInvalidMove (p, from, to);
			}
		}		
	}

	private class Param extends Command
	{
		public Param(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			setTextColor (new Color (12, 252, 198));
			for (int i = 0; i < commands.size (); i++)
				println (commands.get(i).regex);
			setTextColor (null);
		}
	}
	
	private class PieceSize extends Command
	{
		public PieceSize(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split(" ");

			if (parameters.length == 4)
			{
				int w = Integer.parseInt (parameters[2]);
				int h = Integer.parseInt (parameters[3]);
				gui.board.setPieceSize(w, h);
				logConfig("piece size", parameters[2] + "," + parameters[3]);
			}
			else
				println ("piece size = " + gui.board.getPieceSize());
		}
	}
	
	private class Player extends Command
	{
		public Player(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");
			if (parameters.length == 2)
			{				
				gui.board.turn = Integer.parseInt(parameters[1]);
				logConfig("current player", parameters[2]);
			}
			else
				println ("current player = " + gui.board.turn);
		}
		
	}

	private class ProMode extends Command
	{
		public ProMode(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			if (parameters.length == 2)
			{
				if (parameters[1].equals("1"))
					gui.board.proMode = true;
				else
					gui.board.proMode = false;
				logToggle (parameters[1], "pro_mode");
			}
			else			
				println ("pro_mode = " + gui.board.proMode);
		}		
	}

	private class Reset extends Command
	{
		public Reset(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{			
			gui.reset ();
		}		
	}	
	
	private class ShowLast extends Command
	{
		public ShowLast(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			if (parameters.length == 3)
			{				
				if (parameters[2].equals ("1"))
					gui.board.showLastMove = true;
				else
				{
					gui.board.showLastMove = false;
					gui.board.lastMoved = null;
				}
				gui.board.repaint();
				logConfig ("show last move", parameters[2]);
			}
			else			
				println ("show last move = " + gui.board.showBoardLabels);
		}		
	}
	
	private class Textures extends Command
	{
		public Textures(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split(" ");
			if (parameters.length == 2)
			{				
				gui.board.texturePath = parameters[1];
				logConfig ("texture path", parameters[1]);
				parseInput ("reload textures");
			}
			else
				println ("texture path = " + gui.board.texturePath);
		}

	}	

	private class ReloadTextures extends Command
	{
		public ReloadTextures(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			gui.board.loadImages();
			println ("Reloaded texture pack at: " + gui.board.texturePath);
		}		
	}
	
	private class TileSize extends Command
	{
		public TileSize(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split(" ");

			if (parameters.length == 4)
			{
				int w = Integer.parseInt (parameters[2]);
				int h = Integer.parseInt (parameters[3]);
				gui.board.setTileSize(w, h);
				logConfig("tile size", parameters[2] + "," + parameters[3]);
			}
			else
				println ("tile size = " + gui.board.getTileSize());
		}
	}
	
	private class Turns extends Command
	{
		public Turns(String regex, String detail) {
			super(regex, detail);
		}
		@Override
		void execute(String command) 
		{
			String[] parameters = command.split (" ");

			if (parameters.length == 2)
			{
				if (parameters[1].equals("1"))
					gui.board.takeTurns = true;
				else
					gui.board.takeTurns = false;
				logToggle (parameters[1], "turn taking");
			}
			else			
				println ("turn taking = " + gui.board.takeTurns);
		}		
	}

	/** Closes the console. Updates the 
	 * consoleIsOpen and log variables accordingly. 
	 */
	@Override
	public void dispose ()
	{
		gui.consoleIsOpen = false;
		gui.board.log = false;
		super.dispose();
	}
}

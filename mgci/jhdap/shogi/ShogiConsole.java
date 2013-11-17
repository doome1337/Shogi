package mgci.jhdap.shogi;

import java.awt.Color;

// TODO: annotate

/** Experimental debug console for shogi GUI.
 * 
 * @author Jiayin
 */
public class ShogiConsole extends CommandLineInterface 
{	
	private static final long serialVersionUID = 1L;
	private GraphicUI gui;
	private int notation = 0;

	public ShogiConsole(GraphicUI gui, String title, int width, int height) 
	{
		super(title, width, height);
		setLocationRelativeTo (gui);
		setLocation (gui.getWidth(), 0);
		this.gui = gui;
		printInit ();
	}
	@Override
	public void initCommands() 
	{		
		commands.add(new BoardOffset ("^b os( \\d+ \\d+)?$", 
				"b os\t\tsets the board offset relative to the top left corner of JPanel"));
		
		commands.add(new Choices ("^choices \\d[a-i\\d]$", 
				"choices\t\tdraws all possible moves of the piece at XY"
						+"\n\t\tNote: there is currently a strange bug where" 
						+"\n\t\tthe squares don't show up the first time."));

		commands.add(new Close ("^close$", 
				"close\t\tcloses console"));				

		commands.add(new ConfigLog ("^config log ?[01]?$", 
				"config log\tenables or disables GUI log"
						+"\n\t\t0 = disabled"
						+"\n\t\t1 = enabled"));

		commands.add(new ConfigNotation ("^config notation ?[01]?$", 
				"config notation\tsets notation format for console"
						+"\n\t\t0 = GameState xy"
						+"\n\t\t1 = shogi standard"));
				
		commands.add(new ConfigTexturePack ("^config textures( [\\w\\p{Punct}]+)?$", 
				"config textures\tsets the path of the texture pack"
						+ "\n\t\tpath is relative to the parent directory (\"Shogi/\")"));

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

		commands.add(new ProMode ("^pro_mode( [01])?$", 
				"pro_mode\t\tenables or disables pro mode"						
						+ "\n\t\tDisables tile highlighting."
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
		
		commands.add(new TileSize ("^t size( \\d+ \\d+)?$", 
				"t size\t\tsets the size of the 9x9 tiles"));
	}

	public void printInit ()
	{
		println ("Welcome to Shogi console.");		
		println ("Run 'help' to list commands.");
	}




	public void logConfig (String config, String newValue)
	{
		println ("Set " + config + " to " + newValue + ".");
	}

	public void logDeselectPiece (Piece p)
	{		
		Tile location = new Tile (p.x, p.y);
		println ("Deselected piece \"" + p + "\" at " + location.getCode (notation));
	}

	public void logDropTable404 (String pieceName, int allegiance)
	{
		println ("Could not find \"" + pieceName + "\" piece in drop table " + allegiance + ".");
	}	

	public void logError (Exception e)
	{
		logError (e.toString ());
	}

	public void logError (String e)
	{
		setTextColor (Color.red);
		println (e);
		setTextColor (null);		
	}

	public void logInvalidCoordinates (String tile)
	{
		println ("Invalid coordinates \"" + tile + "\".");
	}

	public void logInvalidDrop (int allegiance, Piece p, Tile location)
	{
		println ("Invalid drop location for drop from table " + allegiance 
				+ " a " + p + " at " + location.getCode (notation) + " ");
	}

	public void logInvalidMove (Piece p, Tile from, Tile to)
	{	
		println ("Could not move piece \"" + p + "\" at " + from.getCode (notation)
				+ " to " + to.getCode (notation));
	}	

	public void logPossibleMoves (Piece p)
	{
		Tile location = new Tile (p.x, p.y);
		println ("Drew possible moves for piece \"" + p + "\" at " + location.getCode (notation));
	}

	public void logPromote (Piece p)
	{
		Tile location = new Tile (p.x, p.y);
		println ("Promoted piece \"" + p + "\" at " + location.getCode (notation));
	}

	public void logReset ()
	{
		println ("Board reset.");
	}

	public void logSelectPiece (Piece p)
	{		
		Tile location = new Tile (p.x, p.y);
		println ("Selected piece \"" + p + "\" at " + location.getCode (notation));
	}

	public void logToggle (String enable, String toggle)
	{
		String action = enable.startsWith ("1") ? "Enabled" : "Disabled";
		println (action + " " + toggle + ".");
	}

	public void logValidDrop (int allegiance, Piece p, Tile location)
	{
		println ("From table " + allegiance + ": Dropped a \"" + p 
				+ "\" piece at " + location.getCode (notation) + ".");
	}

	public void logValidMove (Piece p, Tile from, Tile to)
	{		
		println ("Moved piece \"" + p + "\" at " + from.getCode (notation)
				+ " to " + to.getCode (notation));
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
			

	private class ConfigTexturePack extends Command
	{
		public ConfigTexturePack(String regex, String detail) {
			super(regex, detail);
		}

		@Override
		void execute(String command) 
		{
			String[] parameters = command.split(" ");
			if (parameters.length == 3)
			{				
				gui.board.texturePath = parameters[2];
				logConfig ("texture path", parameters[2]);
				println ("Reload textures for changes to take effect.");
			}
			else
				println ("texture path = " + gui.board.texturePath);
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
			gui.board.reset ();
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

	@Override
	public void dispose ()
	{
		gui.consoleIsOpen = false;
		gui.board.log = false;
		super.dispose();
	}
}

package name.paramonov.doomep.shogi;
import java.io.*;
import java.util.List;

/** A class used for playing shogi in a text user interface.
 * Meant to be a precursor to the GUI.
 * @author       Jiayin Huang
 * @author       Dmitry Andreevich Paramonov
 */
public class TextUI
{
	/** An array of valid inputs in regex format.
	 */
	public static final String[] regex =
		{
		"^board$",
		"^choices \\d\\d$",
		"^drop \\d \\d\\d$",      
		"^help$",
		"^move \\d\\d \\d\\d$",
		"^promote \\d\\d$",
		"^quit$",
		"^reset$",
		"^who$"
		}
	;

	/** An array of valid inputs in user-friendly format.
	 */
	public static final String[] commands =
		{
		" board",
		" choices [int x][int y]",
		" drop [int piece number] [int x][int y]",      
		" help",
		" move [int x1][int y1] [int x2][int y2]",
		" promote [int x][int y]",
		" quit",
		" reset",
		" who"
		}
	;

	/** An array of objects with an execute method
	 * corresponding with the command in the commands array.      
	 */
	public static final Command[] faire =
		{
		new Board (),
		new Choices (),
		new Drop (),      
		new Help (),
		new Move (),
		new Promote (),
		new Quit (),
		new Reset (),
		new Who ()
		}
	;

	/** The current text UI version.
	 */
	public static final String version = "version 2013.11.10.1946";

	/** Whether this text UI is still running
	 */
	private static boolean running = true;

	/** Whose turn it is to play.
	 */
	protected static int player = 1;

	/** The board. 
	 */
	protected static GameState state = new GameState ();

	/** The main program.
	 */
	public static void main (String[] args)
	{
		// Initialization

		BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
		String input = "";

		state.defaultBoardConfigure ();

		// Input

		printInit ();
		while (running)
		{            
			System.out.print ("\n> ");
			try
			{
				input = in.readLine ().trim ();
				parseInput (input);
			}
			catch (IOException io)
			{
			}
		}

	} // main method

	/** Parses the input and acts upon it.
	 * 
	 * @param input 	the inputed String 
	 */
	public static void parseInput (String input)
	{
		boolean found = false;
		for (int i = 0 ; i < regex.length && !found; i++)
		{
			if (input.matches (regex [i]))
				faire [i].execute (input);
		}
	}

	/** Prints the welcome message.
	 */
	public static void printInit ()
	{
		System.out.println ("Welcome to Shogi Text UI(" + version + ")");
		System.out.println ();
		System.out.println ("Input 'help' to display the list of commands.");       
		parseInput ("board");
	}

	/** Prints the current state of the board.
	 */
	private static class Board implements Command
	{    	
		@Override
		public void execute (String command)
		{
			List<Piece> d1 = state.getDropTable1();
			List<Piece> d2 = state.getDropTable2();

			// Print player -1's drop table
			for (int i = 0; i < d2.size(); i++)     		
				System.out.print(i + ":" + d2.get(i).getDoubleCharRepresentation()+"; ");

			// Print top x-coordinates
			System.out.println ();
			System.out.println("  0 1 2 3 4 5 6 7 8");

			// Print pieces and y-coordinates on either side
			for (int i = 8; i >= 0; i--) 
			{
				System.out.print(i + "|");
				for (int j = 0; j < 9; j++)     			  				
					System.out.print(state.getPieceAt(j, i).getDoubleCharRepresentation());

				System.out.println("|"+i);
			}    		
			// Print bottom x-coordinates
			System.out.println("  0 1 2 3 4 5 6 7 8");

			// Print player 1's drop table
			for (int i = 0; i < d1.size(); i++)			
				System.out.print(i+":"+d1.get(i).getDoubleCharRepresentation()+"; ");			
		}
	}
	/** Prints all of the possible moves available to the piece 
	 * at the given coordinates.
	 */
	private static class Choices implements Command
	{
		@Override
		public void execute(String command) 
		{
			String[] parameters = command.split (" ");

			int x = Character.getNumericValue (parameters [1].charAt (0));
			int y = Character.getNumericValue (parameters [1].charAt (1));

			Piece piece = state.getPieceAt(x, y);
			boolean[][] canMoveHere = piece.generateMoves(state);

			// Print board state with piece options, if there's a piece at given coordinates

			if (!(piece instanceof EmptyPiece))
			{
				// Print top x coordinates
				System.out.println("  0 1 2 3 4 5 6 7 8");

				// Print pieces and y coordinates on either side
				for (int i = 8; i >= 0; i--)
				{
					System.out.print(i + "|");
					for (int j = 0; j < canMoveHere[i].length; j++)
					{
						if (canMoveHere[j][i])
						{
							if (state.getPieceAt(j, i) instanceof EmptyPiece)
								System.out.print("^ ");
							else
								System.out.print("* ");
						}
						else 
							System.out.print(state.getPieceAt(j, i).getDoubleCharRepresentation());
					}
					System.out.println ("|" + i);
				}
				System.out.println("  0 1 2 3 4 5 6 7 8");
			}
			else
				System.out.println ("No such piece.");
		}    	
	}

	/** Drops the selected piece onto the table.
	 */
	private static class Drop implements Command
	{   
		@Override
		public void execute (String command)
		{
			String[] parameters = command.split (" ");			   

			int piece = Integer.parseInt (parameters [1]);

			int x = Character.getNumericValue (parameters [2].charAt (0));
			int y = Character.getNumericValue (parameters [2].charAt (1));

			if (piece < state.getCorrectDropTable(player).size())
			{
				if (state.getPieceAt(x, y) instanceof EmptyPiece)
				{					
					state.dropPieceFromTable(player, x, y, piece); 
					player *= -1;
					parseInput ("board");
				}
				else				
					System.out.println ("Invalid move.");
				
			}
			else			
				System.out.println ("No such piece.");			
		}
	}

	/** Prints a list of all valid commands.
	 */
	private static class Help implements Command
	{    
		@Override
		public void execute (String command)
		{ 
			System.out.println ("Shogi Text UI " + version);
			System.out.println ();
			for (int i = 0 ; i < commands.length ; i++)
				System.out.println (" " + commands [i]);
		}
	}

	/** Moves the piece at (x1,y1) to (x2,y2), if valid.
	 */
	private static class Move implements Command
	{    	
		@Override
		public void execute (String command)
		{
			int x1 = Character.getNumericValue (command.charAt (5));
			int y1 = Character.getNumericValue (command.charAt (6));

			int x2 = Character.getNumericValue (command.charAt (8));
			int y2 = Character.getNumericValue (command.charAt (9));

			if (state.getPieceAt(x1, y1).getAllegiance() == player)
			{
				if (state.getPieceAt(x1, y1).isValidMove(state, x2, y2))
				{					
					state.getPieceAt(x1, y1).move(state, x2, y2);  
					player *= -1;
					parseInput ("board");
				}       
				else				
					System.out.println ("Invalid move.");
				
			}
			else			
				System.out.println ("Not your turn.");
			
		}        
	}

	/** Promotes the piece at (x,y).
	 */
	private static class Promote implements Command
	{    
		@Override
		public void execute (String command)
		{
			int x = Character.getNumericValue (command.charAt (8));
			int y = Character.getNumericValue (command.charAt (9));

			if (state.getPieceAt(x, y).getAllegiance() == player)
			{
				if (state.getPieceAt(x, y).isPromotable())
				{					
					state.promotePieceAt(x, y);
					player *= -1;
					parseInput ("board");
				}
				else				
					System.out.println ("Cannot promote.");				
			}
			else			
				System.out.println ("Not your turn.");
			
		}
	}

	/** Stops the program.
	 */
	private static class Quit implements Command
	{    	
		@Override
		public void execute (String command)
		{
			running = false;
		}
	}

	/** Resets the board.
	 */
	private static class Reset implements Command
	{    
		@Override
		public void execute (String command)
		{
			state = new GameState ();
			state.defaultBoardConfigure();
			parseInput ("board");
		}
	}

	/** Prints whose turn it is.
	 */
	private static class Who implements Command
	{
		@Override
		public void execute (String command)
		{
			System.out.println ("It's player " + player + "'s turn."); 
		}
	}

} // TextUI class
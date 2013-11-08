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
    /** 
     * An array of valid inputs in regex format.
     */
    static final String[] regex =
    {
      "^board$",
      "^drop \\d \\d \\d\\d$",      
      "^help$",
      "^move \\d\\d \\d\\d$",
      "^promote \\d\\d$",
      "^quit$",
      "^reset$"
    }
    ;
 
    /**
     * An array of valid inputs in user-friendly format.
     */
    static final String[] commands =
    {
      " board",
      " drop [int allegiance] [int piece number] [int x][int y]",      
      " help",
      " move [int x1][int y1] [int x2][int y2]",
      " promote [int x][int y]",
      " quit",
      " reset"
    }
    ;
    
    /** An array of objects with an execute method
     * corresponding with the command in the commands array.      
     */
    static final Command[] faire =  // tentative array name
    {
      new Board (),
      new Drop (),      
      new Help (),
      new Move (),
      new Promote (),
      new Quit (),
      new Reset ()
    }
    ;
    
    /** The current text UI version.
     * Version format (yyyy.mm.dd.hhmm) temporary. 
     * Might change to something like (0.x).
     */
    static final String version = "version 2013.11.06.1832";
    
    /**
     * Whether this text UI is still running
     */
    static boolean running = true;
    
    /**
     * The board. 
     */
    static GameState state = new GameState ();
    
    /**
     * The main program.
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

    /**
     * Parses the input and acts upon it.
     * @param input - The inputed String. 
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

    /**
     * Prints the welcome message.
     */
    public static void printInit ()
    {
        System.out.println ("Welcome to Shogi Text UI(" + version + ")");
        System.out.println ();
        System.out.println ("Input 'help' to display the list of commands.");       
        parseInput ("board");
    }
         
    //TODO: annotate more.
    
    /**     
     * Prints the current state of the board.
     */
    static class Board implements Command
    {    	
    	public void execute (String command)
    	{
    		List<Piece> d1 = state.getDropTable1();
    		List<Piece> d2 = state.getDropTable2();
    		    	    		    		
    		for (int i = 0; i < d2.size(); i++)     		
    			System.out.print(i + ":" + d2.get(i).getDoubleCharRepresentation()+"; ");
    		
    		System.out.println ();
    		    		
    		System.out.println("  0 1 2 3 4 5 6 7 8");
    		
    		for (int i = 8; i >= 0; i--) 
    		{
    			System.out.print(i + "|");
    			for (int j = 0; j < 9; j++)     			  				
    				System.out.print(state.getPieceAt(j, i).getDoubleCharRepresentation());
    			
            System.out.println("|"+i);
    		}    		
    		System.out.println("  0 1 2 3 4 5 6 7 8");
    		for (int i = 0; i < d1.size(); i++) 
    		{
    			System.out.println(i+":"+d1.get(i).getDoubleCharRepresentation()+"; ");
    		}    		
        }
    }
    
    /**
	 * Drops the selected piece onto the table.
	 */
    static class Drop implements Command
    {    	
        public void execute (String command)
        {
            String[] parameters = command.split (" ");
            
            int allegiance = Integer.parseInt(parameters[1]);    
            
            int piece = Integer.parseInt (parameters [2]);
            
            int x = Character.getNumericValue (parameters [3].charAt (0));
            int y = Character.getNumericValue (parameters [3].charAt (1));
            if (piece < state.getCorrectDropTable(allegiance).size())
            {
            	state.dropPieceFromTable(allegiance, x, y, piece);  
            	parseInput ("board");
            }
        }
    }

    /**
	 * Prints a list of all valid commands.
	 */
    static class Help implements Command
    {    	
        public void execute (String command)
        { 
            System.out.println ("Shogi Text UI " + version);
            System.out.println ();
            for (int i = 0 ; i < commands.length ; i++)
            System.out.println (" " + commands [i]);
        }
    }

    /**
	 * Moves the piece at (x1,y1) to (x2,y2), if valid.
	 */
    static class Move implements Command
    {    	
        public void execute (String command)
        {
        	int x1 = Character.getNumericValue (command.charAt (5));
        	int y1 = Character.getNumericValue (command.charAt (6));

        	int x2 = Character.getNumericValue (command.charAt (8));
        	int y2 = Character.getNumericValue (command.charAt (9));

        	if (state.getPieceAt(x1, y1).isValidMove(state, x2, y2))
        	{
        		state.getPieceAt(x1, y1).move(state, x2, y2);  
        		parseInput ("board");
        	}       	      	
        }        
    }

    /**
	 * Promotes the piece at (x,y).
	 */
    static class Promote implements Command
    {    	
        public void execute (String command)
        {
            int x = Character.getNumericValue (command.charAt (8));
            int y = Character.getNumericValue (command.charAt (9));

            if (state.getPieceAt(x, y).isPromotable())
            {
                state.promotedPieceAt(x, y);
                parseInput ("board");
            }
        }
    }
    
    /**
	 * Stops the program.
	 */
    static class Quit implements Command
    {    	
        public void execute (String command)
        {
            running = false;
        }
    }

    /**
	 * Resets the board.
	 */
    static class Reset implements Command
    {    	
        public void execute (String command)
        {
        	state = new GameState ();
        	state.defaultBoardConfigure();
        	parseInput ("board");
        }
    }
    
} // TextUI class

/** 
 * This interface acts upon an input using polymorphism.
 */
interface Command
{
    void execute (String command);
}

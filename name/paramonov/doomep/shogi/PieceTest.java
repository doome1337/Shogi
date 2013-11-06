package name.paramonov.doomep.shogi;

import java.util.List;
import java.util.Scanner;

/** A class for testing whether or not the piece classes work.
 * Used purely for testing purposes.
 * Not meant to be used by non-developers.
 * @author                  Dmitry Andreevich Paramonov
 * @author                  Jiayin Huang
 */
public class PieceTest {
    /** Displays the state of the game at a given point.
     * Uses primitive ASCII graphics to show a visual representation of the board.
     * @param state
     */
    public static void display(GameState state) {
        List<Piece> d1 = state.getDropTable1();
        List<Piece> d2 = state.getDropTable2();
        for (int i = 0; i < d1.size(); i++) {
            System.out.println(i+":"+d1.get(i).getDoubleCharRepresentation()+"; ");
        }
        System.out.println("\n+--+--+--+--+--+--+--+--+--+");
        for (int i = 8; i >= 0; i--) {
            for (int j = 0; j < 9; j++) {
                System.out.print("+");
                System.out.print(state.getPieceAt(j, i).getDoubleCharRepresentation());
            }
            System.out.println("+");
        }
        for (int i = 0; i < d2.size(); i++) {
            System.out.println(i+":"+d2.get(i).getDoubleCharRepresentation()+"; ");
        }
    }
    
    /** Main program.
     * Contains logic and input.
     * @param args Wait, do I need to put something here? Uh... ARGS!
     */
    public static void main (String[] args) {
        GameState state = new GameState();
        state.defaultBoardConfigure();
        String input = "x";
        boolean quit = false;
        while (!quit) {
            display(state);
            input = new Scanner(System.in).nextLine().toLowerCase();
            if (input.startsWith("m")) {
                int x1 = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                input = input.substring(input.indexOf(" ")+1);
                int y1 = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                input = input.substring(input.indexOf(" ")+1);
                int x2 = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                input = input.substring(input.indexOf(" ")+1);
                int y2 = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                System.out.println(state.getPieceAt(x1, y1).isValidMove(state, x2, y2));
                System.out.println(state.getPieceAt(x1, y1).getDoubleCharRepresentation());
                if (state.getPieceAt(x1, y1).isValidMove(state, x2, y2)) {
                    state.getPieceAt(x1, y1).move(state, x2, y2);
                }
            } else if (input.startsWith("p")) {
                
            } else if (input.startsWith("d")) {
                
            } else if (input.startsWith("q")) {
                quit = true;
            }
        }
    }
}

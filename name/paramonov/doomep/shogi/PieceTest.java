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
        for (int i = 0; i < d2.size(); i++) {
            System.out.println(i+":"+d2.get(i).getDoubleCharRepresentation()+"; ");
        }
        System.out.println("  0  1  2  3  4  5  6  7  8  ");
        System.out.println(" +--+--+--+--+--+--+--+--+--+");
        for (int i = 8; i >= 0; i--) {
            System.out.print(i);
            for (int j = 0; j < 9; j++) {
                System.out.print("+");
                System.out.print(state.getPieceAt(j, i).getDoubleCharRepresentation());
            }
            System.out.println("+"+i);
        }
        System.out.println(" +--+--+--+--+--+--+--+--+--+");
        System.out.println("  0  1  2  3  4  5  6  7  8  ");
        for (int i = 0; i < d1.size(); i++) {
            System.out.println(i+":"+d1.get(i).getDoubleCharRepresentation()+"; ");
        }
        System.out.print("\n");
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
                if (state.getPieceAt(x1, y1).isValidMove(state, x2, y2)) {
                    state.getPieceAt(x1, y1).move(state, x2, y2);
                }
            } else if (input.startsWith("p")) {
                int x = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                input = input.substring(input.indexOf(" ")+1);
                int y = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                if (state.getPieceAt(x, y).isPromotable()) {
                    state.setPieceAt(x, y, state.getPieceAt(x, y).promote());
                }
            } else if (input.startsWith("d")) {
                int a = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                input = input.substring(input.indexOf(" ")+1);
                int p = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                input = input.substring(input.indexOf(" ")+1);
                int x = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                input = input.substring(input.indexOf(" ")+1);
                int y = Integer.parseInt(input.substring(input.indexOf(" ")+1, input.indexOf(" ")+2));
                if (state.getPieceAt(x, y) instanceof EmptyPiece) {
                    state.dropPieceFromTable(a, x, y, state.getCorrectDropTable(a).get(p));
                }
            } else if (input.startsWith("q")) {
                quit = true;
            }
        }
    }
}

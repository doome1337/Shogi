package name.paramonov.doomep.shogi;

import java.util.List;
import java.util.ArrayList;

/** A class representing the state of the game at any given moment.
 * Stores the board and the contents of the two drop tables.
 * Can be considered the table the game is being played on.
 * @author                  Dmitry Andreevich Paramonov
 * @author                  Jiayin Huang
 */
public class GameState {
    /** The board at the time represented by this GameState.
     * A list of lists, where each list represents a row of pieces.
     * Stores all non-filled tiles with EmptyPieces.
     */
    private List<List<Piece>> board;

    /** Player 1's (White/Bottom) drop table.
     * Stores the pieces captured by player 1.
     */
    private List<Piece> dropTable1;

    /** Player 2's (Black/Top) drop table.
     * Stores the pieces captured by player 2.
     */
    private List<Piece> dropTable2;

    /** Constructs a new, empty game state. 
     * Constructs a game state with a 9*9 board, 
     * filled with EmptyPieces, 
     * and with empty drop tables.
     */
    public GameState () {
        this.board = new ArrayList<ArrayList<Piece>>(9);
        for (int i = 0; i < 9; i++) {
            this.board.set(i, new ArrayList<Piece>(9));
        }
        this.dropTable1 = new ArrayList<Piece>(0);
        this.dropTable2 = new ArrayList<Piece>(0);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.setPieceAt(i, j, new EmptyPiece());
            }
        }
    }

    /** Returns the board at a given point in time.
     * @return              The List&lt;List&lt;Piece&gt;&gt; 
     * that represents the game board.
     */
    public List<List<Piece>> getBoard () {
        return this.board;
    }

    /** Return the drop table of player 1 at a given point in time.
     * @return              The List&lt;Piece&gt;
     * that represents the first player's drop table.
     */
    public List<Piece> getDropTable1 () {
        return this.dropTable1;
    }

    /** Return the drop table of player 1 at a given point in time.
     * @return              The List&lt;Piece&gt;
     * that represents the first player's drop table.
     */
    public List<Piece> getDropTable2 () {
        return this.dropTable2;
    }

    /** Adds a piece to the drop table of a given player.
     * Appens a Piece to the List that represents 
     * the drop table with the given allegiance.
     * @param   allegiance  The drop table to be added to. Either 1 or -1.
     * @param   piece       The piece to be added to the drop table.
     */
    public void addPieceToDropTable (int allegiance, Piece piece) {
        switch (allegiance) {
            case 1:     dropTable1.add(piece); break;
            case -1:    dropTable2.add(piece); break;
        }
        piece.setAllegiance(allegiance);
        piece.demote();
        //TODO: Finish this.
    }

    public Piece getPieceAt (int x, int y) {
        return board.get(y).get(x);
    }

    public void setPieceAt (int x, int y, Piece piece) {
        board.get(y).set(x, piece);
    }
}

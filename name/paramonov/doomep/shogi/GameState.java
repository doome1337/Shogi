package name.paramonov.doomep.shogi;

import java.util.ArrayList;
import java.util.List;

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
        this.board = new ArrayList<List<Piece>>(9);
        for (int i = 0; i < 9; i++) {
            this.board.set(i, new ArrayList<Piece>(9));
        }
        this.dropTable1 = new ArrayList<Piece>(0);
        this.dropTable2 = new ArrayList<Piece>(0);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.setPieceAt(i, j, new EmptyPiece(i, j));
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

    public List<Piece> getCorrectDropTable(int allegiance) {
        if (allegiance == 1) {
            return this.dropTable1;
        } else if (allegiance == -1) {
            return this.dropTable2;
        } else {
            return null;
        }
    }
    
    /** Adds a piece to the drop table of a given player.
     * Appends a Piece to the List that represents 
     * the drop table with the given allegiance.
     * @param   allegiance  The drop table to be added to. Either 1 or -1.
     * @param   piece       The piece to be added to the drop table.
     */
    protected void addPieceToDropTable (int allegiance, Piece piece) {
        switch (allegiance) {
            case 1:     dropTable1.add(piece); break;
            case -1:    dropTable2.add(piece); break;
        }
        piece.setAllegiance(allegiance);
        piece.demote();
        piece.setPosition(-1, -1);
        //TODO: Finish this.
        //TODO: Is this finished? Look back over plans and decide.
        //TODO: Pretty sure this is finished nao.
    }
    /** Drops a piece from the drop table onto the board.
     * Removes the piece from the drop table, 
     * and places it on the board.
     * @param allegiance
     * @param x
     * @param y
     * @param piece
     */
    protected void dropPieceFromTable (int allegiance, int x, int y, Piece piece) {
        this.setPieceAt(x, y, piece);
        piece.setPosition(x, y);
        this.getCorrectDropTable(allegiance).remove(piece);
        //TODO: This is done if addPieceToDropTable is done(). I think. Revise this anyway.
    }
    
    /** Obtains the Piece at a point on the board.
     * Returns the Piece at the given (x, y) value pair, on the board.
     * @param   x           The x-value at which we're looking for a Piece.
     * @param   y           The y-value at which we're looking for a Piece.
     * @return              The Piece at the given x and y values.
     */
    public Piece getPieceAt (int x, int y) {
        return board.get(y).get(x);
    }

    /** Places a Piece on a given tile of the board.
     * @param   x           The x-value at which we're placing the Piece.
     * @param   y           The y-value at which we're placing the Piece.
     * @param piece         The Piece to place on the board.
     */
    protected void setPieceAt (int x, int y, Piece piece) {
        board.get(y).set(x, piece);
    }

    /** Checks whether a tile is being attacked by any Piece of a given allegiance.
     * @param x             The x-value of the tile which we are checking.
     * @param y             The y-value of the tile which we are checking.
     * @param attackingAllegiance
     *                      The allegiance that we are checking whether or not the tile is being attacked.
     * @return              Whether or not the given tile is bing attacked by any Piece of the given allegiance.
     */
    public boolean isAttacked(int x, int y, int attackingAllegiance) {
        boolean isAttacked = false;
        for (int i = 0; !isAttacked && i < 9; i++) {
            for (int j = 0; !isAttacked && j < 9; j++) {
                isAttacked = this.getPieceAt(i, j).getAllegiance() == attackingAllegiance && this.getPieceAt(i, j).isUncheckedMove(this, x, y);
            }
        }
        return isAttacked;
    }
}

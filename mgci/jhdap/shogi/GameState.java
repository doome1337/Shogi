package mgci.jhdap.shogi;

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
            this.board.add(i, new ArrayList<Piece>(9));
        }
        this.dropTable1 = new ArrayList<Piece>(0);
        this.dropTable2 = new ArrayList<Piece>(0);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board.get(i).add(new EmptyPiece(i, j));
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

    /** Returns the drop table of a given player.
     * @param allegiance    The allegiance of the player
     *                      whose drop table we are looking for.
     *                      Either 1 or -1.
     * @return              The List&lt;Piece&gt;
     * that represents the given player's drop table.
     */
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
    	piece.setAllegiance(allegiance);
        piece = piece.demote();
        piece.setPosition(-1, -1);
        switch (allegiance) {
            case 1:     dropTable1.add(piece); break;
            case -1:    dropTable2.add(piece); break;
        }
        
        //TODO: Finish this.
        //TODO: Is this finished? Look back over plans and decide.
        //TODO: Pretty sure this is finished nao.
    }
    
    /** Drops a piece from the drop table onto the board.
     * Removes the piece from the drop table, 
     * and places it on the board.
     * @param allegiance    The allegiance of the drop table we're dropping from.
     * @param x             The x-value at which we're dropping the piece.
     * @param y             The y-value at which we're dropping the piece.
     * @param piece         The piece we're dropping.
     */
    protected void dropPieceFromTable (int allegiance, int x, int y, Piece piece) {
        this.setPieceAt(x, y, piece);
        piece.setPosition(x, y);
        this.getCorrectDropTable(allegiance).remove(piece);
        //TODO: This is done if addPieceToDropTable is done(). I think. Revise this anyway.
    }
    
    /** Drops a piece from the drop table onto the board.
     * Removes the piece from the drop table, 
     * and places it on the board.
     * @param allegiance    The allegiance of the drop table we're dropping from.
     * @param x             The x-value at which we're dropping the piece.
     * @param y             The y-value at which we're dropping the piece.
     * @param pieceNumberInDropTable         
     *                      The index of the piece we're dropping in the drop table.
     */
    protected void dropPieceFromTable (int allegiance, int x, int y, int pieceNumberInDropTable) {
        this.setPieceAt(x, y, this.getCorrectDropTable(allegiance).get(pieceNumberInDropTable));
        this.getCorrectDropTable(allegiance).get(pieceNumberInDropTable).setPosition(x, y);
        this.getCorrectDropTable(allegiance).remove(pieceNumberInDropTable);
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
    
    /** Promotes the piece at a given x and y value.
     * @param x             The x-value of the piece that is being promoted.
     * @param y             The y-value of the piece that is being promoted.
     */
    protected void promotePieceAt (int x, int y) {
        this.setPieceAt(x, y, this.getPieceAt(x, y).promote());
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
                isAttacked = this.getPieceAt(i, j).getAllegiance() == attackingAllegiance 
                          && this.getPieceAt(i, j).isUncheckedNonDropMove(this, x, y);
            }
        }
        return isAttacked;
    }
    
    /** Returns whether a King of a given allegiance is being attacked.
     * @param defendingAllegiance
     *                      The allegiance which we are checking for check.
     * @return              Whether the King of defendingAllegiance is under check.
     */
    public boolean isKingInCheck(int defendingAllegiance) {
        boolean tested = false;
        for (int i = 0; !tested && i < 9; i++) {
            for (int j = 0; !tested && j < 9; j++) {
                if (this.getPieceAt(i, j)/*.getCheckmatable()*/ instanceof King 
                 && this.getPieceAt(i, j).getAllegiance() == defendingAllegiance) {
                    tested = this.isAttacked(i, j, -defendingAllegiance);
                }
            }
        }
        return tested;
    }
    
    public boolean isKingCheckmated(int defendingAllegiance) {
        boolean check = this.isKingInCheck(defendingAllegiance);
        if (check) {
            for (List<Piece> row: this.getBoard()) {
                for (Piece piece: row) {
                    if (piece.getAllegiance() == defendingAllegiance) {
                        boolean[][] moves = piece.generateMoves(this);
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                if (moves[i][j]) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return check;
    }
    
    /** Returns whether a move by a given Piece would put the King of that allegiance in check. 
     * @param x             The x-value of the tile to which we're trying to move movingPiece.
     * @param y             The y-value of the tile to which we're trying to move movingPiece.
     * @param movingPiece   The Piece we are moving. 
     * @return              Whether the given move would cause check for the moving Piece.
     */
    public boolean willKingBeInCheckAfterMove(int x, int y, Piece movingPiece) {
        Piece temp = this.getPieceAt(x, y);
        this.setPieceAt(x, y, movingPiece);
        this.setPieceAt(movingPiece.x, movingPiece.y, new EmptyPiece(movingPiece.x, movingPiece.y));
        boolean tested = this.isKingInCheck(movingPiece.getAllegiance());
        this.setPieceAt(movingPiece.x, movingPiece.y, movingPiece);
        this.setPieceAt(x, y, temp);
        return tested;
    }
    
    /** Whether a drop of a piece would put the opposing king in check.
     * Used solely for pawn dropping.
     * @param x             The x-value of the tile to which we're dropping the piece.
     * @param y             The y-value of the tile to which we're dropping the piece.
     * @param droppingAllegiance
     *                      The allegiance that is dropping the Piece.
     * @param numberOfPieceToDrop
     *                      The number of the piece in the drop table.
     * @return              Whether this drop would put the enemy king in check.
     */
    public boolean willKingBeInCheckAfterDrop(int x, int y, int droppingAllegiance, int numberOfPieceToDrop) {
        this.dropPieceFromTable(droppingAllegiance, x, y, numberOfPieceToDrop);
        boolean tested = this.isKingInCheck(-droppingAllegiance);
        this.addPieceToDropTable(droppingAllegiance, this.getPieceAt(x, y));
        this.setPieceAt(x, y, new EmptyPiece(x, y));
        return tested;
    }

    /** Sets this board to be the default starting configuration of Shogi.
     */
    public void defaultBoardConfigure() {
        /* Order is the standard Ohashi order of placing pieces.
         */
        this.setPieceAt(4, 0, new King(4, 0, 1));
        this.setPieceAt(4, 8, new King(4, 8, -1));
        this.setPieceAt(3, 0, new GoldGeneral(3, 0, 1));
        this.setPieceAt(5, 8, new GoldGeneral(5, 8, -1));
        this.setPieceAt(5, 0, new GoldGeneral(5, 0, 1));
        this.setPieceAt(3, 8, new GoldGeneral(3, 8, -1));
        this.setPieceAt(2, 0, new SilverGeneral(2, 0, 1));
        this.setPieceAt(6, 8, new SilverGeneral(6, 8, -1));
        this.setPieceAt(6, 0, new SilverGeneral(6, 0, 1));
        this.setPieceAt(2, 8, new SilverGeneral(2, 8, -1));
        this.setPieceAt(1, 0, new Knight(1, 0, 1));
        this.setPieceAt(7, 8, new Knight(7, 8, -1));
        this.setPieceAt(7, 0, new Knight(7, 0, 1));
        this.setPieceAt(1, 8, new Knight(1, 8, -1));
        this.setPieceAt(0, 0, new Lance(0, 0, 1));
        this.setPieceAt(8, 8, new Lance(8, 8, -1));
        this.setPieceAt(8, 0, new Lance(8, 0, 1));
        this.setPieceAt(0, 8, new Lance(0, 8, -1));
        this.setPieceAt(1, 1, new Bishop(1, 1, 1));
        this.setPieceAt(7, 7, new Bishop(7, 7, -1));
        this.setPieceAt(7, 1, new Rook(7, 1, 1));
        this.setPieceAt(1, 7, new Rook(1, 7, -1));
        for (int i = 0; i < 9; i++) {
            this.setPieceAt(i, 2, new Pawn(i, 2, 1));
            this.setPieceAt(8-i, 6, new Pawn(8-i, 6, -1));
        }
    }
}

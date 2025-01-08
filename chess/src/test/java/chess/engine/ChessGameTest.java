package chess.engine;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Piece;
import engine.pieces.Bishop;
import engine.pieces.King;
import engine.pieces.Knight;
import engine.pieces.Pawn;
import engine.pieces.Queen;
import engine.pieces.Rook;

public class ChessGameTest {

    ChessGame controller;
    TestView view;

    // Declare variables
    Rook br1;
    Knight bk1;
    Bishop bb1;
    Queen bq;
    Bishop bb2;
    Knight bk2;
    Rook br2;
    Pawn bp1;
    Pawn bp2;
    Pawn bp3;
    Pawn bp4;
    Pawn bp5;
    Pawn bp6;
    Pawn bp7;
    Pawn bp8;

    // Declare white pieces
    Rook wr1;
    Knight wk1;
    Bishop wb1;
    Queen wq;
    Bishop wb2;
    Knight wk2;
    Rook wr2;
    Pawn wp1;
    Pawn wp2;
    Pawn wp3;
    Pawn wp4;
    Pawn wp5;
    Pawn wp6;
    Pawn wp7;
    Pawn wp8;

    King bk;
    King wk;

    private void initializeTest() {

        controller = new ChessGame();
        view = new TestView(controller);
        controller.start(view);
        
        // Initialize black pieces
        br1 = new Rook(PlayerColor.BLACK, controller);
        bk1 = new Knight(PlayerColor.BLACK, controller);
        bb1 = new Bishop(PlayerColor.BLACK, controller);
        bq = new Queen(PlayerColor.BLACK, controller);
        bb2 = new Bishop(PlayerColor.BLACK, controller);
        bk2 = new Knight(PlayerColor.BLACK, controller);
        br2 = new Rook(PlayerColor.BLACK, controller);
        bp1 = new Pawn(PlayerColor.BLACK, controller);
        bp2 = new Pawn(PlayerColor.BLACK, controller);
        bp3 = new Pawn(PlayerColor.BLACK, controller);
        bp4 = new Pawn(PlayerColor.BLACK, controller);
        bp5 = new Pawn(PlayerColor.BLACK, controller);
        bp6 = new Pawn(PlayerColor.BLACK, controller);
        bp7 = new Pawn(PlayerColor.BLACK, controller);
        bp8 = new Pawn(PlayerColor.BLACK, controller);

        // Initialize white pieces
        wr1 = new Rook(PlayerColor.WHITE, controller);
        wk1 = new Knight(PlayerColor.WHITE, controller);
        wb1 = new Bishop(PlayerColor.WHITE, controller);
        wq = new Queen(PlayerColor.WHITE, controller);
        wb2 = new Bishop(PlayerColor.WHITE, controller);
        wk2 = new Knight(PlayerColor.WHITE, controller);
        wr2 = new Rook(PlayerColor.WHITE, controller);
        wp1 = new Pawn(PlayerColor.WHITE, controller);
        wp2 = new Pawn(PlayerColor.WHITE, controller);
        wp3 = new Pawn(PlayerColor.WHITE, controller);
        wp4 = new Pawn(PlayerColor.WHITE, controller);
        wp5 = new Pawn(PlayerColor.WHITE, controller);
        wp6 = new Pawn(PlayerColor.WHITE, controller);
        wp7 = new Pawn(PlayerColor.WHITE, controller);
        wp8 = new Pawn(PlayerColor.WHITE, controller);

        // Initialize kings
        bk = new King(PlayerColor.BLACK, controller, br1, br2);
        wk = new King(PlayerColor.WHITE, controller, wr1, wr2);
    }

    private static class PieceView {
        public PieceType type;
        public PlayerColor color;

        private PieceView (PieceType type, PlayerColor color) {
            this.type = type;
            this.color = color;
        }

        static public PieceView pieceToView(Piece p) {
            if (p == null) return null;
            return new PieceView(p.getType(), p.getColor());
        }

        public boolean equals(PieceView other) {
            return (other != null) && this.type.equals(other.type) && this.color.equals(other.color);
        }

        static public PieceView[][] boardToPieceViews(Piece[][] board) {
            if (board.length == 0) return new PieceView[][]{};
            PieceView[][] pw = new PieceView[board.length][board[0].length];
            for (int x = 0; x < board.length; ++x) {
                for (int y = 0; y < board[0].length; ++y) {
                    pw[x][y] = pieceToView(board[x][y]);
                }
            }
            return pw;
        }

        static public boolean areBoardsEqual(PieceView[][] board1, PieceView[][] board2) {
            if (board1.length != board2.length) return false;
            for (int x = 0; x < board1.length; ++x) {
                if (board1[x].length != board2[x].length) return false;
                for (int y = 0; y < board1[x].length; ++y) {
                    if ((board1[x][y] == null && board2[x][y] != null)
                    || (board1[x][y] != null && !board1[x][y].equals(board2[x][y]))) {
                        System.out.println("[" + x + "," + y + "] is filles with " + board1[x][y] + " instead of " + board2[x][y]);
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public String toString () {
            return color.name().subSequence(0, 1).toString() 
                + type.name().subSequence(0, 1).toString();
        }

    }

    //TODO: We might need to use BaseView or GUI view instead ?
    private static class TestView implements ChessView {

        public ChessGame chessGame;
        public PieceView[][] board;
        public String message = "";

        public TestView(ChessGame chessGame) {
            this.chessGame = chessGame;
            this.board = new PieceView[chessGame.width()][chessGame.height()];
        }

        @Override
        public void startView() {
        }

        @Override
        public void removePiece(int x, int y) {
            board[x][y] = null;
        }

        @Override
        public void putPiece(PieceType type, PlayerColor color, int x, int y) {
            board[x][y] = new PieceView(type, color);
        }

        @Override
        public void displayMessage(String msg) {
            message = msg;
        }

        @Override
        public <T extends UserChoice> T askUser(String title, String question, T... possibilities) {
            return possibilities[0];
        }
    }

    /**
     * Play the given moves and test that ChessGame gives the correct instructions to view
     * @param turns the states of view's board before each moves
     * @param moves the moves to test
     */
    private boolean testTurns(Piece[][][] turns, int[][] movesCoordinates) {
        controller.newGame(turns[0]);
        if (!PieceView.areBoardsEqual(PieceView.boardToPieceViews(turns[0]), view.board)) {
            System.out.println("Board mismatch at initialisation");
            return false;
        }

        if (turns.length <= movesCoordinates.length) throw new IllegalArgumentException("There are only " + turns.length + " turns for " + movesCoordinates.length + " moves.");
        
        for (int i = 0; i < movesCoordinates.length; ++i) {
            if (movesCoordinates[i].length != 4) throw new IllegalArgumentException("Wrong number of coordinates to make one move."); 
            controller.move(movesCoordinates[i][0], movesCoordinates[i][1], movesCoordinates[i][2], movesCoordinates[i][3]);
            PieceView[][] v = PieceView.boardToPieceViews(turns[i + 1]);
            if (!PieceView.areBoardsEqual(v, view.board)) {
                System.out.println("Board state mismatch after move " + i);
                return false;
            }
        }
        return true;
    }

    @Test
    public void testNewGame() {
        initializeTest();

        Piece[][] initialBoard = {
            {wr2, wp8, null, null, null, null, bp8, br2},
            {wk2, wp7, null, null, null, null, bp7, bk2},
            {wb2, wp6, null, null, null, null, bp6, bb2},
            {wq, wp5, null, null, null, null, bp5, bq},
            {wk, wp4, null, null, null, null, bp4, bk},
            {wb1, wp3, null, null, null, null, bp3, bb1},
            {wk1, wp2, null, null, null, null, bp2, bk1},
            {wr1, wp1, null, null, null, null, bp1, br1}
        };

        Piece[][][] otherBoards = {{
            {wr2, wp8, null, null, null, null, bp8, br2},
            {null, null, null, null, null, null, bp7, null},
            {wb2, null, null, null, null, null, wr1, null},
            {wq, wp5, null, null, null, null, bp5, bq},
            {null, wp4, null, null, null, null, bp4, bk},
            {wb1, wp3, null, null, null, null, bp3, null},
            {null, wp2, null, null, null, null, null, bk1},
            {null, wp1, null, null, null, null, bp1, null}
        },{
            {wr2, wp8, null, null, null, null, bp8, br2},
            {wk2, null, null, null, null, null, bp7, bk2},
            {wb2, wp7, null, null, null, null, bp6, bb2},
            {wq, wp6, null, null, null, null, bp5, bq},
            {wk, wp5, null, null, null, null, bp4, bk},
            {wb1, wp4, null, null, null, null, bp3, bb1},
            {wk1, wp3, null, null, null, null, bp2, bk1},
            {wr1, wp2, null, null, null, null, bp1, br1}
        },{
            {wr1, wp1, null, null, null, null, bp1, br1},
            {null, wp2, null, null, null, null, bp2, null},
            {wb1, null, null, null, null, null, bp3, bb1},
            {wq, wp3, null, null, null, null, bp4, bq},
            {wk, wp4, null, null, null, null, bp5, bk},
            {wb2, wp5, null, null, null, null, bp6, bb2},
            {wk2, wp6, null, null, null, null, bp7, bk2},
            {wr2, wp7, null, null, null, null, bp8, br2}
        }};

        PieceView[][] defaultView = PieceView.boardToPieceViews(initialBoard);

        controller.newGame();
        assertTrue(PieceView.areBoardsEqual(view.board, defaultView));

        for (Piece[][] board : otherBoards) {
            controller.newGame(board);
            PieceView[][] boardView = PieceView.boardToPieceViews(board);
            assertTrue(PieceView.areBoardsEqual(boardView, view.board));
        }
    }

    @Test
    public void testPriseEnPassant() {
        initializeTest();

        Piece[][][] turns = {
            {
                {null, wp1, null, null, null, null, null, null},
                {null, null, null, bp1, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },{
                {null, null, null, wp1, null, null, null, null},
                {null, null, null, bp1, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },{
                {null, null, bp1, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            }
        };

        int[][] moves = {
            {0, 1, 0, 3},
            {1, 3, 0, 2}
        };
        
        assertTrue(testTurns(turns, moves));
    }
}
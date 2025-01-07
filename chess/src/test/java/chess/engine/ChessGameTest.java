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
            return this.type.equals(other.type) && this.color.equals(other.color);
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
                    if (board1[x][y] == null && board2[x][y] != null) return false;
                    if (board1[x][y] != null && !board1[x][y].equals(board2[x][y])) return false;
                }
            }
            return true;
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
        public void startView() { }

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

    @Test
    public void testNewGame() {

        ChessGame chessGame = new ChessGame();
        TestView view = new TestView(chessGame);
        chessGame.start(view);

        Rook blackRook1 = new Rook(PlayerColor.BLACK, chessGame);
        Knight blackKnight1 = new Knight(PlayerColor.BLACK, chessGame);
        Bishop blackBishop1 = new Bishop(PlayerColor.BLACK, chessGame);
        Queen blackQueen = new Queen(PlayerColor.BLACK, chessGame);
        Bishop blackBishop2 = new Bishop(PlayerColor.BLACK, chessGame);
        Knight blackKnight2 = new Knight(PlayerColor.BLACK, chessGame);
        Rook blackRook2 = new Rook(PlayerColor.BLACK, chessGame);
        Pawn blackPawn1 = new Pawn(PlayerColor.BLACK, chessGame);
        Pawn blackPawn2 = new Pawn(PlayerColor.BLACK, chessGame);
        Pawn blackPawn3 = new Pawn(PlayerColor.BLACK, chessGame);
        Pawn blackPawn4 = new Pawn(PlayerColor.BLACK, chessGame);
        Pawn blackPawn5 = new Pawn(PlayerColor.BLACK, chessGame);
        Pawn blackPawn6 = new Pawn(PlayerColor.BLACK, chessGame);
        Pawn blackPawn7 = new Pawn(PlayerColor.BLACK, chessGame);
        Pawn blackPawn8 = new Pawn(PlayerColor.BLACK, chessGame);
        
        // Define white pieces
        Rook whiteRook1 = new Rook(PlayerColor.WHITE, chessGame);
        Knight whiteKnight1 = new Knight(PlayerColor.WHITE, chessGame);
        Bishop whiteBishop1 = new Bishop(PlayerColor.WHITE, chessGame);
        Queen whiteQueen = new Queen(PlayerColor.WHITE, chessGame);
        Bishop whiteBishop2 = new Bishop(PlayerColor.WHITE, chessGame);
        Knight whiteKnight2 = new Knight(PlayerColor.WHITE, chessGame);
        Rook whiteRook2 = new Rook(PlayerColor.WHITE, chessGame);
        Pawn whitePawn1 = new Pawn(PlayerColor.WHITE, chessGame);
        Pawn whitePawn2 = new Pawn(PlayerColor.WHITE, chessGame);
        Pawn whitePawn3 = new Pawn(PlayerColor.WHITE, chessGame);
        Pawn whitePawn4 = new Pawn(PlayerColor.WHITE, chessGame);
        Pawn whitePawn5 = new Pawn(PlayerColor.WHITE, chessGame);
        Pawn whitePawn6 = new Pawn(PlayerColor.WHITE, chessGame);
        Pawn whitePawn7 = new Pawn(PlayerColor.WHITE, chessGame);
        Pawn whitePawn8 = new Pawn(PlayerColor.WHITE, chessGame);
        
        King blackKing = new King(PlayerColor.BLACK, chessGame, blackRook1, blackRook2);
        King whiteKing = new King(PlayerColor.WHITE, chessGame, whiteRook1, whiteRook2);
        
        // Initialize the board
        Piece[][] defaultBoard = {
            {blackRook1, blackKnight1, blackBishop1, blackQueen, blackKing, blackBishop2, blackKnight2, blackRook2},
            {blackPawn1, blackPawn2, blackPawn3, blackPawn4, blackPawn5, blackPawn6, blackPawn7, blackPawn8},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {null, null, null, null, null, null, null, null},
            {whitePawn1, whitePawn2, whitePawn3, whitePawn4, whitePawn5, whitePawn6, whitePawn7, whitePawn8},
            {whiteRook1, whiteKnight1, whiteBishop1, whiteQueen, whiteKing, whiteBishop2, whiteKnight2, whiteRook2}
        };

        PieceView[][] defaultView = PieceView.boardToPieceViews(defaultBoard);
        
        chessGame.newGame();
        
        // view correctly updated
        assertTrue(PieceView.areBoardsEqual(defaultView, view.board));

    }
}

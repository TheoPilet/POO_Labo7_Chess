package engine.utils;

import java.util.Arrays;
import java.util.LinkedList;

import chess.PlayerColor;
import engine.ChessGame;
import engine.Piece;
import engine.pieces.Bishop;
import engine.pieces.King;
import engine.pieces.Knight;
import engine.pieces.Pawn;
import engine.pieces.Queen;
import engine.pieces.Rook;

public interface ChessBoardInitializer {
    public final static int STANDARD_BOARD_WIDTH = 8;
    public final static int STANDARD_BOARD_HEIGHT = 8;
    final static PlayerColor WHITE = PlayerColor.WHITE;
	final static PlayerColor BLACK = PlayerColor.BLACK;

    static public Piece[][] standardInitializedBoard (ChessGame cg) {
        if (cg.width() != STANDARD_BOARD_WIDTH || cg.height() != STANDARD_BOARD_HEIGHT)
            throw new IllegalArgumentException("ChessGame board has non standard width and/or height.");
        Piece [][] board = new Piece[8][8];

		Rook rook1 = new Rook(WHITE, cg);
		Rook rook2 = new Rook(WHITE, cg);
		Rook blackRook1 = new Rook(BLACK, cg);
		Rook blackRook2 = new Rook(BLACK, cg);

		King whiteKing = new King(WHITE, cg, rook1, rook2);
		King blackKing = new King(BLACK, cg, blackRook1, blackRook2);

		LinkedList<Piece> whitePieces = new LinkedList<>(Arrays.asList(
			rook1,
			new Knight(WHITE, cg),
			new Bishop(WHITE, cg),
			whiteKing,
			new Queen(WHITE, cg),
			new Bishop(WHITE, cg),
			new Knight(WHITE, cg),
			rook2
		));

		LinkedList<Piece> blackPieces = new LinkedList<>(Arrays.asList(
			blackRook1,
			new Knight(BLACK, cg),
			new Bishop(BLACK, cg),
			blackKing,
			new Queen(BLACK, cg),
			new Bishop(BLACK, cg),
			new Knight(BLACK, cg),
			blackRook2
		));

		for (int x=0, y=0; x < cg.width(); ++x) board[x][y] = whitePieces.pop();
		for (int x=0, y=1; x < cg.width(); ++x) board[x][y] = new Pawn(WHITE, cg);
		for (int x=0, y=cg.height()-1; x < cg.width(); ++x) board[x][y] = blackPieces.pop();
		for (int x=0, y=cg.height()-2; x < cg.width(); ++x) board[x][y] = new Pawn(BLACK, cg);

        cg.setWhiteKing(whiteKing);
        cg.setBlackKing(blackKing);

        return board;
    }
}

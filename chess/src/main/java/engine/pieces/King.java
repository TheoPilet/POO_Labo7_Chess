package engine.pieces;

import chess.PlayerColor;
import engine.ChessGame;
import engine.Piece;

public class King extends Piece {
    
    private Rook rook1;
    private Rook rook2;

    public King (PlayerColor color, ChessGame chessGame, Rook rook1, Rook rook2) {
        super(color, chessGame);
        this.rook1 = rook1;
        this.rook2 = rook2;
    }
}

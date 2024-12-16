package engine;

import chess.PlayerColor;

public abstract class Piece {
    private PlayerColor color;
    private ChessGame chessGame;

    public Piece (PlayerColor color, ChessGame chessGame) {
        this.color=color;
        this.chessGame=chessGame;
    }

    public Move[] availableMoves(){
        return new Move[0];
    };

    public PlayerColor getColor(){
        return null;
    }

    private Move[] getMovesInDirection(){
        return null;
    };
}
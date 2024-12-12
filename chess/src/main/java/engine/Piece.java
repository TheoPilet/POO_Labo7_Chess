package engine;

import chess.PlayerColor;

public abstract class Piece {
    private PlayerColor color;
    private ChessGame chessGame;

    protected Piece () {
        
    }

    public Move[] availableMoves(){
        return new Move[0];
    };

    public PlayerColor getColor(){
        return null;
    }
}
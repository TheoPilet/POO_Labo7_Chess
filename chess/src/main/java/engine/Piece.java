package engine;

import chess.PieceType;
import chess.PlayerColor;
import engine.utils.*;

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
        return color;
    }

    private boolean hasTheSameColor(Piece piece){
        return piece.color == this.color;
    }

    public abstract PieceType getType();

    private Move[] getMovesInDirection(Direction d, int limit, Position from, int tour){

        boolean canGoFurther = true;

        Position pos = new Position(0, 0);
        //tant que limite est pas atteinte ou que pièce bloquante -> pièce à soi
        for(int i = 0; canGoFurther; pos.next(d), i++){
            if(i == limit || hasTheSameColor(chessGame.at(pos))){
                canGoFurther = false;
                break;
            }
            
        }
        return null;
    };
}
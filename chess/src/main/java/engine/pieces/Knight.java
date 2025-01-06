package engine.pieces;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;
import engine.utils.Position;

public class Knight extends Piece{
    
    public Knight (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
    }

    public PieceType getType(){
        return PieceType.KNIGHT;
    }

    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);

        Direction[] directions = {  new Direction(1, 2),
                            new Direction(-1, 2),
                            new Direction(1, -2),
                            new Direction(-1, -2)
                        };
        
        for(Direction dir : directions){
            availableMoves.addAll(getMovesInDirection(dir, 1, from));
        }
        
        return availableMoves;
    }
}
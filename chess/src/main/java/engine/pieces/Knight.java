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
            Position pos = new Position(from.x(), from.y()); //deep copy ou une connerie comme ça?
            pos.next(dir); //a faire plus joli
            if(isSquareAvailable(pos)){
                availableMoves.add(new Move(this, chessGame.at(pos), from, pos));
            }
        }
        
        return availableMoves;
    }
}
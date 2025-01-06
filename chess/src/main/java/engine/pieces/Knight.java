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
        
        for(Direction d : Direction.ALL_DIRECTIONS){
            // à implémtenr spécial !!
        }
        
        Direction d = Direction(1,2);
        return availableMoves;

        
        // We can't go further if :
        //1. we've reached the nb of case max we can reach (limit) -> limit = -1 if infinite
        //2. a piece from our color blocks the way
        //3. we would go beyond the board
        //4. we eat a piece 
        boolean canGoFurther = true;

        LinkedList<Move> moves = new LinkedList<>();
        Position pos = new Position(from.x(), from.y());

        for(int i = 0; canGoFurther; pos.next(d), i++){
        
            if(i == limit || hasTheSameColor(chessGame.at(pos)) || !chessGame.isOnBoard(pos)){
                canGoFurther = false;
                break;
            }
            if(!hasTheSameColor(chessGame.at(pos))){
                canGoFurther = false;
            }
            moves.add(new Move(this, chessGame.at(pos), from, pos));
        }
        return moves;
    }
}
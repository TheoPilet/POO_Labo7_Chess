package engine.pieces;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;
import engine.utils.Position;

public class Pawn extends Piece{

    private Direction[] directionsToEat;
    private Direction directionToMove;
    
    public Pawn (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
        directionsToEat = this.color==PlayerColor.WHITE ? 
            new Direction[]{Direction.UP_LEFT, Direction.UP_RIGHT} : 
            new Direction[]{Direction.DOWN_LEFT, Direction.DOWN_RIGHT};
        directionToMove = this.color==PlayerColor.WHITE ? Direction.UP : Direction.DOWN;
    }

    public PieceType getType(){
        return PieceType.PAWN;
    }


    private LinkedList<Move> eats(Position pos, Position from){
        LinkedList<Move> moves = new LinkedList<>();

        for(Direction dir : directionsToEat){ //only eats in diagonal
            if(!hasTheSameColor(chessGame.at(pos))){
                moves.addAll(0, getMovesInDirection(dir, ONE_SQUARE_LIMIT, from));
            }
        }

        return moves;
    }

    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        Position pos = from.copy();

        if(chessGame.hasMoved(this)){
            availableMoves.addAll(0, getMovesInDirection(directionToMove, ONE_SQUARE_LIMIT, from).stream().filter((Move m) -> m.pieceEaten == null).toList());
        } else {
            availableMoves.addAll(0, getMovesInDirection(directionToMove, TWO_SQUARES_LIMIT, from).stream().filter((Move m) -> m.pieceEaten == null).toList());
        }

        //Moves where the pawn can eat
        availableMoves.addAll(0, eats(pos, from));
        
        return availableMoves;
    }
}   
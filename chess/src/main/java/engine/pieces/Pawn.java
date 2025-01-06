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

    private LinkedList<Move> eats(Position pos, Position from, int limit){
        LinkedList<Move> moves = new LinkedList<>();

        for(Direction dir : directionsToEat){
            if(!hasTheSameColor(chessGame.at(pos))){
                moves.addAll(0, getMovesInDirection(dir, limit, from));
            }
        }

        return moves;
    }

    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        Position pos = new Position(from.x(), from.y()); //deep copy ou une connerie comme ça?


        if(chessGame.hasMoved(this)){
            availableMoves.addAll(0, eats(pos, from, ONE_SQUARE_LIMIT));
        } else {
            availableMoves.addAll(0, eats(pos, from, TWO_SQUARES_LIMIT));
        }

        availableMoves.addAll(0, getMovesInDirection(directionToMove, ONE_SQUARE_LIMIT, from).stream().filter((Move m) -> m.pieceEaten == null).toList());
        
        return availableMoves;
    }
}   
package engine.pieces;

import java.util.LinkedList;
import java.util.stream.Collectors;

import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;
import engine.utils.Position;

public class Pawn extends Piece{

    private final Direction[] directionsToEat;
    private final Direction directionToMove;
    
    public Pawn (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
        directionsToEat = this.color==PlayerColor.WHITE ?
            new Direction[]{Direction.UP_LEFT, Direction.UP_RIGHT} :
            new Direction[]{Direction.DOWN_LEFT, Direction.DOWN_RIGHT};
        directionToMove = this.color==PlayerColor.WHITE ? Direction.UP : Direction.DOWN;
    }

    @Override
    public PieceType getType(){
        return PieceType.PAWN;
    }


    private void addEatMoves(LinkedList<Move> moves, Position from){
        for(Direction dir : directionsToEat) //only eats in diagonal
            moves.addAll(0, getMovesInDirection(dir, ONE_SQUARE_LIMIT, from)
                .stream().filter((Move m) -> m.pieceEaten != null).toList());
    }

    @Override
    public LinkedList<Move> availableMoves(){
        
        Position from = chessGame.where(this);
        int forwardDistance = chessGame.hasMoved(this) ? ONE_SQUARE_LIMIT : TWO_SQUARES_LIMIT;
        LinkedList<Move> availableMoves = getMovesInDirection(directionToMove, forwardDistance, from)
            .stream().filter((Move m) -> m.pieceEaten == null)
            .collect(Collectors.toCollection(LinkedList::new));

        //Moves where the pawn can eat
        addEatMoves(availableMoves, from);
        
        return availableMoves;
    }
}   
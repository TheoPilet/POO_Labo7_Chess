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


    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        // Position fr
        Position pos = new Position(from.x(), from.y()); //deep copy ou une connerie comme ça?


        if(chessGame.hasMoved(this)){

            for(Direction dir : directionsToEat){
                pos.next(dir);
                if(!hasTheSameColor(chessGame.at(pos))){
                    availableMoves.add(new Move(this, chessGame.at(pos), from, pos));
                }
            }
        }
        
        // for(Direction d : Direction.UP){
        // }

        // à implémenter spécial
        
        return availableMoves;
    }
}   
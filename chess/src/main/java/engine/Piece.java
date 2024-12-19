package engine;

import java.util.LinkedList;

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

    public PlayerColor getColor(){
        return color;
    }

    private boolean hasTheSameColor(Piece piece){
        return piece.color == this.color;
    }

    public abstract PieceType getType();

    protected LinkedList<Move> getMovesInDirection(Direction d, int limit, Position from){

        // We can't go further if :
        //1. we've reached the nb of case max we can reach (limit) -> limit = -1 if infinite
        //2. a piece from our color blocks the way
        //3. we would go beyond the board
        //4. we eat a piece 
        boolean canGoFurther = true;

        LinkedList<Move> moves = new LinkedList<>();
        Position pos = new Position(0, 0);

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
    };

    public abstract LinkedList<Move> availableMoves();
}
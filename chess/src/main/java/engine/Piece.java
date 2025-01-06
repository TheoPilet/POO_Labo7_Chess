package engine;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.utils.Direction;
import engine.utils.Position;

public abstract class Piece {

    protected final int INFINITE_LIMIT = -1;
    protected final int ONE_SQUARE_LIMIT = 1;

    protected PlayerColor color;
    protected ChessGame chessGame;

    public Piece (PlayerColor color, ChessGame chessGame) {
        this.color=color;
        this.chessGame=chessGame;
    }

    public PlayerColor getColor(){
        return color;
    }

    protected boolean hasTheSameColor(Piece piece){
        return piece.color == this.color;
    }

    public abstract PieceType getType();

    protected boolean isSquareAvailable(Position pos){
        return hasTheSameColor(chessGame.at(pos)) || !chessGame.isOnBoard(pos);
    }

    protected LinkedList<Move> getMovesInDirection(Direction d, int limit, Position from){

        // We can't go further if :
        //1. we've reached the nb of case max we can reach (limit) -> limit = -1 if infinite
        //2. a piece from our color blocks the way
        //3. we would go beyond the board
        //4. we eat a piece 
        boolean canGoFurther = true;

        LinkedList<Move> moves = new LinkedList<>();
        Position pos = from.copy();

        for(int i = 0; canGoFurther; pos.next(d), i++){
        
            if(i == limit || isSquareAvailable(pos)){
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
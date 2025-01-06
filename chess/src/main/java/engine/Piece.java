package engine;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.utils.Direction;
import engine.utils.Position;

public abstract class Piece {

    protected final int INFINITE_LIMIT = Integer.MAX_VALUE;
    protected final int ONE_SQUARE_LIMIT = 1;
    protected final int TWO_SQUARES_LIMIT = 2;

    protected PlayerColor color;
    protected ChessGame chessGame;

    public Piece (PlayerColor color, ChessGame chessGame) {
        this.color=color;
        this.chessGame=chessGame;
    }

    public PlayerColor getColor(){
        return color;
    }

    public abstract PieceType getType();

    protected LinkedList<Move> getMovesInDirection(Direction d, int limit, Position from){

        // We can't go further if :
        //1. we've reached the nb of case max we can reach (limit) -> limit = -1 if infinite
        //2. a piece from our color blocks the way
        //3. we would go beyond the board
        //4. we eat a piece 

        LinkedList<Move> moves = new LinkedList<>();
        Position pos = from.next(d);

        Piece content;
        for(int i = 0; chessGame.isOnBoard(pos) && i < limit; i++, pos = pos.next(d)){
            content = chessGame.at(pos);

            if (content != null && content.color.equals(this.color)) break;
            moves.add(new Move(this, content, from, pos));
            if (content != null) break;
        }
        return moves;
    };

    public abstract LinkedList<Move> availableMoves();
}
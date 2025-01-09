package engine;

import java.util.LinkedList;

import chess.ChessView;
import chess.PieceType;
import chess.PlayerColor;
import engine.utils.Direction;
import engine.utils.Position;

/**
 * Defines the class Piece that implements UserChoice
 * @author Bénédicte Vernet & Benoît Jaouen & Théo Pilet
 */
public abstract class Piece implements ChessView.UserChoice {

    // The limit of the squares the piece can access
    protected final int INFINITE_LIMIT = Integer.MAX_VALUE;
    protected final int ONE_SQUARE_LIMIT = 1;
    protected final int TWO_SQUARES_LIMIT = 2;

    protected PlayerColor color;
    protected ChessGame chessGame;

    /**
     * Creates a constructor of Piece
     * @param color    the color of the piece
     * @param chessGame     the chess game we're playing with
     */
    public Piece (PlayerColor color, ChessGame chessGame) {
        this.color=color;
        this.chessGame=chessGame;
    }

    /**
     * Getter to know the color of the piece
     * @return the PlayerColor of the piece
     */
    public PlayerColor getColor(){
        return color;
    }

    /**
     * Abstract getter to know the type of the piece
     * @return the PieceType of the piece
     */
    public abstract PieceType getType();

    /**
     * Method to get all moves that are authorized in one direction
     * @param d the direction in which you want to check all the movements it can make
     * @param limit the max number of squares that the piece can move to
     * @param from the position from where the piece comes
     * @return a LinkedList of Moves with all moves that the piece can do
     */
    protected LinkedList<Move> getMovesInDirection(Direction d, int limit, Position from){

        // We can't go further if :
        //1. we've reached the nb of case max we can reach (limit)
        //2. a piece from our color blocks the way
        //3. we would go beyond the board
        //4. we eat a piece

        LinkedList<Move> moves = new LinkedList<>();
        Position pos = from.next(d);

        Piece content = null;
        for(int i = 0; chessGame.isOnBoard(pos) && i < limit && content == null; i++, pos = pos.next(d)){
            content = chessGame.at(pos); // updated once we enter the loop
            if (content == null || !content.color.equals(this.color)) moves.add(new Move(this, content, from, pos));
        }
        return moves;
    };

    /**
     * Method that tells if the piece can be promoted or not
     * No piece can be promoted except for the pawn
     * @return  true if the piece can be promoted and false if it can't
     */
    public boolean canBePromotedAt (Position p) { return false; }

    /**
     * Abstract method that returns all the moves that the piece can do
     * @return  a LinkedList of Moves 
     */
    public abstract LinkedList<Move> availableMoves();

    /**
     * Method to have the type of the piece in string
     * @return  the String of the type name
     */
    @Override
    public String toString() {
        return getType().name();
    }
}
package engine.pieces;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;
import engine.utils.Position;

/**
 * Defines the class Knight that inherits from Piece
 * @author Bénédicte Vernet & Benoît Jaouen & Théo Pilet
 */
public class Knight extends Piece{
    
    /**
     * Creates a constructor of Knight
     * @param color    the color of the piece
     * @param chessGame     the chess game we're playing with
     */
    public Knight (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
    }

    /**
     * Getter to know the type of the piece
     * @return  the PieceType KNIGHT
     */
    @Override
    public PieceType getType(){
        return PieceType.KNIGHT;
    }

    /**
     * Method that returns all the moves that the piece can do
     * @return  a LinkedList of Moves 
     */
    @Override
    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();
        Position from = chessGame.where(this);
        
        // all directions that the knight could go
        for(Direction dir : new Direction[]{
            new Direction(1, 2),
            new Direction(1, -2),
            new Direction(-1, 2),
            new Direction(-1, -2),
            new Direction(2, 1),
            new Direction(2, -1),
            new Direction(-2, 1),
            new Direction(-2, -1)
        }) {
            availableMoves.addAll(getMovesInDirection(dir, ONE_SQUARE_LIMIT, from));
        }
        
        return availableMoves;
    }
}
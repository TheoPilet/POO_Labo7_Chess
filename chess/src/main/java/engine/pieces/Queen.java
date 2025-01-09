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
 * Defines the class Queen that inherits from Piece
 * @author Bénédicte Vernet & Benoît Jaouen & Théo Pilet
 */
public class Queen extends Piece {
   
    /**
     * Creates a constructor of Queen
     * @param color    the color of the piece
     * @param chessGame     the chess game we're playing with
     */
    public Queen (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
    }

    /**
     * Getter to know the type of the piece
     * @return  the PieceType QUEEN
     */
    @Override
    public PieceType getType(){
        return PieceType.QUEEN;
    }

    /**
     * Method that returns all the moves that the piece can do
     * @return  a LinkedList of Moves 
     */
    @Override
    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        
        for(Direction d : Direction.ALL_DIRECTIONS){
            availableMoves.addAll(0, getMovesInDirection(d, INFINITE_LIMIT, from));
        }
        
        return availableMoves;
    }
}

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

/**
 * Defines the class Pawn that inherits from Piece
 * @author Bénédicte Vernet & Benoît Jaouen & Théo Pilet
 */
public class Pawn extends Piece{

    private final Direction[] directionsToEat; // a pawn can only eat in diagonal
    private final Direction directionToMove; // a pawn can only move forward without eating
    
    /**
     * Creates a constructor of Pawn, initialize all directions to eat and to move
     * @param color    the color of the piece
     * @param chessGame     the chess game we're playing with
     */
    public Pawn (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
        // The pawn's direction depends on its color
        directionsToEat = this.color==PlayerColor.WHITE ? 
            new Direction[]{Direction.UP_LEFT, Direction.UP_RIGHT} :
            new Direction[]{Direction.DOWN_LEFT, Direction.DOWN_RIGHT};
        directionToMove = this.color==PlayerColor.WHITE ? Direction.UP : Direction.DOWN;
    }

    /**
     * Getter to know the type of the piece
     * @return  the PieceType PAWN
     */
    @Override
    public PieceType getType(){
        return PieceType.PAWN;
    }

    /**
     * Private method to add all moves that allow the pawn to eat and deals with "la prise en passant"
     * @return  the PieceType KNIGHT
     */
    private void addEatMoves(LinkedList<Move> moves, Position from){
        for(Direction dir : directionsToEat) //only eats in diagonal
            moves.addAll(0, getMovesInDirection(dir, ONE_SQUARE_LIMIT, from)
                .stream().filter((Move m) -> m.pieceEaten != null).toList()); // filter to only get the moves where the pawn can eat another Piece

        // prise en passant :
        Move m = chessGame.lastMove();

        if (m != null
            && Math.abs(m.to.x - from.x) == 1 && m.to.y == from.y   // the last moved piece is next to the pawn
            && m.pieceMoved.getType().equals(this.getType())        // is also a pawn
            && !m.pieceMoved.getColor().equals(this.getColor())     // is not of the same color
            && Math.abs(m.to.y - m.from.y) > 1) {                   // and has moved two cases at once
            // since the other pawn cannot jump over a piece, we have no conflicting eating move already stored in moves
            Move move = new Move(this, null, from, m.to.next(directionToMove), 
                new Move(m.pieceMoved, m.pieceMoved, m.to, m.to));
            moves.add(move);
            // the second move is essentially the other piece eating itself
        }
    }

    /**
     * Method that returns all the moves that the piece can do
     * @return  a LinkedList of Moves 
     */
    @Override
    public LinkedList<Move> availableMoves(){

        Position from = chessGame.where(this);
        int forwardDistance = chessGame.hasMoved(this) ? ONE_SQUARE_LIMIT : TWO_SQUARES_LIMIT;
        LinkedList<Move> availableMoves = getMovesInDirection(directionToMove, forwardDistance, from)
            .stream().filter((Move m) -> m.pieceEaten == null)
            .collect(Collectors.toCollection(LinkedList::new));

        // Moves where the pawn can eat
        addEatMoves(availableMoves, from);
        
        return availableMoves;
    }

    /**
     * Method that tells if the pawn can be promoted or not
     * @return  true if the pawn can be promoted and false if it can't
     */
    @Override
    public boolean canBePromotedAt(Position p) {
        return (this.color.equals(PlayerColor.WHITE) && p.y == (chessGame.height()-1))
            || this.color.equals(PlayerColor.BLACK) && p.y == 0;
    }
}
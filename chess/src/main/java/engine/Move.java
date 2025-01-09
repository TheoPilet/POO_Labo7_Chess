package engine;

import engine.utils.Position;

/**
 * Defines the class Move
 * @author Bénédicte Vernet & Benoît Jaouen & Théo Pilet
 */
public class Move {
    public final Piece pieceMoved;
    public final Piece pieceEaten;
    public final Position from;
    public final Position to;
    public final Move secondMove; // to account for Rocks

    /**
     * Creates a constructor of Move without secondMove
     * @param pieceMoved    the piece that would be moved
     * @param pieceEaten     the piece that the moved piece eventually eats
     * @param from  the position the Piece went from
     * @param to the position the Piece would move to
     */
    public Move(Piece pieceMoved, Piece pieceEaten, Position from, Position to) {
        this.pieceMoved = pieceMoved;
        this.pieceEaten = pieceEaten;
        this.from = from;
        this.to = to;
        this.secondMove = null; // Set to null when not provided
    }

    /**
     * Creates a constructor of Move with secondMove
     * @param pieceMoved    the piece that would be moved
     * @param pieceEaten     the piece that the moved piece eventually eats
     * @param from  the position the Piece went from
     * @param to the position the Piece would move to
     * @param secondMove the second move associated with the first one (two moves in one turn)
     */
    public Move(Piece pieceMoved, Piece pieceEaten, Position from, Position to, Move secondMove) {
        this.pieceMoved = pieceMoved;
        this.pieceEaten = pieceEaten;
        this.from = from;
        this.to = to;
        this.secondMove = secondMove;
    }

    /**
     * Method to have the move in a string format
     * @return  the String of the move
     */
    @Override
    public String toString () {
        return pieceMoved.getType().name() + " : " + from + " -> " + to;
    }
}
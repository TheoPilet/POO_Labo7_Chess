package engine;

import engine.utils.Position;

public class Move {
    public final Piece pieceMoved;
    public final Piece pieceEaten;
    public final Position from;
    public final Position to;
    public final Move secondMove; // to account for Rocks

    // Constructor without secondMove
    public Move(Piece pieceMoved, Piece pieceEaten, Position from, Position to) {
        this.pieceMoved = pieceMoved;
        this.pieceEaten = pieceEaten;
        this.from = from;
        this.to = to;
        this.secondMove = null; // Set to null when not provided
    }

    // Constructor with secondMove
    public Move(Piece pieceMoved, Piece pieceEaten, Position from, Position to, Move secondMove) {
        this.pieceMoved = pieceMoved;
        this.pieceEaten = pieceEaten;
        this.from = from;
        this.to = to;
        this.secondMove = secondMove;
    }
}
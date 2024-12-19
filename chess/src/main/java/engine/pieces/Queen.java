package engine.pieces;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;

public class Queen extends Piece {
    
    public Queen (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
    }

    public PieceType getType(){
        return PieceType.QUEEN;
    }

    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();
        
        availableMoves.addAll(0, getMovesInDirection(Direction.UP, -1, null));
        availableMoves.addAll(0, getMovesInDirection(Direction.UP, -1, null));
    }
}

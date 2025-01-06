package engine.pieces;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;
import engine.utils.Position;

public class King extends Piece {
    
    private Rook rook1;
    private Rook rook2;

    public King (PlayerColor color, ChessGame chessGame, Rook rook1, Rook rook2) {
        super(color, chessGame);
        this.rook1 = rook1;
        this.rook2 = rook2;
    }

    public PieceType getType(){
        return PieceType.KING;
    }

    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        
        for(Direction d : Direction.ALL_DIRECTIONS){
            availableMoves.addAll(0, getMovesInDirection(d, ONE_MOVE_LIMIT, from));
        }
        
        return availableMoves;
    }
}

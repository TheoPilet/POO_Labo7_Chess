package engine.pieces;

import java.util.LinkedList;


import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;
import engine.utils.Position;

public class Queen extends Piece {
    
    public Queen (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
    }

    public PieceType getType(){
        return PieceType.QUEEN;
    }

    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        
        for(Direction d : Direction.ALL_DIRECTIONS){
            availableMoves.addAll(0, getMovesInDirection(d, INFINITE_LIMIT, from));
        }
        
        return availableMoves;
    }
}

package engine.pieces;

import java.util.LinkedList;

import chess.PieceType;
import chess.PlayerColor;
import engine.ChessGame;
import engine.Move;
import engine.Piece;
import engine.utils.Direction;
import engine.utils.Position;

public class Pawn extends Piece{
    
    public Pawn (PlayerColor color, ChessGame chessGame) {
        super(color, chessGame);
    }

    public PieceType getType(){
        return PieceType.PAWN;
    }

    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        
        // for(Direction d : Direction.UP){
        // }

        // à implémenter spécial
        
        return availableMoves;
    }
}   
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
    
    private Rook[] rooks;

    public King (PlayerColor color, ChessGame chessGame, Rook rook1, Rook rook2) {
        super(color, chessGame);
        this.rooks = new Rook[] {rook1, rook2};
    }

    @Override
    public PieceType getType(){
        return PieceType.KING;
    }

    @Override
    public LinkedList<Move> availableMoves(){

        LinkedList<Move> availableMoves = new LinkedList<>();

        Position from = chessGame.where(this);
        
        for(Direction d : Direction.ALL_DIRECTIONS){
            availableMoves.addAll(0, getMovesInDirection(d, ONE_SQUARE_LIMIT, from));
        }

        // Big and little rock
        if (!chessGame.hasMoved(this)) {
            for (Rook r : rooks) {
                //System.out.println(this.getColor() + " " + this.getType().name() + " " + chessGame.hasMoved(this));
                //System.out.println(r.getColor() + " " + r.getType().name() + " " + chessGame.hasMoved(r));

                if (!chessGame.hasMoved(r)) {
                    Position pk = chessGame.where(this);
                    Position pr = chessGame.where(r);
                    // gets unitary direction toward the rook
                    Direction d = new Direction(Math.abs(pr.x - pk.x) / (pr.x - pk.x), 0);

                    Position p = pk.next(d);
                    //System.out.println(d.dx);

                    // if the king and the rook haven't moved, the rook is on this trajectory
                    for (; chessGame.at(p) == null && !chessGame.isThreatened(this.getColor(), p); p = p.next(d))

                    if (p.equals(pr)) {
                        availableMoves.add(new Move(this, null, from, from.next(d).next(d),
                            new Move(r, null, pr, from.next(d))));
                    }
                }
            }
            
        }
        
        return availableMoves;
    }
}

package engine;

import chess.PlayerColor;

public abstract class Piece {
    private PlayerColor color;
    public Move[] availableMoves(){
        return new Move[0];
    };

    public PlayerColor getColor(){

    }
}

public class Pawn extends Piece{

}

public class Rook extends Piece{

}

public class Knight extends Piece{

}

public class Bishop extends Piece{

}

public class King extends Piece{

}

public class Qeen extends Piece{

}

package engine.utils;

public class Position implements Cloneable {
    public final int x;
    public final int y;

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Position p){
        return (x==p.x && y==p.y);
    }

    public Position next(Direction d) {
        return new Position(x + d.dx, y + d.dy);
    }

    @Override
    public String toString () {
        return "(" + x + "," + y + ")";
    }

    public Position copy() {
        return new Position(x, y);
    }

}
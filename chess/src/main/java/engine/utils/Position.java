package engine.utils;

public class Position implements Cloneable {
    private int x;
    private int y;

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x(){
        return x;
    }

    public int y(){
        return y;
    }

    public boolean equals(Position p){
        return (x==p.x() && y==p.y());
    }

    public Position next(Direction d) {
        x += d.dx;
        y += d.dy;
        return this;
    }

    @Override
    public String toString () {
        return "(" + x + "," + y + ")";
    }

    public Position copy() {
        return new Position(x, y);
    }

}
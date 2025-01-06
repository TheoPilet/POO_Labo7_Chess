package engine.utils;

public class Position {
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

    public Position copy() {
        return new Position(x, y);
    }

}
package engine.utils;

public class Position {
    public final int x;
    public final int y;

    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position next(Direction d) {
        return new Position(x + d.dx, y + d.dy);
    }
}
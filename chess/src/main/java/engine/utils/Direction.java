package engine.utils;

public class Direction {
    public final int dx;
    public final int dy;

    public static final Direction UP = new Direction(0, 1);
    public static final Direction UP_LEFT = new Direction(-1, 1);
    public static final Direction LEFT = new Direction(-1, 0);
    public static final Direction DOWN_LEFT = new Direction(-1, -1);
    public static final Direction DOWN = new Direction(0, -1);
    public static final Direction DOWN_RIGHT = new Direction(1, -1);
    public static final Direction RIGHT = new Direction(1, 0);
    public static final Direction UP_RIGHT = new Direction(1, 1);

    public static final Direction[] STRAIGHT_DIRECTIONS = {UP, LEFT, DOWN, RIGHT};
    public static final Direction[] DIAGONAL_DIRECTIONS = {UP_LEFT, DOWN_LEFT, DOWN_RIGHT, UP_RIGHT};
    public static final Direction[] ALL_DIRECTIONS = {UP, LEFT, DOWN, RIGHT, UP_LEFT, DOWN_LEFT, DOWN_RIGHT, UP_RIGHT};

    public Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}

package engine.utils;

public enum Direction {
    UP(0, 1),
    UP_LEFT(-1, 1),
    LEFT(-1, 0),
    DOWN_LEFT(-1, -1),
    DOWN(0, -1),
    DOWN_RIGHT(1, -1),
    RIGHT(1, 0),
    UP_RIGHT(1, 1);

    public final int dx;
    public final int dy;

    private Direction (int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static final Direction[] STRAIGHT_DIRECTIONS = {UP, LEFT, DOWN, RIGHT};
    public static final Direction[] DIAGONAL_DIRECTIONS = {UP_LEFT, DOWN_LEFT, DOWN_RIGHT, UP_RIGHT};
    public static final Direction[] ALL_DIRECTIONS = {UP, LEFT, DOWN, RIGHT, UP_LEFT, DOWN_LEFT, DOWN_RIGHT, UP_RIGHT};
}

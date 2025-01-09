package engine.utils;

/**
 * Defines the class Position that implements the interface Cloneable
 * @author Bénédicte Vernet & Benoît Jaouen & Théo Pilet
 */
public class Position implements Cloneable {

    public final int x;
    public final int y;

    /**
     * Creates a constructor of Position
     * @param x    position on the x-axis (horizontal)
     * @param y     position on the y-axis (vertical)
     */
    public Position (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Method to know if two positions are equals
     * @param p    the other position
     * @return     true if both positions are equals and false if not
     */
    public boolean equals(Position p){
        return (x==p.x && y==p.y);
    }

    /**
     * Method to go to the next position based on a direction
     * @param d    the direction where we want to go
     * @return     the new position
     */
    public Position next(Direction d) {
        return new Position(x + d.dx, y + d.dy);
    }

    @Override
    public String toString () {
        return "(" + x + "," + y + ")";
    }

    /**
     * Method to copy a Position
     * @return     a copy of the Position
     */
    public Position copy() {
        return new Position(x, y);
    }

}
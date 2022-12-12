package agh.ics.oop;

public class Vector2d {
    private int x;
    private int y;

    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public String toString() {
        return "(" + x + "," + y + ")";
    }
    public boolean precedes(Vector2d other) {
        if (x > other.x || y > other.y) {
            return false;
        }
        return true;
    }
    public boolean follows(Vector2d other) {
        if (x < other.x || y < other.y) {
            return false;
        }
        return true;
    }
    public Vector2d add(Vector2d other) {
        return new Vector2d(x+other.x, y+other.y);
    }
    public Vector2d subtract(Vector2d other) {
        return new Vector2d(x-other.x, y-other.y);
    }
    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }
    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }
    public Vector2d opposite() {
        return new Vector2d(-x, -y);
    }
    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(!(other instanceof  Vector2d)){
            return false;
        }

        Vector2d that = (Vector2d) other;

        return this.x == that.x && this.y == that.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

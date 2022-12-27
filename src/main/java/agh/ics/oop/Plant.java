package agh.ics.oop;

public class Plant implements IMapElement{
    private Vector2d location;

    Plant(Vector2d location) {
        this.location = location;
    }

    public Vector2d getLocation() {
        return location;
    }

    public String toString() {
        return "*";
    }
}

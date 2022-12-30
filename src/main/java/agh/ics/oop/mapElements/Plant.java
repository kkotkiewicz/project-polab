package agh.ics.oop.mapElements;

import agh.ics.oop.attributes.Vector2d;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Plant implements IMapElement {
    private Vector2d location;

    public Plant(Vector2d location) {
        this.location = location;
    }

    public Vector2d getLocation() {
        return location;
    }

    public String toString() {
        return "*";
    }

    @Override
    public Circle toShape(int startingEnergy, int size) {
        Circle circle = new Circle();
        circle.setRadius(size);
        circle.setFill(Color.rgb(0, 255, 0));
        return circle;
    }
}

package agh.ics.oop.mapElements;

import agh.ics.oop.attributes.Vector2d;
import javafx.scene.shape.Circle;

public interface IMapElement {
    Vector2d getLocation();

    Circle toShape(int startingEnergy, int size);
}

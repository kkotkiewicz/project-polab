package agh.ics.oop;

import java.util.HashMap;

public interface IGrassGenerator {

    public Vector2d generateGrass(HashMap<Vector2d, Plant> plantPositions);
}

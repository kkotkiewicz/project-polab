package agh.ics.oop.grassGenerators;

import agh.ics.oop.attributes.Vector2d;
import agh.ics.oop.mapElements.Plant;

import java.util.HashMap;

public class ToxicGrassGenerator implements IGrassGenerator{
    private final int width;
    private final int height;
    public ToxicGrassGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vector2d generateGrass(HashMap<Vector2d, Plant> plantPositions){
        return null;
    }
}
